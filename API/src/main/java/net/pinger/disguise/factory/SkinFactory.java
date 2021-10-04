package net.pinger.disguise.factory;

import net.pinger.disguise.skin.Skin;

public interface SkinFactory {

    /**
     * Returns all skins that were previously retrieved.
     * The place of retrieval may be the database or the load folder
     * in the base folder of the plugin,
     *
     * @return all skins that this plugin has loaded
     */

    Skin[] getSkins();

    Skin getRandomSkin();

}
