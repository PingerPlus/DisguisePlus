package net.pinger.disguiseplus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BukkitFeatureManager implements FeatureManager {
    private final Set<DisguiseFeature> featuresList = new HashSet<>();

    @Override
    public void registerFeature(DisguiseFeature... features) {
        this.featuresList.addAll(Arrays.asList(features));
    }

    @Override
    public void load() {
        for (final DisguiseFeature feature : this.featuresList) {
            feature.load();
        }
    }

    @Override
    public void reload() {
        for (final DisguiseFeature feature : this.featuresList) {
            feature.reload();
        }
    }
}
