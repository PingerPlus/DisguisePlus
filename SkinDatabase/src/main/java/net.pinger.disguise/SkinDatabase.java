package net.pinger.disguise;

import com.google.gson.*;
import net.disguise.database.file.Reader;
import net.disguise.database.skin.Skin;
import net.pinger.disguise.skin.reader.SkinReader;
import net.pinger.disguise.util.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SkinDatabase {

    /**
     * The {@link Gson} object used for manipulating over .json files
     */

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();

    /**
     * The base path of the file where we're writing to
     */

    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "\\database.json";

    /**
     * The base path of the project
     */

    private static final String BASE_PATH = System.getProperty("user.dir");

    /**
     * The base file where we're writing to
     */

    private static final File BASE_FILE = new File(BASE_FILE_PATH);

    /**
     * Represents the skins array returned from the database.json file
     */

    private static final JsonArray SKINS_ARRAY = GSON.fromJson(Reader.read(BASE_FILE), JsonArray.class);

    /**
     * The default logger implementation
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(SkinDatabase.class);

    /**
     * All skins loaded from the database.json
     */

    private static final Set<UUID> SKINS = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        LOGGER.info("Loading all UUID's from the database.json file...");

        // Loop through each skin
        for (JsonElement element : SKINS_ARRAY) {
            // Get the skin object
            JsonObject object = element.getAsJsonObject();

            // Save the uuid
            SKINS.add(UUID.fromString(object.get("uuid").getAsString()));
        }

        // Searching for the query
        while (scanner.hasNextLine()) {
            // Scanner#nextLine is the query here
            SkinDatabase.resolveQuery(scanner.nextLine());
        }

        System.exit(0);
    }

    /**
     * Resolves a certain query.
     *
     * @param query the query that is being resolved
     */

    private static void resolveQuery(String query) throws IOException {
        // Split the arguments by " "
        String[] args = query.split("\\s++");

        // Implies that we're reading a file
        if (args[0].equalsIgnoreCase("read")) {
            if (args.length != 2)
                throw new RuntimeException("You need to specify a single file you want to read");

            File read = new File(BASE_PATH, args[1]);
            if (!read.exists()) {
                LOGGER.error("File doesn't exist " + read.getPath());
                return;
            }

            List<String> lines = Reader.readLines(read);

            for (String line : lines) {
                // Get uuid from each element
                UUID uuid = ConverterUtil.fromString(line);

                // Convert to skin and add to the array
                Skin skin = SkinReader.read(uuid);
                if (skin == null) {
                    LOGGER.error("Skipping UUID of " + uuid + " because it seems to be null.");
                    continue;
                }

                if (SKINS.contains(skin.getUniqueId())) {
                    LOGGER.info("Skipping query with id " + uuid + " because this uuid is already in the database.");
                    continue;
                }

                LOGGER.info("Successfully saved UUID of " + uuid + ".");
                SKINS_ARRAY.add(skin.toJsonObject());
                SKINS.add(skin.getUniqueId());
            }
        }

        // Using guavas method toJson to convert an array to a json string
        String json = GSON.toJson(SKINS_ARRAY);
        try (BufferedWriter reader = new BufferedWriter(new FileWriter(BASE_FILE))) {
            reader.write(json);
        }
    }
}