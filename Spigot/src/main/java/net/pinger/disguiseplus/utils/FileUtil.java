package net.pinger.disguiseplus.utils;

import java.io.File;

public class FileUtil {

    /**
     * This method is a DFS type algorithm used to delete a
     * file or a folder.
     * <p>
     * The reason this method uses recursion for deleting a folder is that
     * java doesn't allow deleting non-empty folders.
     * @see File#delete()
     * @param file the file to delete
     */

    public static void deleteFile(File file) {
        if (!file.isDirectory()) {
            file.delete();
            return;
        }

        // Loop through each child file
        // And delete it first
        // If it is a directory
        for (File f : file.listFiles()) {
            // Check if the child file is a directory
            // And if so do the recursion
            if (f.isDirectory()) {
                deleteFile(f);
                continue;
            }

            f.delete();
        }

        // Finally, we can delete this file
        file.delete();
    }

}
