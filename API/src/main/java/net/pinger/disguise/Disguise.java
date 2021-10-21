package net.pinger.disguise;

import net.pinger.disguise.factory.SkinFactory;
import net.pinger.disguise.packet.PacketProvider;

public interface Disguise {

    PacketProvider getPacketProvider();

    //

    SkinFactory getSkinFactory();

    //

    DisguiseManager getManager();
}
