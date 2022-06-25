package disguiseplus.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import disguiseplus.skin.Skin;
import disguiseplus.skin.reader.SkinReader;
import net.pinger.common.http.HttpRequest;
import net.pinger.common.http.HttpResponse;
import net.pinger.common.http.request.HttpPostRequest;
import net.pinger.common.lang.Lists;
import disguiseplus.SkinDatabase;
import disguiseplus.file.Reader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SkinPackLoader {

    private static final Logger logger = LoggerFactory.getLogger("SkinPackLoader");
    private final static String MINESKIN_URL = "https://api.mineskin.org/generate/url?url=%s";
    private final static String MC_TEXTURES = "https://texture.namemc.com/%s/%s/%s.png";

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public static void createNewSkinPack(String pack, String category, String fileName) {
        pack = pack.replace("_", " ");
        category = category.replace("_", " ");

        // Create the new pack name
        logger.info("Reading from " + fileName);

        JsonArray array = new JsonArray();

        List<String> skins = Lists.newArrayList();

        // Going through each file
        for (File file : new File(SkinDatabase.BASE_PATH, fileName).listFiles()) {
            // Retrieve the skins
            skins.addAll(SkinPackLoader.retrieveSkinUrls(file));
        }

        String finalCategory = category;
        String finalPack = pack;

        service.scheduleAtFixedRate(() -> {
            SkinPackLoader.retrieveSkin(skins.get(0), skin -> {
                try {
                    array.add(skin.toJsonObject());

                    // Remove the first
                    skins.remove(0);

                    logger.info("Skins Left To Retrieve -> " + skins.size());

                    if (skins.isEmpty()) {
                        logger.info("Saving the skins into the end file.");

                        // Create the category file
                        File f = new File(SkinDatabase.BASE_PATH, finalCategory);
                        f.mkdirs();

                        // Create the pack file
                        File pf = new File(f, finalPack);
                        pf.mkdirs();

                        File file = new File(pf, "data.json");

                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            writer.write(SkinDatabase.GSON.toJson(array));
                        }

                        SkinPackLoader.saveCategory(finalCategory, finalPack);

                        logger.info(String.format("Successfully saved %s skins!", array.size()));
                    }
                } catch (IOException e) {
                    logger.error("Failed to load a skin -> ", e.getMessage());
                }
            });
        }, 3, 6, TimeUnit.SECONDS);
    }

    /**
     * This method retrieves all skins from a specific source.
     *
     * @param file the file that contains the source
     * @return a list of image urls
     */

    private static List<String> retrieveSkinUrls(File file) {
        List<String> urls = Lists.newArrayList();

        try {
            Document execute = Jsoup.parse(file, "UTF-8");
            Elements elementsByClass = execute.getElementsByClass("card mb-2");

            for (Element element : elementsByClass) {
                Element x = element.getElementsByTag("a").get(0);

                // Get the value
                String value = x.attr("href").replace("/skin/", "");
                String first = value.substring(0, 2);
                String second = value.substring(2, 4);

                String finalUrl = String.format(MC_TEXTURES, first, second, value);
                urls.add(finalUrl);
            }

        } catch (IOException e) {
            logger.error("Failed to retrieve skins from the file -> " + file.getName());
            logger.error(e.getMessage());
        }

        return urls;
    }

    private static void retrieveSkin(String url, Consumer<Skin> skin) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = new HttpPostRequest(String.format(MINESKIN_URL, url));
                HttpResponse response = request.connect();

                JsonObject object = SkinDatabase.GSON.fromJson(response.getResponse(), JsonObject.class)
                        .getAsJsonObject("data")
                        .getAsJsonObject("texture");

                skin.accept(SkinReader.getSkinFromMineskin(object));
            } catch (IOException e) {
                logger.error("Failed to retrieve a skin from the url -> " + url);
                logger.error(e.getMessage());
            }
        });
    }

    public static void saveCategory(String category, String pack) throws IOException {
        File f = new File(SkinDatabase.BASE_PATH, "categories.json");

        f.mkdirs();
        JsonObject object;

        // Check if exists
        if (!f.exists())
            object = new JsonObject();
        else
            object = SkinDatabase.GSON.fromJson(Reader.read(f), JsonObject.class);

        if (object.has(category)) {
            object.get(category).getAsJsonArray().add(pack);
        } else {
            JsonArray arr = new JsonArray();
            arr.add(pack);

            object.add(category, arr);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            writer.write(SkinDatabase.GSON.toJson(object));
        }
    }



}
