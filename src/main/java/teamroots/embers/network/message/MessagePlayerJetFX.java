package teamroots.embers.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessagePlayerJetFX implements IMessage {
	public static Random random = new Random();
	public UUID id = null;
	
	public MessagePlayerJetFX(){
		super();
	}
	
	public MessagePlayerJetFX(UUID id){
		super();
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

    public static class MessageHolder implements IMessageHandler<MessagePlayerJetFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessagePlayerJetFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
    		EntityPlayer p = world.getPlayerEntityByUUID(message.id);
    		if (p != null){
    			for (int i = 0; i < 40; i ++){
    				ParticleUtil.spawnParticleSmoke(world, (float)p.posX-(float)p.motionX-(float)p.getLookVec().x*0.5f, (float)p.posY+p.height/4.0f, (float)p.posZ-(float)p.motionZ-(float)p.getLookVec().z*0.5f, -(float)p.motionX+0.25f*(random.nextFloat()-0.5f), -(float)p.motionY+0.25f*(random.nextFloat()-0.5f), -(float)p.motionZ+0.25f*(random.nextFloat()-0.5f), 80, 80, 80, 0.25f+0.25f*random.nextFloat(), 4.0f+random.nextFloat()*20.0f, 80);
    			}
    		}
    		return null;
        }
    }

}
