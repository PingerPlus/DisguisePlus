package net.pinger.disguiseplus.libraries;

import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.LibraryManager;
import net.pinger.disguiseplus.DisguisePlus;

public class DependencyManager {
    private final LibraryManager manager;

    public DependencyManager(DisguisePlus dp) {
        this.manager = new BukkitLibraryManager(dp);
    }

    public void loadDependencies() {
        this.manager.addMavenCentral();
        this.manager.addSonatype();

        for (final Dependency dependency : Dependencies.values()) {
            this.manager.loadLibrary(dependency.toLibrary());
        }
    }
}
