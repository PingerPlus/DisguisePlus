package net.pinger.disguise;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * This class represents a factory holder for every skin that gets loaded
 * within the {@link Disguise} plugin.
 * <p>
 * Essentially, each skin that gets loaded belongs to a unique {@link SkinPack},
 * and each {@link SkinPack} belongs to a unique category.
 *
 * Apart from this, do note that two skin packs can have the same name in two different categories, which is why
 * it is preferred to use {@link #getSkinPack(String, String)} where you need to specify the
 * category name, rather than {@link  #getSkinPack(String)}
 *
 * @since 2.0
 * @author Pinger
 */

public interface SkinFactory {

    /**
     * This method is used to retrieve a random skin
     * from all the packs that can be found in the {@link #getSkinPacks()} method.
     * <p>
     * The retrieved skin can never be null, as we make sure that
     * there is at least one backup skin at all times.
     *
     * @return the random skin
     */

    @Nonnull
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

    @Nullable
    List<? extends SkinPack> getSkinPacks(String category);

    /**
     * This method is used to retrieve all skin packs
     * combined into a single list.
     *
     * @return the skin packs
     */

    List<? extends SkinPack> getSkinPacks();
}
