package net.pinger.disguiseplus.skin.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.pinger.common.file.Reader;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.lang.Lists;
import net.pinger.disguiseplus.internal.SkinPackImpl;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.utils.HttpUtil;
import net.pinger.disguiseplus.utils.ReferenceUtil;
import net.pinger.disguiseplus.utils.SkinUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SkinPackLoader {

    public static SkinPack getSkinPack(String category, String name) throws IOException {
        List<Skin> skins = Lists.newArrayList();

        HttpRequest request = new HttpGetRequest(HttpUtil.toSkinPack(category, name));
        HttpResponse response = request.connect();

        // Getting the skins
        JsonArray array = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonArray.class);

        // Looping through each skin
        for (JsonElement object : array) {
            skins.add(SkinUtil.getFromJson(object.getAsJsonObject()));
        }

        return new SkinPackImpl(null, category, name, skins);
    }

    public static SkinPack getSkinPack(File f, String category, String name) {
        List<Skin> skins = Lists.newArrayList();

        // Edge case
        if (f == null || !f.exists() || f.length() <= 1)
            return null;

        JsonArray array = ReferenceUtil.GSON.fromJson(Reader.read(f), JsonArray.class);

        // Looping through each skin
        for (JsonElement el : array) {
            skins.add(SkinUtil.getFromJson(el.getAsJsonObject()));
        }

        return new SkinPackImpl(f.getParentFile(), category, name, skins);
    }


}
