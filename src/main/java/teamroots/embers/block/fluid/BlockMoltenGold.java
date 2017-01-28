package teamroots.embers.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.IModeledBlock;

public class BlockMoltenGold extends BlockFluidClassic implements IModeledBlock {
	public static FluidStack stack = new FluidStack(RegistryManager.fluid_molten_gold,1000);
	
	public BlockMoltenGold(String name, boolean addToTab) {
		super(RegistryManager.fluid_molten_gold,Material.LAVA);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			this.setCreativeTab(Embers.tab);
		}
		this.setQuantaPerBlock(6);
		RegistryManager.fluid_molten_gold.setBlock(this);
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
        Block block = RegistryManager.block_molten_gold;
        Item item = Item.getItemFromBlock(block);   

        ModelBakery.registerItemVariants(item);
        
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Embers.MODID + ":fluid", stack.getFluid().getName());

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);

        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
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
