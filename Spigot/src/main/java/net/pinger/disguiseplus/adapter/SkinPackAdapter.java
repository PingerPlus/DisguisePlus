package net.pinger.disguiseplus.adapter;

import com.google.gson.*;
import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlusAPI;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.utils.SkinUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SkinPackAdapter implements JsonSerializer<SkinPack>, JsonDeserializer<SkinPack> {

	/**
	 * This is a deserialization strategy for retrieving a {@link SkinPack} instance.
	 *
	 * @param jsonElement the jsonElement to transform
	 * @param type the type of the {@link SkinPack}
	 * @param jsonDeserializationContext the deserialization context
	 * @return the new {@link SkinPack}
	 * @throws JsonParseException if an error occurred while retrieving values
	 */

	@Override
	public SkinPack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		// Get the object as json
		JsonObject object = jsonElement.getAsJsonObject();

		// Get default values such as custom, name, strategy, skins
		boolean custom = object.has("custom") && object.get("custom").getAsBoolean();
		String name = object.get("name").getAsString();
		String category = object.get("category").getAsString();

		// Extract skins from the array
		List<Skin> skins = new ArrayList<>();
		for (JsonElement element : object.getAsJsonArray("skins")) {
			// Add the skins from the file
			skins.add(SkinUtil.getFromJson(element.getAsJsonObject()));
		}

		return DisguisePlusAPI.getSkinFactory().createSkinPack(category, name, skins, custom);
	}

	/**
	 * This is a serialization strategy for the {@link SkinPack} class.
	 * <p>
	 * The current .json format for serializing skin packs looks like
	 * <pre>
	 * {
	 *   "custom": true,
	 *   "name": "someName",
	 *   "category": "someCategory",
	 *   "skins": [
	 *      {
	 *       "properties": {
	 *         "value": "someValue",
	 *         "signature": "someSignature"
	 *       }
	 *     }
	 *   ]
	 * }
	 * </pre>
	 *
	 * @param skins the skin pack to serialize
	 * @param type the type of the skin pack
	 * @param jsonSerializationContext the context of the serialization
	 * @return the serialized element
	 */

	@Override
	public JsonElement serialize(SkinPack skins, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject object = new JsonObject();

		// Add default properties
		object.addProperty("custom", skins.isCustomSkinPack());
		object.addProperty("name", skins.getName());
		object.addProperty("category", skins.getCategory());
		object.add("skins", skins.toJsonArray());

		// Return the given object
		return object;
	}
}
