package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockEmberGauge;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityReactor extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraDialInformation, IExtraCapabilityInformation {
    public static final float BASE_MULTIPLIER = 1.0f;
    public static final int PROCESS_TIME = 20;
    public IEmberCapability capability = new DefaultEmberCapability() {
        @Override
        public void onContentsChanged() {
            TileEntityReactor.this.markDirty();
        }
    };
    Random random = new Random();
    int progress = -1;
    private List<IUpgradeProvider> upgrades = new ArrayList<>();

    public static final int SOUND_HAS_EMBER = 1;
    public static final int[] SOUND_IDS = new int[]{SOUND_HAS_EMBER};

    HashSet<Integer> soundsPlaying = new HashSet<>();

    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityReactor.this.markDirty();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (EmbersAPI.getEmberValue(stack) == 0) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }
    };
    private double catalyzerMult;
    private double combustorMult;

    public TileEntityReactor() {
        super();
        capability.setEmberCapacity(64000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("inventory", inventory.serializeNBT());
        capability.writeToNBT(tag);
        tag.setInteger("progress", progress);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        capability.readFromNBT(tag);
        if (tag.hasKey("progress")) {
            progress = tag.getInteger("progress");
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                            EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory);
        world.setTileEntity(pos, null);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        } else if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        } else if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            return (T) this.capability;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        upgrades = UpgradeUtil.getUpgrades(world, pos, EnumFacing.HORIZONTALS);
        UpgradeUtil.verifyUpgrades(this, upgrades);
        if (getWorld().isRemote)
            handleSound();
        boolean cancel = UpgradeUtil.doWork(this,upgrades);
        if (!cancel && !inventory.getStackInSlot(0).isEmpty()) {
            progress++;
            if (progress > UpgradeUtil.getWorkTime(this, PROCESS_TIME, upgrades)) {
                catalyzerMult = 0.0f;
                combustorMult = 0.0f;
                float multiplier = BASE_MULTIPLIER;
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    TileEntity tile = world.getTileEntity(getPos().offset(facing).down());
                    if (tile instanceof TileEntityCatalyzer)
                        catalyzerMult += ((TileEntityCatalyzer) tile).multiplier;
                    if (tile instanceof TileEntityCombustor)
                        combustorMult += ((TileEntityCombustor) tile).multiplier;
                }
                if (Math.max(combustorMult, catalyzerMult) < 2.0f * Math.min(combustorMult, catalyzerMult)) {
                    multiplier += combustorMult;
                    multiplier += catalyzerMult;
                    progress = 0;
                    int i = 0;
                    if (inventory != null) {
                        ItemStack emberStack = inventory.getStackInSlot(i);
                        double emberValue = EmbersAPI.getEmberValue(emberStack);
                        double ember = UpgradeUtil.getTotalEmberProduction(this, multiplier * emberValue, upgrades);
                        if (ember > 0 && capability.getEmber() + ember <= capability.getEmberCapacity()) {
                            if (!world.isRemote) {
                                world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, SoundManager.IGNEM_REACTOR, SoundCategory.BLOCKS, 1.0f, 1.0f);
                                PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f));
                            }
                            UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.PRODUCE, ember), upgrades);
                            capability.addAmount(ember, true);
                            inventory.extractItem(i, 1, false);
                            markDirty();
                        }
                    }
                }
            }
            markDirty();
        }
        if (this.capability.getEmber() > 0 && getWorld().isRemote) {
            double catalyzerRatio = 0.0;
            if (catalyzerMult > 0 || combustorMult > 0)
                catalyzerRatio = catalyzerMult / (catalyzerMult + combustorMult);
            int r = (int) MathHelper.clampedLerp(255, 255, catalyzerRatio);
            int g = (int) MathHelper.clampedLerp(64, 64, catalyzerRatio);
            int b = (int) MathHelper.clampedLerp(16, 64, catalyzerRatio);
            for (int i = 0; i < Math.ceil(this.capability.getEmber() / 500.0); i++) {
                float vx = (float) MathHelper.clampedLerp(0, (random.nextFloat() - 0.5) * 0.1f, catalyzerRatio);
                float vy = (float) MathHelper.clampedLerp(random.nextFloat() * 0.05f, (random.nextFloat() - 0.5) * 0.2f, catalyzerRatio);
                float vz = (float) MathHelper.clampedLerp(0, (random.nextFloat() - 0.5) * 0.1f, catalyzerRatio);
                float size = (float) MathHelper.clampedLerp(4.0, 2.0, catalyzerRatio);
                int lifetime = (16 + random.nextInt(16));
                ParticleUtil.spawnParticleGlow(getWorld(), getPos().getX() + 0.25f + random.nextFloat() * 0.5f, getPos().getY() + 0.25f + random.nextFloat() * 0.5f, getPos().getZ() + 0.25f + random.nextFloat() * 0.5f, vx, vy, vz, r, g, b, size, lifetime);
            }
        }
    }

    @Override
    public void playSound(int id) {
        switch (id) {
            case SOUND_HAS_EMBER:
                Embers.proxy.playMachineSound(this, SOUND_HAS_EMBER, SoundManager.GENERATOR_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f);
                break;
        }
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        return id == SOUND_HAS_EMBER && capability.getEmber() > 0;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
        if(BlockEmberGauge.DIAL_TYPE.equals(dialType) && Math.max(combustorMult, catalyzerMult) < 2.0f * Math.min(combustorMult, catalyzerMult)) {
            DecimalFormat multiplierFormat = Embers.proxy.getDecimalFormat("embers.decimal_format.ember_multiplier");
            double multiplier = BASE_MULTIPLIER + combustorMult + catalyzerMult;
            information.add(I18n.format("embers.tooltip.dial.ember_multiplier",multiplierFormat.format(multiplier)));
        }
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return true;
    }

    @Override
    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT,"embers.tooltip.goggles.item",I18n.format("embers.tooltip.goggles.item.ember")));
        if(capability == EmbersCapabilities.EMBER_CAPABILITY)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.OUTPUT,"embers.tooltip.goggles.ember",null));
    }
}
