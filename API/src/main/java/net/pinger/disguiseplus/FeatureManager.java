package net.pinger.disguiseplus;

public interface FeatureManager {

    /**
     * This method registers the specified feature. If this method is called after
     * the previous features have already been loaded with {@link DisguiseFeature#load()},
     * then the user calling this method will have to manually call the method once,
     * after which the feature will again be accessible.
     *
     * @param features the features to register
     */

    void registerFeature(DisguiseFeature... features);

    /**
     * This method loads all features that are currently registered under this
     * feature manager.
     *
     * @see #registerFeature(DisguiseFeature...)
     * @see DisguiseFeature#load()
     * @see #reload()
     */

    void load();

    /**
     * This method reloads all features that are currently registered under this
     * feature manager.
     *
     * @see #registerFeature(DisguiseFeature...)
     * @see DisguiseFeature#reload()
     * @see #load()
     */

    void reload();

}
