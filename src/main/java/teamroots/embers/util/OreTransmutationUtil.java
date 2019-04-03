package teamroots.embers.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.SoundManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageMetallurgicDustFX;

import java.util.*;
import java.util.stream.Collectors;

public class OreTransmutationUtil {
    public static final int ITERATIONS_PER_TICK = 10;
    public static final int MAX_BLOCKS = 16;

    public static final String STONE = "stone";
    public static final String NETHER = "nether";
    public static final String END = "end";
    public static final String SAND = "sand";
    public static final String BETWEEN_STONE = "betweenlands";
    public static final String BETWEEN_PIT = "betweenlands_pit";
    public static final String BETWEEN_GEM = "betweenlands_gem";
    public static final HashMap<String, String> ALTERNATE_ORES = new HashMap<>();
    public static final double FAIL_CHANCE = 0.1;

    static LinkedHashMap<String, TransmutationSet> REGISTRY = new LinkedHashMap<>();
    static ArrayList<TransmutationIterator> iterators = new ArrayList<>();
    static int iteratorIndex;
    static Random random = new Random();


    public static void init() {
        registerTransmutationSet(STONE, Blocks.STONE.getDefaultState());
        registerTransmutationSet(NETHER, Blocks.NETHERRACK.getDefaultState());
        registerTransmutationSet(END, Blocks.END_STONE.getDefaultState());
        registerTransmutationSet(SAND, Blocks.SAND.getDefaultState());
        registerTransmutationSet(BETWEEN_STONE, new ResourceLocation("thebetweenlands:betweenstone"), 0);
        registerTransmutationSet(BETWEEN_PIT, new ResourceLocation("thebetweenlands:pitstone"), 0);
        registerTransmutationSet(BETWEEN_GEM, new ResourceLocation("thebetweenlands:mud"), 0);

        registerOre(STONE, Blocks.LIT_REDSTONE_ORE.getDefaultState()); //workaround for a vanilla issue
        registerOre(BETWEEN_PIT, new ResourceLocation("betweenores:lit_redstone_ore"), 0);

        ALTERNATE_ORES.put("minecraft:quartz_ore", NETHER);
        ALTERNATE_ORES.put("tconstruct:ore", NETHER);
        ALTERNATE_ORES.put("astralsorcery:blockcustomsandore", SAND);
        ALTERNATE_ORES.put("thebetweenlands:slimy_bone_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("thebetweenlands:sulfur_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("thebetweenlands:syrmorite_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("thebetweenlands:octine_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("thebetweenlands:valonite_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("thebetweenlands:scabyst_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("thebetweenlands:aqua_middle_gem_ore", BETWEEN_GEM);
        ALTERNATE_ORES.put("thebetweenlands:crimson_middle_gem_ore", BETWEEN_GEM);
        ALTERNATE_ORES.put("thebetweenlands:green_middle_gem_ore", BETWEEN_GEM);
        ALTERNATE_ORES.put("betweenores:iron_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("betweenores:copper_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("betweenores:tin_ore", BETWEEN_STONE);
        ALTERNATE_ORES.put("betweenores:gold_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:redstone_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:lapis_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:diamond_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:lead_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:nickel_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:silver_ore", BETWEEN_PIT);
        ALTERNATE_ORES.put("betweenores:quartz_ore", BETWEEN_PIT);
        
        //How about this
        //You click the make pr button if you want more support

        gatherOreTransmutations();

        MinecraftForge.EVENT_BUS.register(OreTransmutationUtil.class);
    }

    public static void gatherOreTransmutations() {
        HashSet<String> existingTags = new HashSet<>();

        for (String orename : OreDictionary.getOreNames()) {
            if (orename == null || !orename.startsWith("ore") || !isValidOre(orename))
                continue;
            for (ItemStack stack : OreDictionary.getOres(orename, false)) {
                Item item = stack.getItem();
                if (!(item instanceof ItemBlock))
                    continue;
                ResourceLocation registryName = item.getRegistryName();
                if (registryName == null)
                    continue; //WEE WOO WEE WOO
                String entry = ALTERNATE_ORES.getOrDefault(registryName.toString(), STONE);
                String tag = entry + ":" + orename;
                if (existingTags.contains(tag))
                    continue;
                if (!ForgeRegistries.BLOCKS.containsKey(registryName))
                    return;
                Block block = ForgeRegistries.BLOCKS.getValue(registryName);
                IBlockState defaultState = block.getDefaultState();
                if (!defaultState.isFullCube())
                    continue;
                if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
                    registerOre(entry, block);
                else
                    registerOre(entry, block.getStateFromMeta(stack.getMetadata()));
                existingTags.add(tag);
            }
        }
    }

    private static boolean isValidOre(String orename) {
        return true;
    }

    public static void registerTransmutationSet(String name, IBlockState failure) {
        REGISTRY.put(name, new TransmutationSet(name, failure));
    }

    public static void registerTransmutationSet(String name, ResourceLocation failure, int meta) {
        if (!ForgeRegistries.BLOCKS.containsKey(failure))
            return;
        Block block = ForgeRegistries.BLOCKS.getValue(failure);
        registerTransmutationSet(name, block.getStateFromMeta(meta));
    }

    public static void registerOre(String name, IBlockState ore) {
        TransmutationSet set = REGISTRY.get(name);
        if (set != null) {
            set.ores.add(ore);
        }
    }

    public static void registerOre(String name, Block ore) {
        TransmutationSet set = REGISTRY.get(name);
        if (set != null) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            ore.getSubBlocks(CreativeTabs.SEARCH, stacks);
            for (ItemStack stack : stacks) {
                set.ores.add(ore.getStateFromMeta(stack.getMetadata()));
            }
        }
    }

    public static void registerOre(String name, ResourceLocation ore, int meta) {
        if (!ForgeRegistries.BLOCKS.containsKey(ore))
            return;
        Block block = ForgeRegistries.BLOCKS.getValue(ore);
        registerOre(name, block.getStateFromMeta(meta));
    }

    public static void registerOre(String name, ResourceLocation ore) {
        if (!ForgeRegistries.BLOCKS.containsKey(ore))
            return;
        Block block = ForgeRegistries.BLOCKS.getValue(ore);
        registerOre(name, block);
    }

    public static TransmutationSet getFromOre(IBlockState state) {
        for (TransmutationSet entry : REGISTRY.values())
            if (entry.ores.contains(state))
                return entry;
        return null;
    }

    public static boolean transmuteOres(World world, BlockPos pos) {
        return transmuteOres(world, pos, MAX_BLOCKS);
    }

    public static boolean transmuteOres(World world, BlockPos pos, int limit) {
        IBlockState fromReplace = world.getBlockState(pos);
        TransmutationSet set = getFromOre(fromReplace);
        if (set != null) {
            List<IBlockState> replacements = set.ores.stream().filter(ore -> ore != fromReplace).collect(Collectors.toList());
            if (replacements.isEmpty())
                return false;
            IBlockState toReplace = replacements.get(random.nextInt(replacements.size()));
            iterators.add(new TransmutationIterator(world, pos, fromReplace, toReplace, set.failure, limit));
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            return;
        if (iterators.isEmpty())
            return;
        for (int i = 0; i < ITERATIONS_PER_TICK; i++) {
            TransmutationIterator iterator = iterators.get(iteratorIndex % iterators.size());
            iterator.iterate();
            iteratorIndex++;
        }
        iterators.removeIf(TransmutationIterator::isDone);
    }

    public static class TransmutationSet {
        public String name;
        public IBlockState failure;
        public HashSet<IBlockState> ores = new HashSet<>();

        public TransmutationSet(String name, IBlockState failure) {
            this.name = name;
            this.failure = failure;
        }
    }

    public static class TransmutationIterator {
        public Random random = new Random();
        public World world;
        public IBlockState fromReplace;
        public IBlockState toReplace;
        public IBlockState toFailure;
        public HashSet<BlockPos> visitedPositions = new HashSet<>();
        public ArrayList<BlockPos> toVisit = new ArrayList<>();
        public int maxBlocks = Integer.MAX_VALUE;

        public TransmutationIterator(World world, BlockPos start, IBlockState fromReplace, IBlockState toReplace, IBlockState toFailure, int maxBlocks) {
            this.world = world;
            this.fromReplace = fromReplace;
            this.toReplace = toReplace;
            this.toFailure = toFailure;
            this.toVisit.add(start);
            this.maxBlocks = maxBlocks;
        }

        public boolean isDone() {
            return toVisit.isEmpty() || visitedPositions.size() >= maxBlocks;
        }

        public void iterate() {
            if (toVisit.isEmpty())
                return;
            int index = random.nextInt(toVisit.size());
            BlockPos visit = toVisit.get(index);
            toVisit.remove(index);
            IBlockState state = world.getBlockState(visit);
            if (state != fromReplace)
                return;
            boolean failed = random.nextDouble() < FAIL_CHANCE;
            world.setBlockState(visit, failed ? toFailure : toReplace, 2);
            world.playSound(null,visit, failed ? SoundManager.METALLURGIC_DUST_FAIL : SoundManager.METALLURGIC_DUST, SoundCategory.BLOCKS, 1.0f, random.nextFloat()+0.5f);
            if(failed)
                world.playEvent(2001, visit, Block.getStateId(state));
            else
                PacketHandler.INSTANCE.sendToAll(new MessageMetallurgicDustFX(visit.getX(),visit.getY(),visit.getZ()));
            visitedPositions.add(visit);
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighbor = visit.offset(facing);
                if (!visitedPositions.contains(neighbor))
                    toVisit.add(neighbor);
            }
        }
    }
}
