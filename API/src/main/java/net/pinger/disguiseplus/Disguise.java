package net.pinger.disguiseplus;

import net.pinger.disguiseplus.user.UserManager;

public interface Disguise {

    SkinFactory getSkinFactory();

    //

    DisguiseManager getManager();

    //

    UserManager getUserManager();
}
