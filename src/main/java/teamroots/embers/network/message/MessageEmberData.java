package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.EventManager;
import teamroots.embers.world.EmberWorldData;

public class MessageEmberData implements IMessage {
	int chunkCoordX = 0, chunkCoordZ = 0;
	double ember = 0;
	
	public MessageEmberData(){
		//
	}
	
	public MessageEmberData(int chunkX, int chunkZ, double ember){
		this.chunkCoordX = chunkX;
		this.chunkCoordZ = chunkZ;
		this.ember = ember;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		chunkCoordX = buf.readInt();
		chunkCoordZ = buf.readInt();
		ember = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chunkCoordX);
		buf.writeInt(chunkCoordZ);
		buf.writeDouble(ember);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberData,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberData message, final MessageContext ctx) {
    		EmberWorldData.get(Minecraft.getMinecraft().player.getEntityWorld()).setEmberForChunk(message.chunkCoordX, message.chunkCoordZ, message.ember);
    		EmberWorldData.get(Minecraft.getMinecraft().player.getEntityWorld()).markDirty();
    		EventManager.currentEmber = message.ember;
    		return null;
        }
    }

}
