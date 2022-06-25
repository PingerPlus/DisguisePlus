package net.pinger.disguiseplus.file;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public final class FileReader {

    private static final Logger LOGGER = Bukkit.getLogger();

    /**
     * Prevents initialization of the class
     */

    private FileReader() {}

    /**
     * Reads all contents from a specific file.
     * If an {@link IOException} is thrown while trying to read from the file,
     * the resulting string will be empty.
     *
     * @param file the file we're reading from
     * @return the contents of the file
     */

    public static String read(File file) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String k;
            while ((k = reader.readLine()) != null) {
                builder.append(k).append("\n");
            }
        } catch (IOException e) {
            LOGGER.warning("Caught an error while trying to read a file: " + e.getMessage());
        }

        return builder.toString();
    }

    /**
     * Reads all contents from a specific path file
     * If an {@link IOException} is thrown, the resulting string will be empty.
     *
     * @param path the path of the file we're reading from
     * @return the contents of the file
     */

    public static String read(String path) {
        return FileReader.read(new File(path));
    }

    /**
     * Reads all contents from a specific {@link InputStream}
     * If an {@link IOException} is thrown while reading the stream, the resulting string will be empty.
     *
     * @param stream the stream we're reading from
     * @return the contents of the stremam
     */

    public static String read(InputStream stream) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String k;
            while ((k = reader.readLine()) != null) {
                builder.append(k).append("\n");
            }
        } catch (IOException e) {
            LOGGER.warning("Caught an error while trying to read from a stream: " + e.getMessage());
        }

        return builder.toString();
    }

    /**
     * Reads all contents from a specific file and converts them into a {@link List}
     * If an {@link IOException} is thrown, the resulting string will be empty.
     *
     * @param file the file we're reading from
     * @return the contents of the file
     */

    public static List<String> readLines(File file) {
        List<String> lines = Lists.newLinkedList();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String k;
            while ((k = reader.readLine()) != null) {
                lines.add(k);
            }
        } catch (IOException e) {
            LOGGER.warning("Caught an error while trying to read a file: " + e.getMessage());
        }

        return lines;
    }

    /**
     * Reads all contents from a specific path file and converts them into a {@link List}
     * If an {@link IOException} is thrown, the resulting string will be empty.
     *
     * @param path the path of the file we're reading from
     * @return the contents of the file
     */

    public static List<String> readLines(String path) {
        return FileReader.readLines(new File(path));
    }

    /**
     * Reads all contents from a specific {@link InputStream} and converts them into a {@link List}
     * If an {@link IOException} is thrown while reading the stream, the resulting string will be empty.
     *
     * @param stream the stream we're reading from
     * @return the contents of the stremam
     */

    public static List<String> readLines(InputStream stream) {
        List<String> lines = Lists.newLinkedList();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String k;
            while ((k = reader.readLine()) != null) {
                lines.add(k);
            }
        } catch (IOException e) {
            LOGGER.warning("Caught an error while trying to read from a stream: " + e.getMessage());
        }

        return lines;
    }

}
