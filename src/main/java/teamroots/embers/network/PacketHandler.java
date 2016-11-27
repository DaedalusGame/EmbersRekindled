package teamroots.embers.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.network.message.MessageEmberDataRequest;
import teamroots.embers.network.message.MessageEmberGeneration;
import teamroots.embers.network.message.MessageItemUpdate;
import teamroots.embers.network.message.MessageTEUpdate;

public class PacketHandler {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Embers.MODID);

    private static int id = 0;

    public static void registerMessages(){
        INSTANCE.registerMessage(MessageEmberGeneration.MessageHolder.class,MessageEmberGeneration.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberData.MessageHolder.class,MessageEmberData.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberDataRequest.MessageHolder.class,MessageEmberDataRequest.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageTEUpdate.MessageHolder.class,MessageTEUpdate.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageCannonBeamFX.MessageHolder.class,MessageCannonBeamFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberBurstFX.MessageHolder.class,MessageEmberBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageItemUpdate.MessageHolder.class,MessageItemUpdate.class,id ++,Side.CLIENT);
       }
}
