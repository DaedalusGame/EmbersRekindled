package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.embers.Embers;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityMechCore;
import teamroots.embers.tileentity.TileEntitySeed;

public class BlockSeed extends BlockBase implements ITileEntityProvider, IModeledBlock {
	public static final PropertyInteger type = PropertyInteger.create("type", 0, 4);
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.9375,0.6875);
	public Item itemBlock = null;
	public boolean isOpaqueCube = true, isFullCube = true;
	public BlockRenderLayer layer = BlockRenderLayer.SOLID;
	public BlockSeed(Material material, String name, boolean addToTab){
		super(material, name, addToTab);
		itemBlock = ((new ItemBlock(this){
        	@Override
        	public int getMetadata(int meta){
        		return meta;
        	}
        	
        	@Override
        	public String getUnlocalizedName(ItemStack stack){
        		return super.getUnlocalizedName()+"."+stack.getItemDamage();
        	}
        }.setHasSubtypes(true)).setRegistryName(this.getRegistryName()));
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
		return AABB_BASE;
	}
	
	@Override
	public int damageDropped(IBlockState state){
		return state.getValue(type);
	}
	
	public BlockSeed setIsOpaqueCube(boolean b){
		isOpaqueCube = b;
		return this;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return isOpaqueCube;
	}
	
	public BlockSeed setIsFullCube(boolean b){
		isFullCube = b;
		return this;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return isFullCube;
	}
	
	public BlockSeed setHarvestProperties(String toolType, int level){
		super.setHarvestLevel(toolType, level);
		return this;
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, type);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
		list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
		list.add(new ItemStack(this,1,3));
		list.add(new ItemStack(this,1,4));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		return ((ITileEntityBase)world.getTileEntity(pos)).activate(world,pos,state,player,hand,side,hitX,hitY,hitZ);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,player);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(type);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(type,meta);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySeed().setMaterial(meta);
	}
	
	@Override
	public void initModel(){
		ModelBakery.registerItemVariants(this.itemBlock, new ResourceLocation(Embers.MODID+":seed_iron")
				, new ResourceLocation(Embers.MODID+":seed_gold")
				, new ResourceLocation(Embers.MODID+":seed_copper")
				, new ResourceLocation(Embers.MODID+":seed_lead")
				, new ResourceLocation(Embers.MODID+":seed_silver"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Embers.MODID+":seed_iron","inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 1, new ModelResourceLocation(Embers.MODID+":seed_gold","inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 2, new ModelResourceLocation(Embers.MODID+":seed_copper","inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 3, new ModelResourceLocation(Embers.MODID+":seed_lead","inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 4, new ModelResourceLocation(Embers.MODID+":seed_silver","inventory"));
	}
}
