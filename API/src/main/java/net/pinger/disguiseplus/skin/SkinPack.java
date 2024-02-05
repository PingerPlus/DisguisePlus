package net.pinger.disguiseplus.skin;

import com.google.gson.JsonArray;
import java.nio.file.Path;
import net.pinger.disguise.skin.Skin;

import java.util.List;

/**
 * This class represents a holder for a certain amount of skins.
 * <p>
 * Each skin pack essentially belongs to a
 * specific category under the {@link SkinFactory} contract.
 *
 * @since 2.0
 */

public interface SkinPack extends Iterable<Skin>, Comparable<SkinPack> {

    default boolean hasSkins() {
        return !this.getSkins().isEmpty();
    }

    void addSkin(Skin skin);

    void removeSkin(Skin skin);

    boolean isCustomSkinPack();

    Path getFile();

    void setFile(Path file);

    /**
     * This method returns the unique name of this
     * skin pack.
     * <p>
     * This name must be unique for each skin pack under
     * the same category.
     *
     * @return the name of the skin pack
     */

    String getName();

    /**
     * This method returns the category
     * this skin pack is under.
     *
     * @return the category
     */

    String getCategory();

    /**
     * This method returns all skin packs
     * cached inside this type.
     *
     * @return the skins
     */

    List<Skin> getSkins();

    JsonArray toJsonArray();
}
