package teamroots.embers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.item.IEmberChargedTool;
import teamroots.embers.api.item.IInflictorGem;
import teamroots.embers.api.item.IInflictorGemHolder;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.api.block.IDial;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.item.*;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.network.message.MessageEmberGenOffset;
import teamroots.embers.network.message.MessageTyrfingBurstFX;
import teamroots.embers.proxy.ClientProxy;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.tileentity.ITileEntitySpecialRendererLater;
import teamroots.embers.tileentity.TileEntityMechAccessor;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.world.EmberWorldData;

import java.util.*;
import java.util.stream.Collectors;

public class EventManager {
    double gaugeAngle = 0;
    public static boolean hasRenderedParticles = false;
    Random random = new Random();
    public static float emberEyeView = 0;
    public static ResearchBase lastResearch = null;
    public static float frameTime = 0;
    public static float frameCounter = 0;
    public static long prevTime = 0;
    public static EnumHand lastHand = EnumHand.MAIN_HAND;
    public static float starlightRed = 255;
    public static float starlightGreen = 32;
    public static float starlightBlue = 255;
    public static float tickCounter = 0;
    public static double currentEmber = 0;
    public static boolean allowPlayerRenderEvent = true;
    public static int ticks = 0;
    public static float prevCooledStrength = 0;
    public static boolean acceptUpdates = true;

    //public static Map<BlockPos, TileEntity> toUpdate = new HashMap<BlockPos, TileEntity>();
    //public static Map<BlockPos, TileEntity> overflow = new HashMap<BlockPos, TileEntity>();

    static EntityPlayer clientPlayer = null;

	/*public static void markTEForUpdate(BlockPos pos, TileEntity tile){
        if (!tile.getWorld().isRemote && acceptUpdates){
			if (!toUpdate.containsKey(pos)){
				toUpdate.put(pos, tile);
			}
			else {
				toUpdate.replace(pos, tile);
			}
		}
		else if (!tile.getWorld().isRemote){
			if (!overflow.containsKey(pos)){
				overflow.put(pos, tile);
			}
			else {
				overflow.replace(pos, tile);
			}
		}
	}*/

    private static ThreadLocal<Boolean> captureDrops = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<NonNullList<ItemStack>> capturedDrops = ThreadLocal.withInitial(NonNullList::create);

