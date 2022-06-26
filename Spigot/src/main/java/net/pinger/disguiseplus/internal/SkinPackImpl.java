package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.SkinPack;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SkinPackImpl implements SkinPack {

    private final File base;
    private final String category, name;
    private final List<Skin> skins;

    public SkinPackImpl(File base, String category, String name, List<Skin> skins) {
        this.base = base;
        this.category = category;
        this.name = name;
        this.skins = skins;
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
    public File getFile() {
        return this.base;
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
}
