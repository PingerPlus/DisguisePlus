package net.pinger.disguise.fetcher;

import com.google.gson.JsonObject;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.http.request.HttpPostRequest;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.fetcher.NickFetcher;
import net.pinger.disguise.Skin;
import net.pinger.disguise.skin.SkinQueue;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.ReferenceUtil;
import net.pinger.disguise.utils.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * This class is responsible for catching skins using HTTP utils.
 *
 * @author Pinger
 * @since 2.0
 */

public class SkinFetcher {

    private static final Logger logger = LoggerFactory.getLogger("SkinFetcher");

    public static void catchSkin(String url, Consumer<Skin> skin, Consumer<Throwable> error) {
        Skin queued = SkinQueue.getSkinFromUrl(url);

        if (queued != null) {
            skin.accept(queued);
            return;
        }

        // Retrieve the skin otherwise
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = new HttpPostRequest(HttpUtil.toMineskin(url));
                HttpResponse response = request.connect();

                JsonObject property = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class);

                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(DisguisePlus.class), () -> {
                    if (property.has("error")) {
                        error.accept(new InvalidUrlException(property.get("error").getAsString()));
                        return;
                    }

                    JsonObject textured = property.getAsJsonObject("data").getAsJsonObject("texture");

                    if (textured == null) {
                        error.accept(new InvalidUrlException("invalid"));
                        return;
                    }

                    Skin fs = SkinUtil.getSkinFromMineskin(textured);
                    SkinQueue.storeUrl(url, fs);

                    skin.accept(fs);
                });

            } catch (Exception e) {
                error.accept(e);
            }
        });
    }

    public static Skin getSkin(String playerName) {
        try {
            // The uuid
            UUID id = NickFetcher.retrieveUUID(playerName);

            if (id == null)
                return null;

            HttpRequest request = new HttpGetRequest(HttpUtil.toMojangUrl(id));
            HttpResponse response = request.connect();

            if (response.getCode() == 404)
                return null;

            JsonObject object = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class);

            if (object == null || object.isJsonNull())
                return null;

            return SkinUtil.getFromMojang(object);
        } catch (IOException e) {
            return null;
        }
    }

}
