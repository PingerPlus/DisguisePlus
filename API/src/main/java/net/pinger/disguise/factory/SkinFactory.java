package net.pinger.disguise.factory;

import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skin.SkinPack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface SkinFactory {

    /**
     * Returns all skins that were previously retrieved.
     * The place of retrieval may be the database or the load folder
     * in the base folder of the plugin.
     *
     * @return all skins that this plugin has loaded
     */

    @Nonnull
    Skin[] getSkins();

    @Nonnull
    Skin getRandomSkin();

    @Nullable
    SkinPack getSkinPackByName(String name);

    @Nullable
    SkinPack getSkinPackByName(String category, String name);

    List<? extends SkinPack> getSkinPacks(String category);

    List<? extends SkinPack> getSkinPacks();
}
