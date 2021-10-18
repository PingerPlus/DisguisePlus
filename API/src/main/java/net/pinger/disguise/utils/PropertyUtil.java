package net.pinger.disguise.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.pinger.bukkit.nms.NMS;
import net.pinger.disguise.skin.Skin;

import java.util.UUID;

public class PropertyUtil {

    /**
     * Creates a new property from the given skin details
     * which then can be applied to a player that is online on the server.
     * <p>
     * Note that after 1.7 Minecraft version the Property package
     * has been changed from {@link net.minecraft.util.com.mojang.authlib.properties.Property} to {@link com.mojang.authlib.properties.Property}.
     *
     * @param skin the skin
     * @return a new property object
     */

    public static Object getProperty(Skin skin) {
        if (NMS.atLeast("1.8"))
            return new com.mojang.authlib.properties.Property("textures", skin.getValue(), skin.getSignature());

        return new Property("textures", skin.getValue(), skin.getSignature());
    }

    public static Object createProfile(Skin skin) {
        UUID id = UUID.randomUUID();

        if (NMS.atLeast("1.8")) {
            GameProfile profile = new GameProfile(id, "Player");
            profile.getProperties().put("textures", (com.mojang.authlib.properties.Property) getProperty(skin));

            // Return the profile
            return profile;
        }

        net.minecraft.util.com.mojang.authlib.GameProfile profile = new net.minecraft.util.com.mojang.authlib.GameProfile(id, "Player");
        profile.getProperties().put("textures", (Property) getProperty(skin));

        // Return the profile
        return profile;
    }

}
