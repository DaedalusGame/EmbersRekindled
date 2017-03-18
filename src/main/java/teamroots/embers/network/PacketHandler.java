package teamroots.embers.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import teamroots.embers.Embers;
import teamroots.embers.network.message.*;

public class PacketHandler {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Embers.MODID);

    private static int id = 0;

    public static void registerMessages(){
        INSTANCE.registerMessage(MessageTEUpdate.MessageHolder.class,MessageTEUpdate.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageCannonBeamFX.MessageHolder.class,MessageCannonBeamFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberBurstFX.MessageHolder.class,MessageEmberBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageItemUpdate.MessageHolder.class,MessageItemUpdate.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberSparkleFX.MessageHolder.class,MessageEmberSparkleFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberSphereFX.MessageHolder.class,MessageEmberSphereFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageBeamCannonFX.MessageHolder.class,MessageBeamCannonFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTEUpdateRequest.MessageHolder.class,MessageTEUpdateRequest.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageStamperFX.MessageHolder.class,MessageStamperFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageAnvilSparksFX.MessageHolder.class,MessageAnvilSparksFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTyrfingBurstFX.MessageHolder.class,MessageTyrfingBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberActivationFX.MessageHolder.class,MessageEmberActivationFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberGenOffset.MessageHolder.class,MessageEmberGenOffset.class,id ++,Side.CLIENT);
    }
}
