package net.pinger.disguise.skin;

import java.util.List;

public interface SkinPack extends Iterable<Skin> {

    /**
     * Returns the category of this skin pack.
     * <p>
     * For example, the category for Naruto skin pack is <b>Anime</b>.
     *
     * @return the category for this skin pack
     */

    String getCategory();

    /**
     * Returns the name of this skin pack.
     *
     * @return the name
     */

    String getName();

    /**
     * Returns all skins inside this skin pack.
     *
     * @return the list of skins
     */

    List<Skin> getSkins();


}
