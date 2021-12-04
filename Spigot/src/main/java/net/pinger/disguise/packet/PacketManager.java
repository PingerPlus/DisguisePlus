package net.pinger.disguise.packet;

import com.google.common.collect.Sets;
import net.pinger.bukkit.nms.NMS;
import net.pinger.common.lang.Lists;
import net.pinger.common.lang.Maps;
import net.pinger.disguise.exceptions.ProviderNotFoundException;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PacketManager {

    private static final Logger logger = LoggerFactory.getLogger("PacketManager");

    private static PacketProvider<?> provider = null;
    private static final Set<Class<? extends PacketProvider<?>>> providers = Sets.newHashSet();
    private static Map<String, List<String>> providerCompatibility = Maps.newHashMap();

    static {
        providers.add(PacketProvider_v1_17_1.class);

        // The compatibilities
        providerCompatibility.put("1.16.4", Lists.newArrayList("1.16.5"));
        providerCompatibility.put("1.16.2", Lists.newArrayList("1.16.3"));
        providerCompatibility.put("1.17", Lists.newArrayList("1.17.1"));
    }

    public static PacketProvider<?> getApplicableProvider() throws ProviderNotFoundException {
        if (provider != null)
            throw new IllegalArgumentException("PacketProvider has already been found");

        for (Class<? extends PacketProvider<?>> clazz : providers) {
            try {
                String name = clazz.getName().substring(clazz.getName().lastIndexOf("v") + 1);
                String[] splitter = name.split("_");
                String version = String.join(".", splitter);

                // If we found the class with the corresponding version
                if (NMS.isVersion(version)) {
                    logger.info("Found the applicable provider -> " + name);
                    return provider = clazz.getConstructor().newInstance();
                }

                if (providerCompatibility.containsKey(version)) {
                    for (String serverVersion : providerCompatibility.get(version)) {
                        if (NMS.isVersion(serverVersion)) {
                            // Then the current class is also compatible with the current version
                            logger.info("Found the applicable provider -> " + name);
                            return provider = clazz.getConstructor().newInstance();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while trying to find an applicable provider for version -> " + Bukkit.getVersion());
                logger.error(e.getMessage());
            }
        }

        throw new ProviderNotFoundException();
    }

}