    public static NonNullList<ItemStack> captureDrops(boolean start) {
        if (start) {
            captureDrops.set(true);
            capturedDrops.get().clear();
            return NonNullList.create();
        } else {
            captureDrops.set(false);
            return capturedDrops.get();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void captureDrops(HarvestDropsEvent event) {
        World world = event.getWorld();
        if (world.isRemote)
            return;
        float chance = event.getDropChance();
        if (captureDrops.get()) {
            NonNullList<ItemStack> stacks = capturedDrops.get();
            for (ItemStack stack : event.getDrops()) {
                if (stack != null && !stack.isEmpty() && world.rand.nextFloat() <= chance)
                    stacks.add(stack);
            }
            event.getDrops().clear();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event) {
        ResourceLocation particleGlow = new ResourceLocation("embers:entity/particle_mote");
        event.getMap().registerSprite(particleGlow);
        ResourceLocation particleSparkle = new ResourceLocation("embers:entity/particle_star");
        event.getMap().registerSprite(particleSparkle);
        ResourceLocation particleSmoke = new ResourceLocation("embers:entity/particle_smoke");
        event.getMap().registerSprite(particleSmoke);

        stitchFluid(event.getMap(), RegistryManager.fluid_alchemical_redstone);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_lead);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_tin);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_aluminum);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_bronze);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_copper);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_dawnstone);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_electrum);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_gold);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_iron);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_nickel);
        stitchFluid(event.getMap(), RegistryManager.fluid_molten_silver);
        stitchFluid(event.getMap(), RegistryManager.fluid_steam);
    }

    @SideOnly(Side.CLIENT)
    private void stitchFluid(TextureMap map, Fluid fluid) {
        map.registerSprite(fluid.getStill());
        map.registerSprite(fluid.getFlowing());
    }

    @SubscribeEvent
    public void onServerTick(WorldTickEvent event) {
        if (event.world.provider.getDimensionType() == DimensionType.OVERWORLD) {
            if (Misc.random.nextInt(400) == 0) {
                EmberGenUtil.offX++;
                EmberWorldData.get(event.world).markDirty();
            }
            if (Misc.random.nextInt(400) == 0) {
                EmberGenUtil.offZ++;
                EmberWorldData.get(event.world).markDirty();
            }
            PacketHandler.INSTANCE.sendToAll(new MessageEmberGenOffset(EmberGenUtil.offX, EmberGenUtil.offZ));
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (entityLiving instanceof EntityPlayer) {
            attuneInflictorGem(entityLiving, source, entityLiving.getHeldItemMainhand());
            attuneInflictorGem(entityLiving, source, entityLiving.getHeldItemOffhand());
        }
        applyInflictorGemResistance(event, entityLiving.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        applyInflictorGemResistance(event, entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        applyInflictorGemResistance(event, entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
        applyInflictorGemResistance(event, entityLiving.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        for (ItemStack s : entityLiving.getEquipmentAndArmor()) {
            if (s.getItem() instanceof ItemArmor) {
                addHeat(entityLiving, s, 5.0f);
            }
        }
        if (source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer damager = (EntityPlayer) source.getTrueSource();
            ItemStack s = damager.getHeldItemMainhand();
            if (!s.isEmpty()) {
                addHeat(entityLiving, s, 1.0f);
            }
        }
    }

    private void attuneInflictorGem(EntityLivingBase entityLiving, DamageSource source, ItemStack stack) {
        if (stack.getItem() instanceof IInflictorGem) {
            ((IInflictorGem) stack.getItem()).attuneSource(stack, entityLiving, source);
        }
    }

    private void applyInflictorGemResistance(LivingHurtEvent event, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IInflictorGemHolder) {
            IInflictorGemHolder inflictorGemHolder = (IInflictorGemHolder) item;
            float mult = Math.max(0, 1.0f - inflictorGemHolder.getTotalDamageResistance(event.getEntityLiving(), event.getSource(), stack));
            if (mult == 0) {
                event.setCanceled(true);
            }
            event.setAmount(event.getAmount() * mult);
        }
    }

    private void addHeat(EntityLivingBase entity, ItemStack stack, float added) {
        if (ItemModUtil.hasHeat(stack)) {
            double maxHeat = ItemModUtil.getMaxHeat(stack);
            double heat = ItemModUtil.getHeat(stack);
            if (heat < maxHeat) {
                ItemModUtil.addHeat(stack, added);
                if (heat + added >= maxHeat)
                    entity.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ, SoundManager.HEATED_ITEM_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGameOverlayRender(RenderGameOverlayEvent.Post e) {
        if (e.getType() == ElementType.TEXT) {
            EventManager.frameCounter++;
            EventManager.frameTime = (System.nanoTime() - prevTime) / 1000000000.0f;
            EventManager.prevTime = System.nanoTime();
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        boolean showBar = false;

        int w = e.getResolution().getScaledWidth();
        int h = e.getResolution().getScaledHeight();

        int x = w / 2;
        int y = h / 2;
        if (!player.getHeldItemMainhand().isEmpty()) {
            if (player.getHeldItemMainhand().getItem() instanceof ItemEmberGauge) {
                showBar = true;
            }
        }
        if (!player.getHeldItemOffhand().isEmpty()) {
            if (player.getHeldItemOffhand().getItem() instanceof ItemEmberGauge) {
                showBar = true;
            }
        }

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder b = tess.getBuffer();
        if (showBar) {
            World world = player.getEntityWorld();
            if (e.getType() == ElementType.TEXT) {
                GlStateManager.disableDepth();
                GlStateManager.disableCull();
                GlStateManager.pushMatrix();
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/ember_meter_overlay.png"));
                GlStateManager.color(1f, 1f, 1f, 1f);

                int offsetX = 0;

                b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                RenderUtil.drawQuadGui(b, 0, x - 16, y - 4, x + 16, y - 4, x + 16, y - 36, x - 16, y - 36, 0, 0, 1, 1);
                tess.draw();

                double angle = 195.0;
                EmberWorldData data = EmberWorldData.get(world);
                if (player != null) {
                    //if (data.emberData != null){
                    //if (data.emberData.containsKey(""+((int)player.posX) / 16 + " " + ((int)player.posZ) / 16)){
                    double ratio = EmberGenUtil.getEmberDensity(world.getSeed(), player.getPosition().getX(), player.getPosition().getZ());
                    if (gaugeAngle == 0) {
                        gaugeAngle = 165.0 + 210.0 * ratio;
                    } else {
                        gaugeAngle = gaugeAngle * 0.99 + 0.01 * (165.0 + 210.0 * ratio);
                    }
                    //}
                    //}
                }

                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/ember_meter_pointer.png"));
                GlStateManager.translate(x, y - 20, 0);
                GlStateManager.rotate((float) gaugeAngle, 0, 0, 1);
                b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                RenderUtil.drawQuadGui(b, 0.0, -2.5f, 13.5f, 13.5f, 13.5f, 13.5f, -2.5f, -2.5f, -2.5f, 0, 0, 1, 1);
                tess.draw();

                GlStateManager.popMatrix();
                GlStateManager.enableCull();
                GlStateManager.enableDepth();
            }
        }
        World world = player.getEntityWorld();
        RayTraceResult result = player.rayTrace(6.0, e.getPartialTicks());

        if (result != null) {
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                IBlockState state = world.getBlockState(result.getBlockPos());
                EnumFacing facing = result.sideHit;
                List<String> text = Lists.newArrayList();
                if (state.getBlock() instanceof IDial) {
                    text.addAll(((IDial) state.getBlock()).getDisplayInfo(world, result.getBlockPos(), state));
                }
                if (Embers.proxy.isPlayerWearingGoggles()) {
                    TileEntity tileEntity = world.getTileEntity(result.getBlockPos());
                    if (tileEntity != null) {
                        addCapabilityInformation(text, tileEntity, facing);
                    }
                }

                for (int i = 0; i < text.size(); i++) {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text.get(i), x - Minecraft.getMinecraft().fontRenderer.getStringWidth(text.get(i)) / 2, y + 40 + 11 * i, 0xFFFFFF);
                }
            }
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/gui/icons.png"));
        GlStateManager.enableDepth();
    }

    private void addCapabilityInformation(List<String> text, TileEntity tile, EnumFacing facing) {
        addCapabilityItemDescription(text, tile, facing);
        addCapabilityFluidDescription(text, tile, facing);
        addCapabilityEmberDescription(text, tile, facing);
        if(ConfigManager.isMysticalMechanicsIntegrationEnabled())
            MysticalMechanicsIntegration.addCapabilityInformation(text,tile,facing);
        if (tile.hasCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY,facing))
            text.add(I18n.format("embers.tooltip.goggles.upgrade"));
        if (TileEntityMechAccessor.canAccess(tile))
            text.add(I18n.format("embers.tooltip.goggles.accessor_slot"));
        if (tile instanceof IMechanicallyPowered)
            text.add(I18n.format("embers.tooltip.goggles.actuator_slot"));
        if (tile instanceof IExtraCapabilityInformation)
            ((IExtraCapabilityInformation) tile).addOtherDescription(text, facing);
    }

    public static void addCapabilityItemDescription(List<String> text, TileEntity tile, EnumFacing facing) {
        Capability<IItemHandler> capability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        if(tile.hasCapability(capability,facing)) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            String filter = null;
            if(tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability,facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.item", filter));
            }
        }
    }

    public static void addCapabilityFluidDescription(List<String> text, TileEntity tile, EnumFacing facing) {
        Capability<IFluidHandler> capability = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
        if(tile.hasCapability(capability,facing)) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            String filter = null;
            if(tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability,facing);
            } else {
                IFluidHandler handler = tile.getCapability(capability, facing);
                for(IFluidTankProperties properties : handler.getTankProperties()) {
                    boolean input = properties.canFill();
                    boolean output = properties.canDrain();
                    if(!input && !output)
                        ioType = IExtraCapabilityInformation.EnumIOType.NONE;
                    else if(input && !output)
                        ioType = IExtraCapabilityInformation.EnumIOType.INPUT;
                    else if(output && !input)
                        ioType = IExtraCapabilityInformation.EnumIOType.OUTPUT;
                }
                text.add(IExtraCapabilityInformation.formatCapability(ioType,"embers.tooltip.goggles.fluid",filter));
            }

        }
    }

    public static void addCapabilityEmberDescription(List<String> text, TileEntity tile, EnumFacing facing) {
        Capability<IEmberCapability> capability = EmbersCapabilities.EMBER_CAPABILITY;
        if(tile.hasCapability(capability,facing)) {
            IExtraCapabilityInformation.EnumIOType ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
            if(tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability,facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.ember", null));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
            ticks++;
            ClientProxy.particleRenderer.updateParticles();

            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null) {
                World world = player.getEntityWorld();
                RayTraceResult result = player.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
                if (result != null) {
                    if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                        IBlockState state = world.getBlockState(result.getBlockPos());
                        if (state.getBlock() instanceof IDial) {
                            ((IDial) state.getBlock()).updateTEData(world, state, result.getBlockPos());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerRender(RenderPlayerEvent.Pre event) {
        if (event.getEntityPlayer() != null) {
            if (Minecraft.getMinecraft().inGameHasFocus || event.getEntityPlayer().getUniqueID().compareTo(Minecraft.getMinecraft().player.getUniqueID()) != 0) {
                event.setCanceled(!allowPlayerRenderEvent);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onEntityDamaged(LivingHurtEvent event) {
        if (event.getSource().damageType.equals(RegistryManager.damage_ember.damageType)) {
            if (event.getEntityLiving().isPotionActive(Potion.getPotionFromResourceLocation("fire_resistance"))) {
                event.setAmount(event.getAmount() * 0.5f);
            }
        }
        if (event.getSource().getTrueSource() != null) {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                if (((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getItem() == RegistryManager.tyrfing) {
                    if (!event.getEntity().world.isRemote) {
                        PacketHandler.INSTANCE.sendToAll(new MessageTyrfingBurstFX(event.getEntity().posX, event.getEntity().posY + event.getEntity().height / 2.0f, event.getEntity().posZ));
                    }
                    EntityPlayer p = ((EntityPlayer) event.getSource().getTrueSource());
                    event.setAmount((event.getAmount() / 4.0f) * (4.0f + (float) event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue() * 1.0f));
                }
                if (!((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().isEmpty()) {
                    if (((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getItem() instanceof IEmberChargedTool) {
                        if (((IEmberChargedTool) ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getItem()).hasEmber(((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand()) || ((EntityPlayer) event.getSource().getTrueSource()).capabilities.isCreativeMode) {
                            event.getEntityLiving().setFire(1);
                            if (!event.getEntityLiving().getEntityWorld().isRemote) {
                                PacketHandler.INSTANCE.sendToAll(new MessageEmberBurstFX(event.getEntityLiving().posX, event.getEntityLiving().posY + event.getEntityLiving().getEyeHeight() / 1.5, event.getEntityLiving().posZ));
                                ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getTagCompound().setBoolean("didUse", true);
                            }
                        } else {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (!player.getHeldItemMainhand().isEmpty()) {
                ItemStack s = player.getHeldItemMainhand();
                if (!s.isEmpty() && event.getState().getBlockHardness(event.getWorld(), event.getPos()) > 0) {
                    addHeat(player, s, 1.0f);
                }
                /*if (event.getPlayer().getHeldItemMainhand().getItem() instanceof IEmberChargedTool){
					PacketHandler.INSTANCE.sendToAll(new MessageEmberBurstFX(event.getPos().getX()+0.5,event.getPos().getY()+0.5,event.getPos().getZ()+0.5));
				}*/
                if (player.getHeldItemMainhand().getItem() instanceof ItemGrandhammer) {
                    event.setCanceled(true);
                    event.getWorld().setBlockToAir(event.getPos());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onProjectileFired(EmberProjectileEvent event) {
        EntityLivingBase shooter = event.getShooter();
        ItemStack weapon = event.getStack();
        if (!weapon.isEmpty()) {
            addHeat(shooter, weapon, event.getProjectiles().size() * (float) MathHelper.clampedLerp(0.5, 3.0, event.getCharge()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onArrowLoose(ArrowLooseEvent event) {
        EntityLivingBase shooter = event.getEntityLiving();
        ItemStack weapon = event.getBow();
        if (!weapon.isEmpty()) {
            addHeat(shooter, weapon, 1.0f);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (ItemModUtil.hasHeat(event.getItemStack())) {
            event.getToolTip().add("");
            if (ItemModUtil.hasHeat(event.getItemStack())) {
                if (ItemModUtil.getLevel(event.getItemStack()) > 0) {
                    event.getToolTip().add("");
                }
            }
            event.getToolTip().add("                        ");
            if (ItemModUtil.hasHeat(event.getItemStack())) {
                List<ModifierBase> modifiers = ItemModUtil.getModifiers(event.getItemStack());
                long count = modifiers.stream().filter(x -> x.shouldRenderTooltip).count();
                if (count > 0) {
                    event.getToolTip().add(TextFormatting.GRAY + I18n.format("embers.tooltip.modifiers"));
                    for (int i = 0; i < count; i++) {
                        event.getToolTip().add("");
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltipRender(RenderTooltipEvent.PostText event) {
        ItemStack stack = event.getStack();
        if (stack != null) {
            if (ItemModUtil.hasHeat(stack)) {
                for (int i = 0; i < event.getLines().size(); i++) {
                    if (event.getLines().get(i).compareTo(TextFormatting.GRAY + "" + TextFormatting.GRAY + I18n.format("embers.tooltip.modifiers")) == 0) {
                        List<ModifierBase> modifiers = ItemModUtil.getModifiers(stack).stream().filter(x -> x.shouldRenderTooltip).collect(Collectors.toList());
                        GlStateManager.disableDepth();
                        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
                        if (modifiers.size() > 0) {
                            GlStateManager.enableBlend();
                            GlStateManager.enableAlpha();
                            int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
                            float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
                            GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
                            int j = 0;
                            for (ModifierBase modifier : modifiers) {
                                int level = ItemModUtil.getModifierLevel(stack, modifier);
                                GuiCodex.drawTextGlowingAura(event.getFontRenderer(), I18n.format("embers.tooltip.modifier." + modifier.name, getFormattedModifierLevel(level)), event.getX(), event.getY() + (event.getFontRenderer().FONT_HEIGHT + 1) * (i + j + 1) + 2);
                                j++;
                            }
                            GlStateManager.alphaFunc(func, ref);
                            GlStateManager.disableAlpha();
                            GlStateManager.disableBlend();
                        }
                        GlStateManager.enableDepth();
                    }
                    if (event.getLines().get(i).compareTo(TextFormatting.GRAY + "                        ") == 0) {
                        GlStateManager.disableDepth();
                        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
                        if (ItemModUtil.getLevel(stack) > 0) {
                            event.getFontRenderer().drawStringWithShadow(TextFormatting.GRAY + I18n.format("embers.tooltip.heat_level"), event.getX(), event.getY() + (event.getFontRenderer().FONT_HEIGHT + 1) * (i - 1) + 2, 0xFFFFFFFF);
                            int level_x = (int) event.getFontRenderer().getStringWidth(I18n.format("embers.tooltip.heat_level")) + 2;
                            GlStateManager.enableBlend();
                            GlStateManager.enableAlpha();
                            int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
                            float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
                            GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
                            GuiCodex.drawTextGlowingAura(event.getFontRenderer(), "" + ItemModUtil.getLevel(stack), event.getX() + level_x, event.getY() + (event.getFontRenderer().FONT_HEIGHT + 1) * (i - 1) + 2);
                            GlStateManager.alphaFunc(func, ref);
                            GlStateManager.disableAlpha();
                            GlStateManager.disableBlend();
                        }
                        event.getFontRenderer().drawStringWithShadow(TextFormatting.GRAY + I18n.format("embers.tooltip.heat_amount"), event.getX(), event.getY() + (event.getFontRenderer().FONT_HEIGHT + 1) * i + 2, 0xFFFFFFFF);
                        double x = event.getFontRenderer().getStringWidth(I18n.format("embers.tooltip.heat_amount")) + 1.0;
                        double w = event.getFontRenderer().getStringWidth("                        ");
                        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("embers:textures/gui/heat_bar.png"));
                        Tessellator tess = Tessellator.getInstance();
                        BufferBuilder b = tess.getBuffer();
                        GlStateManager.disableTexture2D();
                        GlStateManager.enableAlpha();
                        int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
                        float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
                        GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
                        GlStateManager.enableBlend();
                        double baseX = event.getX();
                        double baseY = event.getY() + (event.getFontRenderer().FONT_HEIGHT + 1) * i + 2;
                        GlStateManager.shadeModel(GL11.GL_SMOOTH);
                        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                        double x1 = baseX + x + 4;
                        double x2 = baseX + w - 3;
                        float heat = ItemModUtil.getHeat(stack);
                        float maxHeat = ItemModUtil.getMaxHeat(stack);
                        x2 = x1 + (x2 - x1) * (heat / maxHeat);
                        for (double j = 0; j < 10; j++) {
                            double coeff = j / 10.0;
                            double coeff2 = (j + 1.0) / 10.0;
                            for (double k = 0; k < 4; k += 0.5) {
                                float thick = (float) (k / 4.0) * (heat >= maxHeat ? (float) Math.sin(ticks * 0.5) * 2 + 3 : 1);
                                RenderUtil.drawColorRectBatched(b, x1 * (1.0 - coeff) + x2 * (coeff), baseY + k, 0, ((x2 - x1) / 10.0), 8.0 - 2.0 * k,
                                        1.0f, 0.25f, 0.0625f, Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff) + x2 * (coeff))), 4 * (int) (baseY + k))),
                                        1.0f, 0.25f, 0.0625f, Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff2) + x2 * (coeff2))), 4 * (int) (baseY + k))),
                                        1.0f, 0.25f, 0.0625f, Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff2) + x2 * (coeff2))), 4 * (int) (baseY + (8.0 - k)))),
                                        1.0f, 0.25f, 0.0625f, Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff) + x2 * (coeff))), 4 * (int) (baseY + (8.0 - k)))));
                            }
                        }
                        x1 = baseX + x + 4;
                        x2 = baseX + w - 3;
                        double point = x1 + (x2 - x1) * (heat / maxHeat);

                        for (double k = 0; k < 4; k += 0.5) {
                            float thick = (float) (k / 4.0);
                            RenderUtil.drawColorRectBatched(b, point, baseY + k, 0, Math.min((x2 - point), ((x2 - x1) / 10.0)), 8.0 - 2.0 * k,
                                    1.0f, 0.25f, 0.0625f, 1.0f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (point)), 4 * (int) (baseY + k))),
                                    0.25f, 0.0625f, 0.015625f, 0.0f,
                                    0.25f, 0.0625f, 0.015625f, 0.0f,
                                    1.0f, 0.25f, 0.0625f, 1.0f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (point)), 4 * (int) (baseY + (8.0 - k)))));
                        }
                        tess.draw();
                        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                        x1 = baseX + x + 4;
                        x2 = baseX + w - 3;
                        x1 = x2 - (x2 - x1) * (1.0f - (heat / maxHeat));
                        for (double j = 0; j < 10; j++) {
                            double coeff = j / 10.0;
                            double coeff2 = (j + 1.0) / 10.0;
                            for (double k = 0; k < 4; k += 0.5) {
                                float thick = (float) (k / 4.0);
                                RenderUtil.drawColorRectBatched(b, x1 * (1.0 - coeff) + x2 * (coeff), baseY + k, 0, ((x2 - x1) / 10.0), 8.0 - 2.0 * k,
                                        0.25f, 0.0625f, 0.015625f, 0.75f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff) + x2 * (coeff))), 4 * (int) (baseY + k))),
                                        0.25f, 0.0625f, 0.015625f, 0.75f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff2) + x2 * (coeff2))), 4 * (int) (baseY + k))),
                                        0.25f, 0.0625f, 0.015625f, 0.75f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff2) + x2 * (coeff2))), 4 * (int) (baseY + (8.0 - k)))),
                                        0.25f, 0.0625f, 0.015625f, 0.75f * Math.min(1.0f, thick * 0.25f + thick * EmberGenUtil.getEmberDensity(6, (int) (ticks * 12 + 4 * (x1 * (1.0 - coeff) + x2 * (coeff))), 4 * (int) (baseY + (8.0 - k)))));
                            }
                        }
                        tess.draw();
                        GlStateManager.shadeModel(GL11.GL_FLAT);
                        GlStateManager.enableTexture2D();
                        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                        GlStateManager.alphaFunc(func, ref);
                        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                        RenderUtil.drawTexturedModalRectBatched(b, (int) (baseX + x + 1), (int) baseY - 1, 0, 0, 0, 0.5, 0.625, 8, 10);
                        RenderUtil.drawTexturedModalRectBatched(b, (int) (baseX + w - 8), (int) baseY - 1, 0, 0.5, 0, 1.0, 0.625, 8, 10);
                        //Gui.drawRect(event.getX()+x+1, event.getY()+(event.getFontRenderer().FONT_HEIGHT+1)*i+2, event.getX()+w, event.getY()+(event.getFontRenderer().FONT_HEIGHT+1)*i+event.getFontRenderer().FONT_HEIGHT, (0xFF << 24)+Misc.intColor(255/2, 32+(int)(32*sine), 8));
                        //Gui.drawRect(event.getX()+x, event.getY()+(event.getFontRenderer().FONT_HEIGHT+1)*i+1, event.getX()+w-1, event.getY()+(event.getFontRenderer().FONT_HEIGHT+1)*i+event.getFontRenderer().FONT_HEIGHT-1, (0xFF << 24)+Misc.intColor(255, 64+(int)(64*sine), 16));
                        tess.draw();
                        GlStateManager.disableBlend();
                        GlStateManager.disableAlpha();
                        GlStateManager.enableDepth();
                    }
                }
            }
        }
    }

    private String getFormattedModifierLevel(int level) {
        String key = "embers.tooltip.num" + level;
        if (I18n.hasKey(key))
            return I18n.format(key);
        else
            return I18n.format("embers.tooltip.numstop");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderAfterWorld(RenderWorldLastEvent event) {
        tickCounter++;
        if (Embers.proxy instanceof ClientProxy) {
            GlStateManager.pushMatrix();
            ClientProxy.particleRenderer.renderParticles(clientPlayer, event.getPartialTicks());
            GlStateManager.popMatrix();
        }
        List<TileEntity> list = Minecraft.getMinecraft().world.loadedTileEntityList;
        GlStateManager.pushMatrix();
        for (int i = 0; i < list.size(); i++) {
            TileEntitySpecialRenderer render = TileEntityRendererDispatcher.instance.getRenderer(list.get(i));
            if (render instanceof ITileEntitySpecialRendererLater) {
                double x = Minecraft.getMinecraft().player.lastTickPosX + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.lastTickPosX);
                double y = Minecraft.getMinecraft().player.lastTickPosY + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posY - Minecraft.getMinecraft().player.lastTickPosY);
                double z = Minecraft.getMinecraft().player.lastTickPosZ + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.lastTickPosZ);
                ((ITileEntitySpecialRendererLater) render).renderLater(list.get(i), list.get(i).getPos().getX() - x, list.get(i).getPos().getY() - y, list.get(i).getPos().getZ() - z, Minecraft.getMinecraft().getRenderPartialTicks());
            }
        }
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onBlockBreak(BreakSpeed event) {
        event.getOriginalSpeed();
    }

    @SideOnly(Side.CLIENT)
    public static void drawScaledCustomSizeModalRect(double x, double y, float u, float v, float uWidth, float vHeight, double width, double height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuffer();
        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        BufferBuilder.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) uWidth) * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        BufferBuilder.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) (v * f1)).endVertex();
        BufferBuilder.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
		/*if (!event.world.isRemote && event.phase == TickEvent.Phase.END){
			NBTTagList list = new NBTTagList();
			acceptUpdates = false;
			TileEntity[] updateArray = toUpdate.values().toArray(new TileEntity[0]);
			acceptUpdates = true;
			for (Entry<BlockPos, TileEntity> e : overflow.entrySet()){
				toUpdate.put(e.getKey(), e.getValue());
			}
			overflow.clear();
			for (int i = 0; i < updateArray.length; i ++){
				TileEntity t = updateArray[i];
				list.appendTag(t.getUpdateTag());
			}
			if (!list.hasNoTags()){
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag("data", list);
				PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(tag));
			}
			toUpdate.clear();
		}*/
    }

    static HashSet<String> removedItems = Sets.newHashSet("embers:advanced_edge", "embers:inferno_forge_edge", "embers:mech_edge", "embers:glow", "embers:structure_marker");

    @SubscribeEvent
    public void missingItemMappings(RegistryEvent.MissingMappings<Item> event) { //Thanks to KnightMiner for linking the relevant code for me to copy
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            String path = entry.key.toString();
            if (removedItems.contains(path)) {
                entry.ignore();
            }
        }
    }
}
