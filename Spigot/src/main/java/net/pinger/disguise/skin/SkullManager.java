package net.pinger.disguise.skin;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.nms.NMS;
import net.pinger.disguise.utils.PropertyUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SkullManager {

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger("SkullManager");

    private final LoadingCache<UUID, ItemStack> skulls = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(CacheLoader.from(this::loadSkull));

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    public ItemStack getSkullFrom(UUID id) {
        try {
            return this.skulls.get(id);
        } catch (Exception e) {
            logger.error(String.format("Failed to retrieve skull from id -> %s", id), e);
        }

        return null;
    }

    private ItemStack loadSkull(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);

        // Getting the skull
        ItemStack stack = new ItemBuilder(FreshMaterial.PLAYER_HEAD.toMaterial(), (short) 3).toItemStack();
        SkullMeta meta = (SkullMeta) stack.getItemMeta();

        // Setting the owner
        if (NMS.atLeast("1.12"))
            meta.setOwningPlayer(player);
        else
            meta.setOwner(player.getName());

        stack.setItemMeta(meta);
        return stack;
    }

    public ItemStack getDefaultPlayerSkull() {
        return this.getSkullFrom(UUID.fromString("e5cb34a9-9f69-44ad-adeb-b81d5ce3d99e"));
    }

    static void mutateItemMeta(SkullMeta meta, Skin skin) {
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }

            metaSetProfileMethod.invoke(meta, PropertyUtil.createProfile(skin));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }

                metaProfileField.set(meta, PropertyUtil.createProfile(skin));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                logger.error("Failed to mutate the given meta object.");
                logger.error(ex2.getMessage());
            }
        }
    }


}
