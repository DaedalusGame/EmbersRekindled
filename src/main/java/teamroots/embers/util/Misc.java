package teamroots.embers.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.particle.ParticleUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Misc {
    public static final double LOG_E = Math.log10(Math.exp(1));
    public static Random random = new Random();

    public static boolean isValidLever(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockLever) {
            EnumFacing face = state.getValue(BlockLever.FACING).getFacing();
            return face == side;
        } else if (block instanceof BlockButton) {
            EnumFacing face = state.getValue(BlockButton.FACING);
            return face == side;
        } else if (block instanceof BlockRedstoneTorch) {
            EnumFacing face = state.getValue(BlockRedstoneTorch.FACING);
            return face == side;
        }
        return false;
    }

    //TODO: DANNY DELETO
    public static EnumFacing getOppositeFace(EnumFacing face) {
        return face.getOpposite();
    }

    public static EnumFacing getOppositeHorizontalFace(EnumFacing face) {
        return face.getAxis().isHorizontal() ? face.getOpposite() : face;
    }

    public static EnumFacing getOppositeVerticalFace(EnumFacing face) {
        return face.getAxis().isVertical() ? face.getOpposite() : face;
    }

    public static ItemStack getRepairItem(ItemStack stack) {
        if (stack.getItem() instanceof ItemTool) {
            ItemStack mat = ToolMaterial.valueOf(((ItemTool) stack.getItem()).getToolMaterialName()).getRepairItemStack().copy();
            if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                mat.setItemDamage(0);
            }
            return mat;
        }
        if (stack.getItem() instanceof ItemSword) {
            ItemStack mat = ToolMaterial.valueOf(((ItemSword) stack.getItem()).getToolMaterialName()).getRepairItemStack().copy();
            if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                mat.setItemDamage(0);
            }
            return mat;
        }
        if (stack.getItem() instanceof ItemArmor) {
            ItemStack mat = ((ItemArmor) stack.getItem()).getArmorMaterial().getRepairItemStack().copy();
            if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                mat.setItemDamage(0);
            }
            return mat;
        }
        return ItemStack.EMPTY;
    }

    public static List<TileEntity> getAdjacentTiles(World world, BlockPos pos) {
        List<TileEntity> tiles = new ArrayList<>();
        tiles.add(world.getTileEntity(pos.up()));
        tiles.add(world.getTileEntity(pos.down()));
        tiles.add(world.getTileEntity(pos.west()));
        tiles.add(world.getTileEntity(pos.east()));
        tiles.add(world.getTileEntity(pos.north()));
        tiles.add(world.getTileEntity(pos.south()));
        return tiles;
    }

    public static EntityItem rayTraceItem(World world, double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        double x = posX;
        double y = posY;
        double z = posZ;
        for (int i = 0; i < 120; i++) {
            x += dirX / 20.0f;
            y += dirY / 20.0f;
            z += dirZ / 20.0f;
            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - 0.2, y - 0.2, z - 0.2, x + 0.2, y + 0.2, z + 0.2));
            if (items.size() > 0) {
                return items.get(0);
            }
        }
        return null;
    }

    public static int intColor(int r, int g, int b) {
        return (r * 65536 + g * 256 + b);
    }

    public static boolean matchOreDict(ItemStack stack1, ItemStack stack2) {
        int[] keys1 = OreDictionary.getOreIDs(stack1);
        int[] keys2 = OreDictionary.getOreIDs(stack2);
        for (int i = 0; i < keys1.length; i++) {
            for (int j = 0; j < keys2.length; j++) {
                if (keys1[i] == keys2[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean betweenAngles(float angleTest, float angleLow, float angleHigh) {
        boolean between = angleTest >= angleLow && angleTest <= angleHigh;
        if (angleHigh < angleLow) {
            between = angleTest >= angleLow && angleTest < 360 || angleTest > 0 && angleTest <= angleHigh;
        }
        return between;
    }

    public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
        float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
        return f;
    }

    public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
        return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
    }

    public static int getResourceCount(ItemStack stack) {
        int baseCount = 0;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            if (armor.armorType == EntityEquipmentSlot.HEAD)
                baseCount = 5;
            if (armor.armorType == EntityEquipmentSlot.CHEST)
                baseCount = 8;
            if (armor.armorType == EntityEquipmentSlot.LEGS)
                baseCount = 7;
            if (armor.armorType == EntityEquipmentSlot.FEET)
                baseCount = 4;
        }
        if (stack.getItem() instanceof ItemSword)
            baseCount = 2;
        if (stack.getItem() instanceof ItemBow)
            baseCount = 3;
        if (stack.getItem() instanceof ItemTool) {
            if (stack.getItem() instanceof ItemPickaxe || stack.getItem().getHarvestLevel(stack, "pickaxe", null, null) > -1)
                baseCount = 3;
            else if (stack.getItem() instanceof ItemAxe || stack.getItem().getHarvestLevel(stack, "axe", null, null) > -1)
                baseCount = 3;
            else if (stack.getItem() instanceof ItemHoe)
                baseCount = 2;
            else if (stack.getItem() instanceof ItemSpade)
                baseCount = 1;
            else
                baseCount = 1;
        }
        if (baseCount > 0) {
            return (int) ((float) baseCount * (1.0f - (float) stack.getItemDamage() / (float) stack.getMaxDamage()));
        }
        return -1;
    }

    public static boolean isHills(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS);
    }

    public static boolean isExtremeHills(Biome biome) {
        return biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_EDGE.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0;
    }

    public static void spawnInventoryInWorld(World world, double x, double y, double z, IItemHandler inventory) {
        if (inventory != null && !world.isRemote) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                if (!inventory.getStackInSlot(i).isEmpty()) {
                    world.spawnEntity(new EntityItem(world, x, y, z, inventory.getStackInSlot(i)));
                }
            }
        }
    }

    public static ItemStack getStackFromState(IBlockState state) {
        if (state == null)
            return ItemStack.EMPTY;
        Block block = state.getBlock();
        int meta = block.damageDropped(state);
        return new ItemStack(block, 1, meta);
    }

    public static void syncTE(TileEntity tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 10); //Does a good job I hope
    }

    @Nullable
    public static RayTraceResult findEntityOnPath(World world, @Nullable Entity projectile, Entity shooter, AxisAlignedBB projectileAABB, Vec3d start, Vec3d end, com.google.common.base.Predicate<Entity> matcher) {
        RayTraceResult pickedEntity = null;
        double motionX = end.x - start.x;
        double motionY = end.y - start.y;
        double motionZ = end.z - start.z;
        List<Entity> list = world.getEntitiesInAABBexcluding(projectile, projectileAABB.expand(motionX, motionY, motionZ).grow(1.0D), matcher);
        double pickedDistance = 0.0D;

        for (Entity entity : list) {
            if (entity != shooter) {
                AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(0.3);
                RayTraceResult raytraceresult = aabb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    double distance = start.squareDistanceTo(raytraceresult.hitVec);

                    if (distance < pickedDistance || pickedDistance == 0.0D) {
                        raytraceresult.typeOfHit = RayTraceResult.Type.ENTITY;
                        raytraceresult.entityHit = entity;
                        pickedEntity = raytraceresult;
                        pickedDistance = distance;
                    }
                }
            }
        }

        return pickedEntity;
    }

    @Nullable
    public static List<RayTraceResult> findEntitiesOnPath(World world, @Nullable Entity projectile, Entity shooter, AxisAlignedBB projectileAABB, Vec3d start, Vec3d end, com.google.common.base.Predicate<Entity> matcher) {
        List<RayTraceResult> entities = new ArrayList<>();
        double motionX = end.x - start.x;
        double motionY = end.y - start.y;
        double motionZ = end.z - start.z;
        List<Entity> list = world.getEntitiesInAABBexcluding(projectile, projectileAABB.expand(motionX, motionY, motionZ).grow(1.0D), matcher);

        for (Entity entity : list) {
            if (entity != shooter) {
                AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(0.3);
                RayTraceResult raytraceresult = aabb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    raytraceresult.typeOfHit = RayTraceResult.Type.ENTITY;
                    raytraceresult.entityHit = entity;

                    entities.add(raytraceresult);
                }
            }
        }

        entities.sort((o1, o2) -> Double.compare(start.squareDistanceTo(o1.hitVec), start.squareDistanceTo(o2.hitVec)));

        return entities;
    }

    public static double getDiminishedPower(double power, double softcap, double slope) {
        if (power > softcap)
            return softcap * slope + Math.log10(power - softcap + LOG_E / slope) - Math.log10(LOG_E / slope);
        else
            return power * slope;
    }

    public static void spawnClogParticles(World world, BlockPos pos, int spouts, float radius) {
        Random localRandom = new Random(pos.hashCode());

        for (int i = 0; i < spouts; i++) {
            double angleA = localRandom.nextDouble() * Math.PI * 2;
            double angleB = localRandom.nextDouble() * Math.PI * 2;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.01875f;
            float vx = xOffset * speed + random.nextFloat() * speed * 0.3f;
            float vy = yOffset * speed + random.nextFloat() * speed * 0.3f;
            float vz = zOffset * speed + random.nextFloat() * speed * 0.3f;
            ParticleUtil.spawnParticleVapor(world, pos.getX() + 0.5f + xOffset * radius, pos.getY() + 0.5f + yOffset * radius, pos.getZ() + 0.5f + zOffset * radius, vx, vy, vz, 64, 64, 64, 64, 0.2f, 3.0f, 40);
        }
    }

    public static IItemHandler makeRestrictedItemHandler(IItemHandler handler, boolean input, boolean output) {
        return new IItemHandler() {
            @Override
            public int getSlots() {
                return handler.getSlots();
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return handler.getStackInSlot(slot);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!input)
                    return stack;
                return handler.insertItem(slot,stack,simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if(!output)
                    return ItemStack.EMPTY;
                return handler.extractItem(slot,amount,simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                return handler.getSlotLimit(slot);
            }
        };
    }

    public static IFluidHandler makeRestrictedFluidHandler(IFluidHandler handler, boolean input, boolean output) {
        return new IFluidHandler() {
            @Override
            public IFluidTankProperties[] getTankProperties() {
                return handler.getTankProperties();
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if(!input)
                    return 0;
                return handler.fill(resource,doFill);
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                if(!output)
                    return null;
                return handler.drain(resource,doDrain);
            }

            @Nullable
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                if(!output)
                    return null;
                return handler.drain(maxDrain,doDrain);
            }
        };
    }

    public static RayTraceResult raytraceMultiAABB(List<AxisAlignedBB> aabbs, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = Lists.newArrayList();

        list.addAll(aabbs.stream().map(axisalignedbb -> rayTrace2(pos, start, end, axisalignedbb)).collect(Collectors.toList()));

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for(RayTraceResult raytraceresult : list) {
            if(raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if(d0 > d1) {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

    private static RayTraceResult rayTrace2(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        Vec3d vec3d = start.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
        Vec3d vec3d1 = end.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), raytraceresult.sideHit, pos);
    }
}