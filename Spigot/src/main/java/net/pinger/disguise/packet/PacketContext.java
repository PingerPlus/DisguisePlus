package net.pinger.disguise.packet;

import com.google.common.collect.Sets;
import net.pinger.common.lang.Lists;
import net.pinger.common.lang.Maps;
import net.pinger.disguise.exceptions.ProviderNotFoundException;
import net.pinger.disguise.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PacketContext {

    private static final Logger logger = LoggerFactory.getLogger("PacketManager");

    private static PacketProvider<?> provider = null;
    private static final Set<Class<? extends PacketProvider<?>>> providers = Sets.newLinkedHashSet();
    private static final Map<String, List<String>> providerCompatibility = Maps.newHashMap();

    static {
        providers.add(PacketProvider_v1_17_1.class);
        providers.add(PacketProvider_v1_17.class);

        // The compatibilities
        providerCompatibility.put("1.16.4", Lists.newArrayList("1.16.5"));
        providerCompatibility.put("1.16.2", Lists.newArrayList("1.16.3"));
    }

    public static PacketProvider<?> getCorrespondingProvider() throws ProviderNotFoundException {
        if (provider != null)
            throw new IllegalArgumentException("PacketProvider has already been found");

        for (Class<? extends PacketProvider<?>> clazz : providers) {
            try {
                String name = clazz.getName().substring(clazz.getName().lastIndexOf("v") + 1);
                String[] splitter = name.split("_");
                String version = String.join(".", splitter);

                // If we found the class with the corresponding version
                if (MinecraftServer.isVersion(version)) {
                    logger.info("Found the appropriate provider for version " + MinecraftServer.STRIPPED_VERSION);
                    return provider = clazz.getConstructor().newInstance();
                }

                if (providerCompatibility.containsKey(version)) {
                    for (String serverVersion : providerCompatibility.get(version)) {
                        if (MinecraftServer.isVersion(serverVersion)) {
                            // Then the current class is also compatible with the current version
                            logger.info("Found the appropriate provider for version " + name.replace("_", "."));
                            return provider = clazz.getConstructor().newInstance();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while trying to find an applicable provider for version " + Bukkit.getVersion());
                logger.error(e.getMessage());
            }
        }

        throw new ProviderNotFoundException();
    }

}
