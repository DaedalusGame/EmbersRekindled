package teamroots.embers.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.IBlockModel;
import teamroots.embers.util.MeshDefinitionFix;

public class BlockMoltenUmberSteel extends BlockFluidClassic implements IBlockModel {
	public static FluidStack stack = new FluidStack(RegistryManager.fluidMoltenUmberSteel,1000);
	
	public BlockMoltenUmberSteel(String name, boolean addToTab) {
		super(RegistryManager.fluidMoltenUmberSteel,Material.LAVA);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			this.setCreativeTab(Embers.tab);
		}
		this.setQuantaPerBlock(6);
		RegistryManager.fluidMoltenUmberSteel.setBlock(this);
		GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initModel(){
        Block block = RegistryManager.blockMoltenUmberSteel;
        Item item = Item.getItemFromBlock(block);   

        ModelBakery.registerItemVariants(item);
        
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Embers.MODID + ":fluid", stack.getFluid().getName());

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);

        ModelLoader.setCustomStateMapper((Block) block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
                return modelResourceLocation;
            }
        });
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(LEVEL, meta);
    }
}
