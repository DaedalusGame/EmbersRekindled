package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.world.EmberWorldData;

public class MessageEmberData implements IMessage {
	NBTTagCompound tag = new NBTTagCompound();
	
	public MessageEmberData(){
		//
	}
	
	public MessageEmberData(EmberWorldData data){
		data.writeToNBT(tag);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberData,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberData message, final MessageContext ctx) {
    		EmberWorldData.get(Minecraft.getMinecraft().thePlayer.getEntityWorld()).readFromNBT(message.tag);
    		EmberWorldData.get(Minecraft.getMinecraft().thePlayer.getEntityWorld()).markDirty();
    		return null;
        }
    }

}
