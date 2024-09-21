package net.pinger.disguiseplus.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.pinger.disguise.http.HttpRequest;
import net.pinger.disguise.http.HttpResponse;
import net.pinger.disguise.http.request.HttpGetRequest;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.exception.DownloadFailedException;
import net.pinger.disguiseplus.exception.SaveFailedException;
import net.pinger.disguiseplus.utils.HttpUtil;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import net.pinger.disguiseplus.utils.RandomUtil;

public class SkinFactoryImpl implements SkinFactory {
    private final List<SkinPack> skinPacks = new ArrayList<>();
    private final Map<String, List<SkinPack>> categorySkins = new TreeMap<>();
    private final Path categoriesDirectory;
    private final Path categoriesFile;
    private final boolean downloadBaseSkins;

    public SkinFactoryImpl(DisguisePlus dp, boolean downloadBaseSkins) {
        // The disguise instance
        this.downloadBaseSkins = downloadBaseSkins;

        // Update the file based on the main
        this.categoriesDirectory = Paths.get(dp.getDataFolder().getAbsolutePath(), "categories");
        this.categoriesFile = this.categoriesDirectory.resolve("categories.json");
    }

    @Override
    public void createCategory(String category) {
        this.categorySkins.putIfAbsent(category, new ArrayList<>());
    }

