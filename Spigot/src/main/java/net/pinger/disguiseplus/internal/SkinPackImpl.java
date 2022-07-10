package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.SkinPack;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SkinPackImpl implements SkinPack {

    private File base;
    private final String category, name;
    private final List<Skin> skins;
    private final boolean custom;

    public SkinPackImpl(File base, String category, String name, List<Skin> skins) {
        this(base, category, name, skins, true);
    }

    public SkinPackImpl(File base, String category, String name, List<Skin> skins, boolean custom) {
        this.base = base;
        this.category = category;
        this.name = name;
        this.skins = skins;
        this.custom = custom;
    }

    @Override
    public void addSkin(@Nonnull Skin skin) {
        this.skins.add(skin);
    }

    @Override
    public void removeSkin(Skin skin) {
        this.skins.remove(skin);
    }

    @Override
    public boolean isCustomSkinPack() {
        return this.custom;
    }

    @Override
    public File getFile() {
        return this.base;
    }

    @Override
    public void setFile(File file) {
        if (this.base != null)
            return;

        this.base = file;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Skin> getSkins() {
        return this.skins;
    }

    @Override
    public Iterator<Skin> iterator() {
        return this.skins.iterator();
    }

    @Override
    public JsonArray toJsonArray() {
        JsonArray array = new JsonArray();

        for (Skin s : this.skins) {
            // Add to the array
            array.add(s.toJsonObject());
        }

        return array;
    }

    @Override
    public boolean equals(Object o) {
        // Check if these objects are equal
        // In terms of the reference
        if (this == o)
            return true;

        // Check if the second object is null
        // Or the classes aren't equal
        if (o == null || getClass() != o.getClass())
            return false;

        // Cast the class to the implementation
        SkinPackImpl skins = (SkinPackImpl) o;

        // Compare categories and names
        return Objects.equals(category, skins.category) && Objects.equals(name, skins.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, name);
    }

    @Override
    public int compareTo(@Nonnull SkinPack o) {
        return this.name.compareTo(o.getName());
    }
}
