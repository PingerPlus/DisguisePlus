package net.pinger.disguise.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pinger.common.file.Reader;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.lang.Lists;
import net.pinger.common.lang.Maps;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.exceptions.SkinCloudDownloadException;
import net.pinger.disguise.skin.SimpleSkinPack;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skin.SkinPack;
import net.pinger.disguise.skin.loader.SkinPackLoader;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.SkinUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleSkinFactory implements SkinFactory {

    private static final Logger logger = LoggerFactory.getLogger("SkinFactory");

    // The list of skins
    private final List<Skin> skins = Lists.newArrayList();
    private final List<SkinPack> skinPacks = Lists.newArrayList();

    // Categorized skins
    private final Map<String, List<SkinPack>> categorySkins = new TreeMap<>();

    // The disguise instance
    private final DisguisePlus dp;

    public SimpleSkinFactory(DisguisePlus dp) {
        this.dp = dp;

        // Initialize the loader
        this.retrieveFromCloud();
        this.retrieveSkinPacks();
    }

    private void retrieveFromCloud() {
        long time = System.currentTimeMillis();

        int size = this.skins.size();

        // Clear it every time
        this.skins.clear();

        File data = new File(this.dp.getDataFolder(), "data");
        if (!data.exists())
            data.mkdirs();

        // Create the load file
        File load = new File(data, "skins.json");

        try {
            HttpRequest request = new HttpGetRequest(HttpUtil.CLOUD_URL);
            HttpResponse response = request.connect();

                // Check if the response is valid
            if (response.getResponse().isEmpty()) {
                if (load.length() <= 1)
                    throw new SkinCloudDownloadException();
                else
                    this.retrieveFromFile(load);
            }

            JsonArray array = this.dp.getGson().fromJson(response.getResponse(), JsonArray.class);

            // Write to the backup file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(load))) {
                writer.write(this.dp.getGson().toJson(array));
            }
                // Saving it here
            for (JsonElement skin : array) {
                this.skins.add(SkinUtil.getFromJson(skin.getAsJsonObject()));
            }

            if (this.skins.size() > size) {
                logger.info("Successfully downloaded " + (this.skins.size() - size) + " skins from the cloud!");
                logger.info(String.format("Time: %sms", System.currentTimeMillis() - time));
            }

        } catch (Exception e) {
            logger.error("Failed to load skins from the cloud: ");
            logger.error(e.getMessage());
        }
    }

    private void retrieveSkinPacks() {
        long time = System.currentTimeMillis();
        this.skinPacks.clear();

        AtomicInteger atom = new AtomicInteger(0);

        try {
            // Establish the connection
            HttpRequest request = new HttpGetRequest(HttpUtil.CATEGORY_URL);
            HttpResponse response = request.connect();

            // This is essentially the categories.json file
            JsonObject object = this.dp.getGson().fromJson(response.getResponse(), JsonObject.class);

            for (Map.Entry<String, JsonElement> element : object.entrySet()) {
                for (JsonElement packName : element.getValue().getAsJsonArray()) {
                    // Add the pack tot he list
                    SkinPack pack = SkinPackLoader.getSkinPack(element.getKey(), packName.getAsString());
                    skinPacks.add(pack);

                    atom.addAndGet(pack.getSkins().size());
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load skin packs -> ");
            logger.error(e.getMessage());
            return;
        }

        logger.info(String.format("Successfully retrieved %s skins within %s skin packs.", atom.get(), skinPacks.size()));
        logger.info(String.format("Time: %sms", System.currentTimeMillis() - time));

        // Get the skins sorted throughout this category
        skinPacks.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        for (SkinPack skins : skinPacks) {
            if (this.categorySkins.containsKey(skins.getCategory()))
                this.categorySkins.get(skins.getCategory()).add(skins);
            else
                this.categorySkins.put(skins.getCategory(), Lists.newArrayList(skins));
        }
    }

    /**
     * Retrieves skins from the skins.json file.
     *
     * @param f the file
     */

    private void retrieveFromFile(File f) {
        // Loading the array
        JsonArray array = this.dp.getGson().fromJson(Reader.read(f), JsonArray.class);

        // Loading
        for (JsonElement element : array) {
            this.skins.add(SkinUtil.getFromJson(element.getAsJsonObject()));
        }

        // Logging
        logger.info("Successfully loaded " + skins.size() + " from the backup file!");
    }

    public void createCategory(String category) {
        this.categorySkins.putIfAbsent(category, Lists.newArrayList());
    }

    public void createSkinPack(String category, SkinPack pack) {
        this.skinPacks.add(pack);

        // Add to category
        this.categorySkins.get(category).add(pack);
    }

    public void addSkinToPack(SkinPack pack, Skin skin) {
        if (!(pack instanceof SimpleSkinPack))
            return;

        ((SimpleSkinPack) pack).addSkin(skin);
    }

    @Override
    public Skin[] getSkins() {
        return this.skins.toArray(new Skin[0]);
    }

    @Override
    public Skin getRandomSkin() {
        return this.skins.get(new Random().nextInt(this.skins.size()));
    }

    @Nullable
    @Override
    public SkinPack getSkinPackByName(String name) {
        for (SkinPack pack : this.getSkinPacks()) {
            if (pack.getName().equalsIgnoreCase(name))
                return pack;
        }

        return null;
    }

    @Override
    public List<? extends SkinPack> getSkinPacks(String category) {
        return this.categorySkins.getOrDefault(category, Lists.newArrayList());
    }

    @Override
    public List<? extends SkinPack> getSkinPacks() {
        return this.skinPacks;
    }

    public Set<String> getSkinCategories() {
        return this.categorySkins.keySet();
    }
}
