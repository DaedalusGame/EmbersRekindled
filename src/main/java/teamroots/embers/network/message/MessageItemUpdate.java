package teamroots.embers.network.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.world.EmberWorldData;

public class MessageItemUpdate implements IMessage {
	public NBTTagCompound tag = new NBTTagCompound();
	public int slot = 0;
	public UUID id = null;
	public MessageItemUpdate(){
		//
	}
	
	public MessageItemUpdate(UUID id, int slot, NBTTagCompound tag){
		this.tag = tag;
		this.id = id;
		this.slot = slot;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
		slot = buf.readInt();
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
		buf.writeInt(slot);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

    public static class MessageHolder implements IMessageHandler<MessageItemUpdate,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageItemUpdate message, final MessageContext ctx) {
    		Minecraft.getMinecraft().addScheduledTask(()-> {
	    		EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(message.id);
	    		if (player != null){
	    			player.inventory.getStackInSlot(message.slot).setTagCompound(message.tag);
	    		}
	    	});
    		return null;
	    }
    }
}
