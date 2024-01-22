package net.pinger.disguiseplus;

import net.pinger.disguiseplus.rank.RankManager;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.user.UserManager;

public interface Disguise {

    SkinFactory getSkinFactory();

    DisguiseManager getManager();

    UserManager getUserManager();

    RankManager getRankManager();

    /**
     * This method returns the {@link FeatureManager} instance
     * used for saving different features.
     *
     * @return the features
     */

    FeatureManager getFeatureManager();
}
