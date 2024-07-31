package net.pinger.disguiseplus;

import net.pinger.disguiseplus.skin.SkinFactory;

public interface Disguise {

    SkinFactory getSkinFactory();

    /**
     * This method returns the {@link FeatureManager} instance
     * used for saving different features.
     *
     * @return the features
     */

    FeatureManager getFeatureManager();
}
