package net.pinger.disguiseplus;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.exception.DownloadFailedException;
import net.pinger.disguiseplus.exception.SaveFailedException;

import javax.annotation.Nullable;
import java.io.File;
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

    /**
     * This method creates a new category with the specified name.
     * <p>
     * In case a category with this name already exists,
     * this method will not replace it.
     *
     * @param category the category to create
     */

    void createCategory(String category);

    /**
     * This method deletes the specified category including
     * the skin packs that are bound to it.
     *
     * @param category the category to delete
     */

    void deleteCategory(String category);

    /**
     * This method creates a new {@link SkinPack} inside the specified category.
     * <p>
     * The skins will be placed immediately inside the created {@link SkinPack}.
     *
     * In case a skin pack with this name already exists in the given category,
     * this method will return null, and no pack will be created.
     *
     * @see SkinPack
     * @param category the category to put the skin pack in
     * @param name the name of the skin pack
     * @param skins the skins to put in the skin pack
     * @param custom whether this pack should be flagged as custom
     * @return the newly created {@link SkinPack}, or null if it already exists.
     */

    @Nullable
    SkinPack createSkinPack(String category, String name, List<Skin> skins, boolean custom);

    /**
     * This method creates a new custom {@link SkinPack} inside the specified category.
     * <p>
     * The skins will be placed immediately inside the created {@link SkinPack}.
     *
     * In case a skin pack with this name already exists in the given category,
     * this method will return null, and no pack will be created.
     *
     * @see SkinPack
     * @param category the category to put the skin pack in
     * @param name the name of the skin pack
     * @param skins the skins to put inside the skin pack
     * @return the newly created {@link SkinPack}, or null if it already exists.
     */

    @Nullable
    SkinPack createSkinPack(String category, String name, List<Skin> skins);

    /**
     * This method creates an empty {@link SkinPack} inside the specified category.
     * <p>
     * In case a skin pack with this name already exists in the given category,
     * this method will return null, and no pack will be created.
     *
     * @see SkinPack
     * @param category the category to put the skin pack in
     * @param name the name of the skin pack
     * @param custom whether this pack should be flagged as custom
     * @return the newly created {@link SkinPack}, or null if it already exists.
     */

    @Nullable
    SkinPack createSkinPack(String category, String name, boolean custom);

    /**
     * This method creates an empty custom {@link SkinPack} inside the specified category.
     * <p>
     * In case a skin pack with this name already exists in the given category,
     * this method will return null, and no pack will be created.
     *
     * @see SkinPack
     * @param category the category to put the skin pack in
     * @param name the name of the skin pack
     * @return the newly created {@link SkinPack}, or null if it already exists.
     */

    @Nullable
    SkinPack createSkinPack(String category, String name);

    /**
     * This method deletes the specified {@link SkinPack}.
     * <p>
     * If this pack was already loaded (meaning that it has a file saved locally),
     * the root folder will also be deleted.
     *
     * @see SkinPack#getFile()
     * @param pack the skin pack to delete
     */

    void deleteSkinPack(SkinPack pack);

    /**
     * This method retrieves a {@link SkinPack} with the specified
     * name cached in the specified category.
     * <p>
     * It is much more preferred to use this method than {@link #getSkinPack(String)}.
     *
     * @see SkinFactory#getSkinPack(String)
     * @param category the name of the category
     * @param name the name of the skin pack
     * @return the {@link SkinPack} with the given name, or null if not found.
     */

    @Nullable
    SkinPack getSkinPack(String category, String name);

    /**
     * This method retrieves a {@link SkinPack} with
     * the specified name.
     * <p>
     * <b>Downside</b> of this method is that different categories
     * can have skin packs with the same name, so searching
     * with this method might not return the correct {@link SkinPack}.
     *
     * @see #getSkinPack(String, String)
     * @param name the name of the skin pack
     * @return the {@link SkinPack} with the given name, or null if not found.
     */

    @Nullable
    SkinPack getSkinPack(String name);

    /**
     * This method returns a collection of skin packs
     * listed within the specified category.
     * <p>
     * If this category doesn't exist, or doesn't contain any skin packs
     * within it, an empty {@link java.util.ArrayList} will be returned.
     *
     * @param category the category
     * @return a collection of skin packs
     */

    List<? extends SkinPack> getSkinPacks(String category);

    /**
     * This method returns a collection of all
     * skin packs cached inside this factory.
     * <p>
     * In case no skin packs are cached within this factory,
     * an empty {@link java.util.ArrayList} will be returned, rather than null.
     *
     * @return a collection of skin packs
     */

    List<? extends SkinPack> getSkinPacks();

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

    /**
     * This method returns a random {@link Skin} picked
     * from all cached skin packs.
     * <p>
     * In case no skin packs are cached, and therefore no cached skins exist,
     * this method will return null.
     *
     * @return a {@link Skin} if it can be found
     */

    @Nullable
    Skin getRandomSkin();

    /**
     * This method returns a {@link Set} of categories
     * where all individual {@link SkinPack}'s are stored.
     *
     * @return the set of categories
     */

    Set<String> getCategories();

    /**
     * This method is used to download the skin packs from both
     * the local machine and a database where base skins are located.
     * <p>
     * Depending on the "downloadBaseSkins" value in the config.yml,
     * the base skins are downloaded from the said database.
     * <br>
     * The link for viewing the base <a href="https://github.com/itspinger/Skins/tree/master/SkinPacks">skins</a>
     *
     * @throws DownloadFailedException if an error occurred while downloading these skins
     */

    void downloadSkins() throws DownloadFailedException;

    /**
     * This method is used to save currently cached skins
     * into the local machine.
     * <p>
     * The saving path is <code>/categories/%category_name%/%pack_name%/pack.json</code>
     *
     * @throws SaveFailedException if an error occurred while saving these skins
     */

    void saveSkins() throws SaveFailedException;

    File getFile();

    File getCategoriesFile();
}
