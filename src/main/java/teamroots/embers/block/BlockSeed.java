package teamroots.embers.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntitySeed;

import java.util.Random;

@Deprecated
public class BlockSeed extends BlockBase implements ITileEntityProvider, IModeledBlock {
	public static final PropertyInteger type = PropertyInteger.create("type", 0, 4);
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.9375,0.6875);
	public boolean isOpaqueCube = true, isFullCube = true;
	public BlockRenderLayer layer = BlockRenderLayer.SOLID;
	public BlockSeed(Material material, String name, boolean addToTab){
		super(material, name, addToTab);
		itemBlock = new ItemBlock(this){
        	@Override
        	public int getMetadata(int meta){
        		return meta;
        	}
        	
        	@Override
        	public String getUnlocalizedName(ItemStack stack){
        		return super.getUnlocalizedName()+"."+stack.getItemDamage();
        	}
        }.setHasSubtypes(true).setRegistryName(this.getRegistryName());
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
		return AABB_BASE;
	}

	@Override
	public boolean canSilkHarvest(World p_canSilkHarvest_1_, BlockPos p_canSilkHarvest_2_, IBlockState p_canSilkHarvest_3_, EntityPlayer p_canSilkHarvest_4_) {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		if(state.getBlock() == this) //I swear to god this will trip at least once in the lifetime of this mod.
			switch(state.getValue(type))
			{
				case(0): return Item.getItemFromBlock(RegistryManager.seed_iron);
				case(1): return Item.getItemFromBlock(RegistryManager.seed_gold);
				case(2): return Item.getItemFromBlock(RegistryManager.seed_copper);
				case(3): return Item.getItemFromBlock(RegistryManager.seed_lead);
				case(4): return Item.getItemFromBlock(RegistryManager.seed_silver);
				default: return Item.getItemFromBlock(RegistryManager.seed_iron);
			}

		return super.getItemDropped(state, random, fortune);
	}

	@Override
	public int damageDropped(IBlockState state){
		return 0;
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
		/*list.add(new ItemStack(this,1,0));
		list.add(new ItemStack(this,1,1));
		list.add(new ItemStack(this,1,2));
		list.add(new ItemStack(this,1,3));
		list.add(new ItemStack(this,1,4));*/
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
