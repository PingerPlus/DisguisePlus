package net.pinger.disguiseplus;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.exception.DownloadFailedException;
import net.pinger.disguiseplus.exception.SaveFailedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * This class represents a factory holder for every skin that gets loaded
 * within the {@link Disguise} plugin.
 * <p>
 * Essentially, each skin that gets loaded belongs to a unique {@link SkinPack},
 * and each {@link SkinPack} belongs to a unique category.
 *
 * Apart from this, do note that two skin packs can have the same name in two different categories, which is why
 * it is preferred to use {@link #getSkinPack(String, String)} where you need to specify the
 * category name, rather than {@link #getSkinPack(String)}
 *
 * @since 2.0
 * @author Pinger
 */

public interface SkinFactory {

    void createCategory(String category);

    void deleteSkinCategory(String category);

    SkinPack createSkinPack(String category, String name, List<Skin> skins, boolean custom);

    SkinPack createSkinPack(String category, String name, List<Skin> skins);

    SkinPack createSkinPack(String category, String name, boolean custom);

    SkinPack createSkinPack(String category, String name);

    void deleteSkinPack(SkinPack pack);

    /**
     * This method is used to retrieve a certain {@link SkinPack}
     * with a specific name.
     * <p>
     * Do note about the potential problems when using this method,
     * which you can see at the declaration of this class.
     *
     * @see SkinFactory
     * @param name the name of the pack
     * @return the given skin pack
     */

    @Nullable
    SkinPack getSkinPack(String name);

    /**
     * This method is used to retrieve a certain {@link SkinPack}
     * from a certain category.
     *
     * @param category the category
     * @param name the name of the pack
     * @return the skin pack
     */

    @Nullable
    SkinPack getSkinPack(String category, String name);

    /**
     * This method is used to retrieve all skin packs
     * that are listed within the specified category name.
     *
     * @param category the category name
     * @return the skin pack
     */

    List<? extends SkinPack> getSkinPacks(String category);

    /**
     * This method is used to retrieve all skin packs
     * combined into a single list.
     *
     * @return the skin packs
     */

    List<? extends SkinPack> getSkinPacks();

    @Nullable
    Skin getRandomSkin();

    /**
     * This method retrieves a random skin from the
     * specified category.
     * <p>
     * This method will return null if the category was not found,
     * or if the category doesn't contain any skin packs with skins in it.
     *
     * @param category the category to search for
     * @return the random skin
     */

    @Nullable
    Skin getRandomSkin(String category);

    Set<String> getCategories();

    void downloadSkins() throws DownloadFailedException;

    void saveSkins() throws SaveFailedException;

    File getFile();

    File getCategoriesFile();
}
