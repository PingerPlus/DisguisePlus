package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.pinger.disguise.Skin;
import net.pinger.disguise.http.HttpRequest;
import net.pinger.disguise.http.HttpResponse;
import net.pinger.disguise.http.request.HttpGetRequest;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.exception.DownloadFailedException;
import net.pinger.disguiseplus.exception.SaveFailedException;
import net.pinger.disguiseplus.utils.HttpUtil;
import net.pinger.disguiseplus.utils.SkinUtil;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

public class SkinFactoryImpl implements SkinFactory {

    // Used information
    private final List<SkinPack> skinPacks = new ArrayList<>();
    private final Map<String, List<SkinPack>> categorySkins = new TreeMap<>();

    // Files
    private final File file;
    private final File categoriesFile;

    // The disguise instance
    private final DisguisePlus dp;
    private final boolean downloadBaseSkins;

    public SkinFactoryImpl(DisguisePlus dp, boolean downloadBaseSkins) {
        this.dp = dp;
        this.downloadBaseSkins = downloadBaseSkins;

        // Update the file based on the main
        this.file = new File(this.dp.getDataFolder(), "categories");
        this.categoriesFile = new File(this.file, "categories.json");
    }

    @Override
    public void createCategory(String category) {
        this.categorySkins.putIfAbsent(category, new ArrayList<>());
    }

    @Override
    public void deleteSkinCategory(String category) {
        this.categorySkins.remove(category);

        // Also delete all packs that are inside this category
        this.skinPacks.removeIf(pack -> pack.getCategory().equalsIgnoreCase(category));
    }

    @Override
    public SkinPack createSkinPack(String category, String name, List<Skin> skins, boolean custom) {
        // Edge case
        // Where we do not create new skin packs
        // If already the same name exists in the given category
        if (getSkinPack(category, name) != null) {
            return null;
        }

        // Create the new implementation instance
        SkinPack pack = new SkinPackImpl(null, category, name, skins, custom);

        // We do not cache skin packs when they are not custom
        // And the downloadBaseSkins is set to false
        if (!custom && !this.downloadBaseSkins) {
            return pack;
        }

        // Add to skin packs
        this.skinPacks.add(pack);

        // Add the skin pack to the categories and skin packs if possible
        if (this.categorySkins.containsKey(category)) {
            // Add the pack
            this.categorySkins.get(category).add(pack);

            // Return the initialized
            return pack;
        }

        // Create the category
        this.categorySkins.put(category, Collections.singletonList(pack));
        return pack;
    }

    @Override
    public SkinPack createSkinPack(String category, String name, List<Skin> skins) {
        return this.createSkinPack(category, name, skins, true);
    }

    @Override
    public SkinPack createSkinPack(String category, String name, boolean custom) {
        return this.createSkinPack(category, name, new ArrayList<>(), custom);
    }

    @Override
    public SkinPack createSkinPack(String category, String name) {
        return this.createSkinPack(category, name, new ArrayList<>());
    }

