package teamroots.embers.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageAnvilSparksFX;
import teamroots.embers.network.message.MessageBeamCannonFX;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.network.message.MessageEmberGenOffset;
import teamroots.embers.network.message.MessageEmberSizedBurstFX;
import teamroots.embers.network.message.MessageEmberSparkleFX;
import teamroots.embers.network.message.MessageEmberSphereFX;
import teamroots.embers.network.message.MessageFlameShieldFX;
import teamroots.embers.network.message.MessageItemUpdate;
import teamroots.embers.network.message.MessagePlayerJetFX;
import teamroots.embers.network.message.MessageRemovePlayerEmber;
import teamroots.embers.network.message.MessageSetPlayerMotion;
import teamroots.embers.network.message.MessageCasterOrb;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.network.message.MessageTyrfingBurstFX;

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
        INSTANCE.registerMessage(MessageStamperFX.MessageHolder.class,MessageStamperFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageAnvilSparksFX.MessageHolder.class,MessageAnvilSparksFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTyrfingBurstFX.MessageHolder.class,MessageTyrfingBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberActivationFX.MessageHolder.class,MessageEmberActivationFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberGenOffset.MessageHolder.class,MessageEmberGenOffset.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageSuperheatFX.MessageHolder.class,MessageSuperheatFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessagePlayerJetFX.MessageHolder.class,MessagePlayerJetFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageFlameShieldFX.MessageHolder.class,MessageFlameShieldFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageEmberSizedBurstFX.MessageHolder.class,MessageEmberSizedBurstFX.class,id ++,Side.CLIENT);

        INSTANCE.registerMessage(MessageCasterOrb.MessageHolder.class,MessageCasterOrb.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageRemovePlayerEmber.MessageHolder.class,MessageRemovePlayerEmber.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageSetPlayerMotion.MessageHolder.class,MessageSetPlayerMotion.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageTEUpdateRequest.MessageHolder.class,MessageTEUpdateRequest.class,id ++,Side.SERVER);
    }
}
