package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.EventManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.Misc;

public class BlockEmberGauge extends BlockBase implements IDial {
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockEmberGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab); 
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(facing).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(facing,EnumFacing.getFront(meta));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(facing, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		if (world.isAirBlock(pos.offset(state.getValue(facing),-1))){
			world.setBlockToAir(pos);
			this.dropBlockAsItem(world, pos, state, 0);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch (state.getValue(facing)){
		case UP:
			return new AxisAlignedBB(0.3125,0,0.3125,0.6875,0.125,0.6875);
		case DOWN:
			return new AxisAlignedBB(0.3125,0.875,0.3125,0.6875,1.0,0.6875);
		case NORTH:
			return new AxisAlignedBB(0.3125,0.3125,0.875,0.6875,0.6875,1.0);
		case SOUTH:
			return new AxisAlignedBB(0.3125,0.3125,0,0.6875,0.6875,0.125);
		case WEST:
			return new AxisAlignedBB(0.875,0.3125,0.3125,1.0,0.6875,0.6875);
		case EAST:
			return new AxisAlignedBB(0.0,0.3125,0.3125,0.125,0.6875,0.6875);
		}
		return new AxisAlignedBB(0.25,0,0.25,0.75,0.125,0.75);
	}

	@Override
	public List<String> getDisplayInfo(World world, BlockPos pos, IBlockState state) {
		ArrayList<String> text = new ArrayList<String>();
		if (world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))) != null){
			if (world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))).hasCapability(EmberCapabilityProvider.emberCapability, state.getValue(facing))){
				IEmberCapability handler = world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))).getCapability(EmberCapabilityProvider.emberCapability, state.getValue(facing));
				if (handler != null){
					text.add(I18n.format("embers.tooltip.emberdial.ember")+handler.getEmber()+"/"+handler.getEmberCapacity());
				}
			}
		}
		return text;
	}

	@Override
	public void updateTEData(World world, IBlockState state, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(this.facing))));
		if (tile != null){
			PacketHandler.INSTANCE.sendToServer(new MessageTEUpdateRequest(Minecraft.getMinecraft().player.getUniqueID(),pos));
		}
	}
}
