package net.pinger.disguise.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pinger.common.file.Reader;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpGetRequest;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.SkinFactory;
import net.pinger.disguise.exceptions.SkinCloudDownloadException;
import net.pinger.disguise.skin.SimpleSkinPack;
import net.pinger.disguise.Skin;
import net.pinger.disguise.SkinPack;
import net.pinger.disguise.skin.loader.SkinPackLoader;
import net.pinger.disguise.utils.HttpUtil;
import net.pinger.disguise.utils.ReferenceUtil;
import net.pinger.disguise.utils.SkinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SkinFactoryImpl implements SkinFactory {

    private static final Logger logger = LoggerFactory.getLogger("SkinFactory");

    // The list of skins
    private final List<Skin> skins = Lists.newArrayList();
    private final List<SkinPack> skinPacks = Lists.newArrayList();

    // Categorized skins
    private final Map<String, List<SkinPack>> categorySkins = new TreeMap<>();

    // The disguise instance
    private final DisguisePlus dp;

    public SkinFactoryImpl(DisguisePlus dp) {
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

        // Parent file
        File parent = new File(new File(this.dp.getDataFolder(), "data"), "categories");

        // Retrieve first from the categories.json
        File categories = new File(parent, "categories.json");

        if (categories.exists() && categories.length() >= 1) {
            this.retrievePacksLocally(parent, categories, atom);
        }

        try {
            // Establish the connection
            HttpRequest request = new HttpGetRequest(HttpUtil.CATEGORY_URL);
            HttpResponse response = request.connect();

            // This is essentially the categories.json file
            JsonObject object = this.dp.getGson().fromJson(response.getResponse(), JsonObject.class);

            for (Map.Entry<String, JsonElement> element : object.entrySet()) {
                for (JsonElement packName : element.getValue().getAsJsonArray()) {
                    // Add the pack to the list
                    if (this.getSkinPack(element.getKey(), packName.getAsString()) != null)
                        continue;

                    SkinPack pack = SkinPackLoader.getSkinPack(element.getKey(), packName.getAsString());
                    this.skinPacks.add(pack);

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
            if (this.categorySkins.containsKey(skins.getCategory())) {
                if (this.getSkinPack(skins.getCategory(), skins.getName()) != null) {
                    continue;
                }

                this.categorySkins.get(skins.getCategory()).add(skins);
            } else {
                this.categorySkins.put(skins.getCategory(), Lists.newArrayList(skins));
            }
        }
    }

    private void retrievePacksLocally(File parent, File f, AtomicInteger atom) {
        // Load from the categories.json
        JsonObject categories = ReferenceUtil.GSON.fromJson(Reader.read(f), JsonObject.class);

        for (Map.Entry<String, JsonElement> category : categories.entrySet()) {
            for (JsonElement element : category.getValue().getAsJsonArray()) {
                // The file
                File result = new File(new File(new File(parent, category.getKey()), element.getAsString()), "data.json");
                SkinPack pack = SkinPackLoader.getSkinPack(result, category.getKey(), element.getAsString());

                if (pack == null)
                    continue;

                this.skinPacks.add(pack);
                atom.addAndGet(pack.getSkins().size());
            }
        }

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

    @Override @Nonnull
    public Skin getRandomSkin() {
        return this.skins.get(new Random().nextInt(this.skins.size()));
    }

    @Override
    public Skin getRandomSkin(String category) {
        List<SkinPack> packs = this.categorySkins.get(category);

        if (packs == null || packs.isEmpty()) {
            return null;
        }

        int index = new Random().nextInt(packs.size());
        SkinPack pack = packs.get(index);

        // Check if the pack has any skins
        // We have to check this for the NullPointerException
        if (pack.getSkins().isEmpty()) {
            return null;
        }

        // Get the second
        // Random index
        int secondIndex = new Random().nextInt(pack.getSkins().size());

        // Return that
        // Second skin pack
        return pack.getSkins().get(secondIndex);
    }

    @Nullable
    @Override
    public SkinPack getSkinPack(String name) {
        for (SkinPack pack : this.getSkinPacks()) {
            if (pack.getName().equalsIgnoreCase(name))
                return pack;
        }

        return null;
    }

    @Nullable
    @Override
    public SkinPack getSkinPack(String category, String name) {
        for (SkinPack pack : this.getSkinPacks(category)) {
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

    /**
     * This method saves the entire skin factory to the local server for faster retrieval.
     *
     * @author Pinger
     * @since 2.0.0
     */

    public void saveLocally() {
        File base = new File(new File(this.dp.getDataFolder(), "data"), "categories");
        base.mkdirs();

        JsonObject obj = new JsonObject();

        for (Map.Entry<String, List<SkinPack>> pack : this.categorySkins.entrySet()) {
            // Add for each category
            JsonArray array = new JsonArray();

            // Create a new file for this category
            File dir = new File(base, pack.getKey());
            dir.mkdirs();

            // Now create a folder for each pack
            for (SkinPack sp : pack.getValue()) {
                File pf = new File(dir, sp.getName());
                pf.mkdirs();

                // Also create the data file and store it
                File data = new File(pf, "data.json");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(data))) {
                    writer.write(ReferenceUtil.GSON.toJson(((SimpleSkinPack) sp).toJsonArray()));
                } catch (IOException e) {
                    logger.error("Failed to save data within the pack -> ", e);
                    logger.error(e.getMessage());
                }

                array.add(sp.getName());
            }

            obj.add(pack.getKey(), array);
        }

        File categories = new File(base, "categories.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(categories))) {
            writer.write(ReferenceUtil.GSON.toJson(obj));
        } catch (IOException e) {
            logger.error("Failed to save the categories.json file", e);
        }
    }

    public boolean deleteSkinPack(SkinPack pack) {
        SimpleSkinPack converted = (SimpleSkinPack) pack;

        // Remove from the target files
        this.categorySkins.get(pack.getCategory()).remove(pack);
        this.skinPacks.remove(pack);

        if (converted.getBase() == null || !converted.getBase().exists()) {
            return true;
        }

        return converted.delete();
    }
}
