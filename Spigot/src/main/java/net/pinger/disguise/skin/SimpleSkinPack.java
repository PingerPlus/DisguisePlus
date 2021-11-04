package net.pinger.disguise.skin;

import java.util.Iterator;
import java.util.List;

public class SimpleSkinPack implements SkinPack {

    private final String category, name;
    private final List<Skin> skins;

    public SimpleSkinPack(String category, String name, List<Skin> skins) {
        this.category = category;
        this.name = name;
        this.skins = skins;
    }

    /**
     * Returns the category of this skin pack.
     * <p>
     * For example, the category for Naruto skin pack is <b>Anime</b>.
     *
     * @return the category for this skin pack
     */

    @Override
    public String getCategory() {
        return this.category;
    }

    /**
     * Returns the name of this skin pack.
     *
     * @return the name
     */

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns all skins inside this skin pack.
     *
     * @return the list of skins
     */

    @Override
    public List<Skin> getSkins() {
        return this.skins;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */

    @Override
    public Iterator<Skin> iterator() {
        return this.skins.iterator();
    }
}