package net.pinger.disguise;

import net.pinger.disguise.factory.SkinFactory;
import net.pinger.disguise.packet.PacketProvider;
import net.pinger.disguise.user.UserManager;

public interface Disguise {

    PacketProvider getPacketProvider();

    //

    SkinFactory getSkinFactory();

    //

    DisguiseManager getManager();

    //

    UserManager getUserManager();
}
