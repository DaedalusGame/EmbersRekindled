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
        INSTANCE.registerMessage(MessageCookItemFX.MessageHolder.class,MessageCookItemFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageResonatingBellFX.MessageHolder.class,MessageResonatingBellFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageMetallurgicDustFX.MessageHolder.class,MessageMetallurgicDustFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageExplosionCharmFX.MessageHolder.class,MessageExplosionCharmFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageAshenAmuletFX.MessageHolder.class,MessageAshenAmuletFX.class,id ++,Side.CLIENT);

        INSTANCE.registerMessage(MessageCasterOrb.MessageHolder.class,MessageCasterOrb.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageSetPlayerMotion.MessageHolder.class,MessageSetPlayerMotion.class,id ++,Side.SERVER);
        INSTANCE.registerMessage(MessageTEUpdateRequest.MessageHolder.class,MessageTEUpdateRequest.class,id ++,Side.SERVER);

        INSTANCE.registerMessage(MessageResearchData.MessageHolder.class,MessageResearchData.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageResearchTick.MessageHolder.class,MessageResearchTick.class,id ++,Side.SERVER);
    }
}
