package teamroots.embers.power;

import teamroots.embers.entity.EntityEmberPacket;

public interface IEmberPacketReceiver {
	public boolean isFull();
	public boolean onReceive(EntityEmberPacket packet);
}
