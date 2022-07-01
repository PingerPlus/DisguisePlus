package net.pinger.disguiseplus.skin;

import com.google.gson.JsonObject;

import java.util.UUID;

public class Skin {

    /**
     * The uuid of the skin returned from the player name
     */

    private final UUID id;

    /**
     * The name of the user
     */

    private final String name;

    /**
     * The value of the skin
     */

    private final String value;

    /**
     * The signature value of the skin
     */

    private final String signature;

    /**
     * Creates a new instance of the {@link Skin} class.
     * None of these values should ever be null and that is checked outside of this method.
     *
     * @param id the id of the skin
     * @param name the name of the skin user
     * @param value the value of the skin
     * @param signature the signature of the skin
     */

    public Skin(UUID id, String name, String value, String signature) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public UUID getUniqueId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * Builds a new {@link JsonObject} out of properties of this object.
     * @return the json object
     */

    public JsonObject toJsonObject() {
        JsonObject object = new JsonObject();
        if (this.id != null )
            object.addProperty("uuid", this.id.toString());

        if (this.name != null)
            object.addProperty("name", this.name);

        JsonObject properties = new JsonObject();
        properties.addProperty("value", this.value);
        properties.addProperty("signature", this.signature);
        object.add("properties", properties);

        return object;
    }
}
