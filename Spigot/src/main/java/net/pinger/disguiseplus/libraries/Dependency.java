package net.pinger.disguiseplus.libraries;

import net.byteflux.libby.Library;

public interface Dependency {

    String groupId();

    String artifactId();

    String version();

    Library toLibrary();


}
