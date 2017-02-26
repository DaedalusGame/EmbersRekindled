package teamroots.embers.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberGenUtil;

public class MessageEmberGenOffset implements IMessage {
	public static Random random = new Random();
	int offX = 0, offZ = 0;
	
	public MessageEmberGenOffset(){
		super();
	}
	
	public MessageEmberGenOffset(int x, int z){
		super();
		this.offX = x;
		this.offZ = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		offX = buf.readInt();
		offZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(offX);
		buf.writeInt(offZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberGenOffset,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberGenOffset message, final MessageContext ctx) {
    		Minecraft.getMinecraft().addScheduledTask(()-> {
    			EmberGenUtil.offX = message.offX;
    			EmberGenUtil.offZ = message.offZ;
	    	});
    		return null;
        }
    }

}
