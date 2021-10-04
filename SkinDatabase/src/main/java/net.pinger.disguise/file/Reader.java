/*
 * Copyright (c) 2021 - Pinger™
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.disguise.database.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Reader {

    /**
     * The default logger for this class
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    /**
     * Reads all contents from a specific file
     * If an {@link IOException} is thrown, the resulting string will be empty.
     *
     * @param file the file we're reading from
     * @return the contents of the file
     */

    public static String read(File file) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String k;
            while ((k = reader.readLine()) != null) {
                builder.append(k).append("\n");
            }
        } catch (IOException e) {
            LOGGER.error("Caught an error while trying to read a file: " + e.getMessage());
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
        return Reader.read(new File(path));
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
            LOGGER.error("Caught an error while trying to read from a stream: " + e.getMessage());
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
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String k;
            while ((k = reader.readLine()) != null) {
                lines.add(k);
            }
        } catch (IOException e) {
            LOGGER.error("Caught an error while trying to read a file: " + e.getMessage());
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
        return Reader.readLines(new File(path));
    }

    /**
     * Reads all contents from a specific {@link InputStream} and converts them into a {@link List}
     * If an {@link IOException} is thrown while reading the stream, the resulting string will be empty.
     *
     * @param stream the stream we're reading from
     * @return the contents of the stremam
     */

    public static List<String> readLines(InputStream stream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String k;
            while ((k = reader.readLine()) != null) {
                lines.add(k);
            }
        } catch (IOException e) {
            LOGGER.error("Caught an error while trying to read from a stream: " + e.getMessage());
        }

        return lines;
    }

}
