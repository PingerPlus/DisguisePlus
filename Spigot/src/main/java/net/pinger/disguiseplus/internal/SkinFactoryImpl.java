package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.loader.SkinPackLoader;
import net.pinger.disguiseplus.utils.HttpUtil;
import net.pinger.disguiseplus.utils.ReferenceUtil;
import net.pinger.disguiseplus.utils.SkinUtil;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.SkinPack;
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
    private final List<SkinPack> skinPacks = new ArrayList<>();
    private final Map<String, List<SkinPack>> categorySkins = new TreeMap<>();

    // The disguise instance
    private final DisguisePlus dp;

    public SkinFactoryImpl(DisguisePlus dp) {
        this.dp = dp;

        // Initialize the loader
        this.retrieveFromCloud();
        this.retrieveSkinPacks();
    }

    @Override
    public void createCategory(String category) {
        this.categorySkins.put(category, new ArrayList<>());
    }

    @Override
    public void deleteSkinCategory(String category) {
        this.categorySkins.remove(category);
    }

    @Override
    public SkinPack createSkinPack(String category, String name) {
        return new SkinPackImpl(null, category, name, new ArrayList<>());
    }

    @Override
    public void deleteSkinPack(SkinPack pack) {

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
        return this.categorySkins.getOrDefault(category, new ArrayList<>());
    }

    @Override
    public List<? extends SkinPack> getSkinPacks() {
        return this.skinPacks;
    }

    @Override @Nonnull
    public Skin getRandomSkin() {
        // Get random skin pack first
        int packIndex = new Random().nextInt(this.skinPacks.size());
        SkinPack pack = this.skinPacks.get(packIndex);

        // We're avoiding IndexOutOfBoundException here
        // By waiting for index
        if (pack.getSkins().isEmpty()) {
            return null;
        }

        // Load the index
        int skinIndex = new Random().nextInt(pack.getSkins().size());
        return pack.getSkins().get(skinIndex);
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

    @Override
    public Set<String> getCategories() {
        return this.categorySkins.keySet();
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

                    net.pinger.disguiseplus.SkinPack pack = SkinPackLoader.getSkinPack(element.getKey(), packName.getAsString());
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

        for (net.pinger.disguiseplus.SkinPack skins : skinPacks) {
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
                net.pinger.disguiseplus.SkinPack pack = SkinPackLoader.getSkinPack(result, category.getKey(), element.getAsString());

                if (pack == null)
                    continue;

                this.skinPacks.add(pack);
                atom.addAndGet(pack.getSkins().size());
            }
        }

        for (net.pinger.disguiseplus.SkinPack skins : skinPacks) {
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

        for (Map.Entry<String, List<net.pinger.disguiseplus.SkinPack>> pack : this.categorySkins.entrySet()) {
            // Add for each category
            JsonArray array = new JsonArray();

            // Create a new file for this category
            File dir = new File(base, pack.getKey());
            dir.mkdirs();

            // Now create a folder for each pack
            for (net.pinger.disguiseplus.SkinPack sp : pack.getValue()) {
                File pf = new File(dir, sp.getName());
                pf.mkdirs();

                // Also create the data file and store it
                File data = new File(pf, "data.json");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(data))) {
                    writer.write(ReferenceUtil.GSON.toJson(((SkinPackImpl) sp).toJsonArray()));
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

//    public boolean deleteSkinPack(SkinPack pack) {
//        SkinPackImpl converted = (SkinPackImpl) pack;
//
//        // Remove from the target files
//        this.categorySkins.get(pack.getCategory()).remove(pack);
//        this.skinPacks.remove(pack);
//
//        if (converted.getBase() == null || !converted.getBase().exists()) {
//            return true;
//        }
//
//        return converted.delete();
//    }
}
