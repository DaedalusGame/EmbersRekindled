package teamroots.embers.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessageCannonBeamFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	double dX = 0, dY = 0, dZ = 0;
	public UUID id = null;
	
	public MessageCannonBeamFX(){
		super();
	}
	
	public MessageCannonBeamFX(UUID id, double x, double y, double z, double dX, double dY, double dZ){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.dX = dX;
		this.dY = dY;
		this.dZ = dZ;
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		dX = buf.readDouble();
		dY = buf.readDouble();
		dZ = buf.readDouble();
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(dX);
		buf.writeDouble(dY);
		buf.writeDouble(dZ);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

    public static class MessageHolder implements IMessageHandler<MessageCannonBeamFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageCannonBeamFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
    		boolean doContinue = true;
    		for (double i = 0; i < 384.0 && doContinue; i ++){
    			for (int j = 0; j < 5; j ++){
    				message.posX += 0.2*i*message.dX/384.0;
    				message.posY += 0.2*i*message.dY/384.0;
    				message.posZ += 0.2*i*message.dZ/384.0;
    				ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0, 0, 0, 255, 64, 16, 2.0f, 24);
    			}
    			IBlockState state = world.getBlockState(new BlockPos(message.posX,message.posY,message.posZ));
    			if (state.isFullCube() && state.isOpaqueCube()){
    				doContinue = false;
    				for (int k = 0; k < 80; k ++){
    					ParticleUtil.spawnParticleGlow(world, (float)message.posX-(float)(i*message.dX/384.0f), (float)message.posY-(float)(i*message.dY/384.0f), (float)message.posZ-(float)(i*message.dZ/384.0f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 24);
    				}
    			}
    			List<EntityLivingBase> rawEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(message.posX-0.85,message.posY-0.85,message.posZ-0.85,message.posX+0.85,message.posY+0.85,message.posZ+0.85));
    			ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
    			for (int j = 0; j < rawEntities.size(); j ++){
    				if (rawEntities.get(j).getUniqueID().compareTo(message.id) != 0){
    					entities.add(rawEntities.get(j));
    				}
    			}
    			if (entities.size() > 0){
    				doContinue = false;
    				for (int k = 0; k < 80; k ++){
    					ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 24);
    				}
    			}
    		}
    		return null;
        }
    }

}
