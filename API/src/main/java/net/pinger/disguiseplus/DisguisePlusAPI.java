package net.pinger.disguiseplus;

import net.pinger.disguiseplus.skin.SkinFactory;

public class DisguisePlusAPI {
    private static Disguise disguise;

    /**
     * Cancel initialization of this class
     */

    private DisguisePlusAPI() {}

    /**
     * Sets the instance of the plugin that will be responsible
     * for handling the api methods.
     *
     * @param disguise the plugin
     */

    public static void setDisguise(Disguise disguise) {
        if (DisguisePlusAPI.disguise != null) {
            throw new IllegalArgumentException("Disguise has already been initialized");
        }

        DisguisePlusAPI.disguise = disguise;
    }

    /**
     * Returns the holder of all skins.
     *
     * @return the skin factory
     */

    public static SkinFactory getSkinFactory() {
        return disguise.getSkinFactory();
    }

    /**
     * This method returns the {@link FeatureManager} instance
     * used for saving different features.
     *
     * @return the features
     */

    public static FeatureManager getFeatureManager() {
        return disguise.getFeatureManager();
    }

}
