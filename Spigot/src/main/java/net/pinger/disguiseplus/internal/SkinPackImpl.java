package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinPack;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class SkinPackImpl implements SkinPack {
    private final String category, name;
    private final List<Skin> skins;
    private final boolean custom;

    private Path base;

    public SkinPackImpl(Path base, String category, String name, List<Skin> skins, boolean custom) {
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
    public Path getFile() {
        return this.base;
    }

    @Override
    public void setFile(Path file) {
        if (this.base != null) {
            return;
        }

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
    public @NotNull Iterator<Skin> iterator() {
        return this.skins.iterator();
    }

    @Override
    public JsonArray toJsonArray() {
        final JsonArray array = new JsonArray();
        for (final Skin skin : this.skins) {
            final String json = DisguisePlus.GSON.toJson(skin, Skin.class);
            array.add(DisguiseAPI.GSON.fromJson(json, JsonObject.class));
        }

        return array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        SkinPackImpl skins = (SkinPackImpl) o;
        return Objects.equals(this.category, skins.category) && Objects.equals(this.name, skins.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.category, this.name);
    }

    @Override
    public int compareTo(@Nonnull SkinPack o) {
        return this.name.compareTo(o.getName());
    }
}
