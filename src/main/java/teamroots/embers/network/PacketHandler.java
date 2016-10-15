package teamroots.embers.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.network.message.MessageEmberDataRequest;
import teamroots.embers.network.message.MessageEmberGeneration;
import teamroots.embers.network.message.MessageParticle;
import teamroots.embers.network.message.MessageTEUpdate;

public class PacketHandler {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Embers.MODID);

    private static int id = 0;

    public static void registerMessages(){
        INSTANCE.registerMessage(MessageEmberGeneration.MessageHolder.class,MessageEmberGeneration.class,0,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberData.MessageHolder.class,MessageEmberData.class,1,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberDataRequest.MessageHolder.class,MessageEmberDataRequest.class,2,Side.SERVER);
        INSTANCE.registerMessage(MessageTEUpdate.MessageHolder.class,MessageTEUpdate.class,3,Side.CLIENT);
    }
}