    @Override
    public void deleteCategory(String category) {
        final List<SkinPack> skinPacks = this.categorySkins.remove(category);
        if (skinPacks.isEmpty()) {
            return;
        }

        for (final SkinPack pack : skinPacks) {
            final Path packFile = pack.getFile();
            if (packFile == null || !Files.exists(packFile)) {
                continue;
            }

            // Delete the skin pack
            // If the file exists
            try (final Stream<Path> paths = Files.walk(pack.getFile())) {
                final List<Path> toDelete = paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                for (final Path path : toDelete) {
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                DisguisePlus.getOutput().info("Failed to delete category ", e);
            }
        }
    }

    @Override
    public SkinPack createSkinPack(String category, String name, List<Skin> skins, boolean custom) {
        if (this.getSkinPack(category, name) != null) {
            return null;
        }

        // Create the new implementation instance
        final SkinPack pack = new SkinPackImpl(null, category, name, skins, custom);
        if (!custom && !this.downloadBaseSkins) {
            return pack;
        }

        // Add to skin packs
        this.skinPacks.add(pack);
        this.categorySkins.compute(category, ($, cat) -> {
            if (cat == null) {
                return new ArrayList<>(Collections.singletonList(pack));
            }

            cat.add(pack);
            return cat;
        });

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
        if (pack.getFile() == null || !Files.exists(pack.getFile())) {
            return;
        }

        // Delete the skin pack
        // If the file exists
        try (final Stream<Path> paths = Files.walk(pack.getFile())) {
            final List<Path> toDelete = paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (final Path path : toDelete) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            DisguisePlus.getOutput().info("Failed to delete skin pack ", e);
        }
    }

    @Nullable
    @Override
    public SkinPack getSkinPack(String name) {
        for (final SkinPack pack : this.getSkinPacks()) {
            if (pack.getName().equalsIgnoreCase(name)) {
                return pack;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public SkinPack getSkinPack(String category, String name) {
        for (final SkinPack pack : this.getSkinPacks(category)) {
            if (pack.getName().equalsIgnoreCase(name)) {
                return pack;
            }
        }

        return null;
    }

    @Override
    public List<? extends SkinPack> getSkinPacks(String category) {
        return this.categorySkins.getOrDefault(category, new ArrayList<>());
    }

    @Override
    public List<? extends SkinPack> getSkinPacks() {
        return this.categorySkins.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public Skin getRandomSkin() {
        final SkinPack pack = RandomUtil.getRandomElement(this.skinPacks, SkinPack::hasSkins);
        return RandomUtil.getRandomElement(pack.getSkins());
    }

    @Override
    public Skin getRandomSkin(String category) {
        final List<SkinPack> packs = this.categorySkins.get(category);
        if (packs == null || packs.isEmpty()) {
            return null;
        }

        final SkinPack pack = RandomUtil.getRandomElement(packs, SkinPack::hasSkins);
        return RandomUtil.getRandomElement(pack.getSkins());
    }

    @Override
    public Set<String> getCategories() {
        return this.categorySkins.keySet();
    }

    @Override
    public void downloadSkins() throws DownloadFailedException {
        this.skinPacks.clear();
        this.categorySkins.clear();

        // Attempt to download skins
        DisguisePlus.getOutput().info("Started the downloading skins process");
        DisguisePlus.getOutput().info("Option for downloading default skins: {}", this.downloadBaseSkins);
        DisguisePlus.getOutput().info("This might take a few seconds if no local files are saved");
        DisguisePlus.getOutput().info("Attempting to download skins from local files");

        CompletableFuture.runAsync(() -> {
            try {
                if (Files.exists(this.categoriesFile)) {
                    try (final BufferedReader reader = Files.newBufferedReader(this.categoriesFile)) {
                        final JsonObject categories = DisguisePlus.GSON.fromJson(reader, JsonObject.class);

                        for (final Map.Entry<String, JsonElement> category : categories.entrySet()) {
                            final Path categoryDir = this.categoriesDirectory.resolve(category.getKey());
                            for (final JsonElement pack : category.getValue().getAsJsonArray()) {
                                final Path packDir = categoryDir.resolve(pack.getAsString());
                                final Path packFile = packDir.resolve("pack.json");
                                if (!Files.exists(packFile)) {
                                    continue;
                                }

                                // Create the file reader
                                try (final BufferedReader fileReader = Files.newBufferedReader(packFile)) {
                                    final SkinPack skinPack = DisguisePlus.GSON.fromJson(fileReader, SkinPack.class);
                                    skinPack.setFile(packFile);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new DownloadFailedException("Failed to download skins from local files", e);
            }

            int skinPacks = this.skinPacks.size();
            int totalSkins = this.skinPacks.stream().mapToInt(pack -> pack.getSkins().size()).sum();

            // Output for different occasions
            if (skinPacks > 0 && totalSkins > 0) {
                DisguisePlus.getOutput().info("Downloaded {} skins from {} different skin packs", totalSkins, skinPacks);
            } else {
                DisguisePlus.getOutput().info("No skins were found within the local cache");
            }

            // Check if we should even try to download from the internet
            if (!this.downloadBaseSkins) {
                DisguisePlus.getOutput().info("Skipping downloading from the database since the config option is turned off");
                return;
            }

            DisguisePlus.getOutput().info("Attempting to download skins from the skin cloud");

            // Try to download from the database
            try {
                final HttpRequest request = new HttpGetRequest(HttpUtil.CATEGORY_URL);
                final HttpResponse response = request.connect();

                final JsonObject categories = DisguisePlus.GSON.fromJson(response.getResponse(), JsonObject.class);
                for (final Map.Entry<String, JsonElement> category : categories.entrySet()) {
                    final String categoryName = category.getKey();
                    for (final JsonElement pack : category.getValue().getAsJsonArray()) {
                        final String packName = pack.getAsString();
                        if (this.getSkinPack(category.getKey(), pack.getAsString()) != null) {
                            continue;
                        }

                        final List<Skin> skins = new ArrayList<>();

                        // We need to create a new request
                        // In order to get the exact file
                        final HttpRequest packRequest = new HttpGetRequest(HttpUtil.toSkinPack(categoryName, packName));
                        final HttpResponse packResponse = packRequest.connect();

                        // Load the array of skins
                        final JsonArray array = DisguisePlus.GSON.fromJson(packResponse.getResponse(), JsonArray.class);
                        for (final JsonElement object : array) {
                            skins.add(DisguisePlus.GSON.fromJson(object, Skin.class));
                        }

                        // Save the skin
                        this.createSkinPack(categoryName, packName, skins, false);
                    }
                }
            } catch (Exception e) {
                throw new DownloadFailedException("Failed to download skins from the database", e);
            }

            skinPacks = this.skinPacks.size();
            totalSkins = this.skinPacks.stream().mapToInt(pack -> pack.getSkins().size()).sum() - totalSkins;

            // Output for different occasions
            if (skinPacks > 0 && totalSkins > 0) {
                DisguisePlus.getOutput().info("Fetched {} skins from {} different skin packs from the database", totalSkins, skinPacks);
            } else {
                DisguisePlus.getOutput().info("No new skins were found within the database");
            }
        });
    }

    @Override
    public void saveSkins() throws SaveFailedException {
        DisguisePlus.getOutput().info("Attempting to save skins locally.");

        try {
            final JsonObject categories = new JsonObject();
            for (final Map.Entry<String, List<SkinPack>> category : this.categorySkins.entrySet()) {
                final JsonArray packArray = new JsonArray();

                // Get the category directory
                final Path categoryDir = this.categoriesDirectory.resolve(category.getKey());
                for (final SkinPack skinPack : category.getValue()) {
                    final Path packDir = categoryDir.resolve(skinPack.getName());
                    Files.createDirectories(packDir);

                    // Get the json file
                    final Path packFile = packDir.resolve("pack.json");
                    try (final BufferedWriter writer = Files.newBufferedWriter(packFile)) {
                        writer.write(DisguisePlus.GSON.toJson(skinPack, SkinPack.class));
                    }

                    // Add to the array
                    packArray.add(new JsonPrimitive(skinPack.getName()));
                }

                categories.add(category.getKey(), packArray);
            }

            // Save the categories.json file
            try (final BufferedWriter writer = Files.newBufferedWriter(this.categoriesFile)) {
                writer.write(DisguisePlus.GSON.toJson(categories));
            }
        } catch (Exception e) {
            throw new SaveFailedException("Failed to save skins from the skin cache", e);
        }

        DisguisePlus.getOutput().info("Successfully saved skins locally");
    }
}
