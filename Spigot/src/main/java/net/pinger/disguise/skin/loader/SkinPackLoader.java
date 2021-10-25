package net.pinger.disguise.skin.loader;

import com.google.gson.*;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.skin.SimpleSkinPack;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skin.SkinPack;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.SkinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class SkinPackLoader {

    private static final Logger logger = LoggerFactory.getLogger("SkinPackLoader");

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();

    public static SkinPack getSkinPack(String category, String name) throws IOException {
        List<Skin> skins = Lists.newArrayList();

        HttpRequest request = new HttpGetRequest(HttpUtil.toSkinPack(category, name));
        HttpResponse response = request.connect();

        // Getting the skins
        JsonArray array = gson.fromJson(response.getResponse(), JsonArray.class);

        // Looping through each skin
        for (JsonElement object : array) {
            skins.add(SkinUtil.getFromJson(object.getAsJsonObject()));
        }

        return new SimpleSkinPack(category, name, skins);
    }



}
