package net.pinger.disguiseplus.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

public class StorageLoader {

    public static void init(Storage storage, InputStream stream) {
        final String contents;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            contents = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to read contents from hynick.sql", e);
            return;
        }

        final String[] queries = contents.split(";");
        try (final Connection connection = storage.getConnection()) {
            for (final String query : queries) {
                try (final PreparedStatement stat = connection.prepareStatement(query)) {
                    stat.execute();
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to execute a query", e);
        }
    }

}
