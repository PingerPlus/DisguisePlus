package net.pinger.disguiseplus;

import net.pinger.disguiseplus.user.UserManager;

public interface Disguise {

    SkinFactory getSkinFactory();

    //

    DisguiseManager getManager();

    //

    UserManager getUserManager();

    /**
     * This method returns the object which
     * validates player id's.
     *
     * @return the name validator
     */

    PlayerMatcher getPlayerMatcher();
}