    @Override
    public void deleteSkinPack(SkinPack pack) {
        // Delete from categories
        this.categorySkins.get(pack.getCategory()).remove(pack);

        // Delete from skin packs
        this.skinPacks.remove(pack);

        // Delete the skin from here
        if (pack.getFile() == null || !pack.getFile().exists()) {
            return;
        }

        // Delete the skin pack
        // If the file exists
        pack.getFile().delete();
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

    @Override
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

    @Override
    public void downloadSkins() throws DownloadFailedException {
        // When downloading
        // We must clear the current skin files
        this.skinPacks.clear();
        this.categorySkins.clear();

        try {
            // Always download local skins first
            if (categoriesFile.exists() && categoriesFile.length() >= 1) {
                try (FileReader reader = new FileReader(this.categoriesFile)) {
                    // Get a jsonObject from the reader
                    // Read all categories here
                    JsonObject categories = DisguisePlus.GSON.fromJson(reader, JsonObject.class);

                    // Loop through all categories
                    for (Map.Entry<String, JsonElement> category : categories.entrySet()) {
                        // Get the category directory
                        File categoryDir = new File(getFile(), category.getKey());

                        // Loop through all skin packs
                        for (JsonElement pack : category.getValue().getAsJsonArray()) {
                            // Get the pack directory
                            File packDir = new File(categoryDir, pack.getAsString());

                            // Get the json file
                            File packFile = new File(packDir, "pack.json");

                            // Avoid an exception by skipping this
                            if (!packFile.exists()) {
                                continue;
                            }

                            // Create the file reader
                            try (FileReader packReader = new FileReader(packFile)) {
                                // We automatically save the files
                                // So need to check for saving here
                                DisguisePlus.GSON.fromJson(packReader, SkinPack.class);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DownloadFailedException("Failed to download skins from local files", e);
        }

        // Check if we should even try to download from the internet
        if (!this.downloadBaseSkins) {
            return;
        }

        // Try to download from the database
        try {
            // Establish the connection
            HttpRequest request = new HttpGetRequest(HttpUtil.CATEGORY_URL);
            HttpResponse response = request.connect();

            // This is essentially the categories.json file
            JsonObject categories = DisguisePlus.GSON.fromJson(response.getResponse(), JsonObject.class);

            // Loop through all categories
            for (Map.Entry<String, JsonElement> category : categories.entrySet()) {
                // Get the category name
                String categoryName = category.getKey();

                // Loop through all skin packs
                for (JsonElement pack : category.getValue().getAsJsonArray()) {
                    // Get the pack name
                    String packName = pack.getAsString();

                    // Check if the item
                    // Is not already loaded
                    if (this.getSkinPack(category.getKey(), pack.getAsString()) != null)
                        continue;

                    // Create a new list of skins
                    List<Skin> skins = new ArrayList<>();

                    // We need to create a new request
                    // In order to get the exact file
                    HttpRequest packRequest = new HttpGetRequest(HttpUtil.toSkinPack(categoryName, packName));
                    HttpResponse packResponse = packRequest.connect();

                    // Load the array of skins
                    JsonArray array = DisguisePlus.GSON.fromJson(packResponse.getResponse(), JsonArray.class);

                    // Looping through each skin
                    for (JsonElement object : array) {
                        skins.add(SkinUtil.getFromJson(object.getAsJsonObject()));
                    }

                    // Save the skin
                    this.createSkinPack(categoryName, packName, skins, false);
                }
            }
        } catch (Exception e) {
            throw new DownloadFailedException("Failed to download skins from the database", e);
        }
    }

    @Override
    public void saveSkins() throws SaveFailedException {
        try {
            // Object that will later be converted
            // Or rather saved in the categories.json file
            JsonObject categories = new JsonObject();

            // Loop through each category
            for (Map.Entry<String, List<SkinPack>> category : this.categorySkins.entrySet()) {
                // Make a json array for each category
                JsonArray packArray = new JsonArray();

                // Get the category directory
                File categoryDir = new File(getFile(), category.getKey());

                // Loop through each skin category
                for (SkinPack skinPack : category.getValue()) {
                    // Get the pack directory
                    File packDir = new File(categoryDir, skinPack.getName());

                    // Get the json file
                    File packFile = new File(packDir, "pack.json");
//                    packFile.mkdirs();

                    // Save the files
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(packFile))) {
                        writer.write(DisguisePlus.GSON.toJson(skinPack, SkinPack.class));
                    }

                    // Add to the array
                    packArray.add(skinPack.getName());
                }

                categories.add(category.getKey(), packArray);
            }

            // Save the categories.json file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getCategoriesFile()))) {
                writer.write(DisguisePlus.GSON.toJson(categories));
            }
        } catch (Exception e) {
            throw new SaveFailedException("Failed to save skins from the skin cache", e);
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public File getCategoriesFile() {
        return this.categoriesFile;
    }
}
