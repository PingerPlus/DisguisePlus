package net.pinger.disguise.manager.skin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.http.request.HttpPostRequest;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.exceptions.InvalidUrlException;
import net.pinger.disguise.exceptions.InvalidUserException;
import net.pinger.disguise.manager.nick.NickFetcher;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skin.SkinQueue;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.ReferenceUtil;
import net.pinger.disguise.utils.SkinUtil;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Ref;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is responsible for catching skins using HTTP utils.
 *
 * @author Pinger
 * @since 2.0
 */

public class SkinFetcher {

    private static final LoadingCache<String, Skin> skinCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build(CacheLoader.from(SkinFetcher::catchSkin));

    private static final Logger logger = LoggerFactory.getLogger("SkinFetcher");

    public static void catchSkin(String url, Consumer<Skin> skin, DisguisePlus dp) throws InvalidUrlException {
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

                if (response.getCode() == 404)
                    throw new InvalidUrlException("The url of this request was invalid.");

                JsonObject property = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class)
                        .getAsJsonObject("data")
                        .getAsJsonObject("texture");

                Skin fs = SkinUtil.getSkinFromMineskin(property);
                SkinQueue.storeUrl(url, fs);

                Bukkit.getScheduler().runTask(dp, () -> {
                    skin.accept(fs);
                });

            } catch (IOException e) {
                logger.error("Failed to load a skin for the url -> " + url);
                e.printStackTrace();
            }
        });
    }

    private static Skin catchSkin(String playerName) {
        try {
            // The uuid
            UUID id = NickFetcher.retrieveUUID(playerName);

            if (id == null)
                return null;

            HttpRequest request = new HttpGetRequest(HttpUtil.toMojangUrl(id));
            HttpResponse response = request.connect();

            if (response.getCode() == 404)
                throw new InvalidUserException();

            JsonObject object = ReferenceUtil.GSON.fromJson(response.getResponse(), JsonObject.class);
            return SkinUtil.getFromMojang(object);
        } catch (IOException e) {
            logger.error("Failed to load a skin for name -> " + playerName);
            logger.error(e.getMessage());
        }

        return null;
    }

    public static Skin getSkin(String playerName) {
        try {
            return skinCache.get(playerName);
        } catch (ExecutionException e) {
            logger.error("Failed to retrieve a skin from the player name -> " + playerName);
            logger.error(e.getMessage());
        }

        return null;
    }

}
