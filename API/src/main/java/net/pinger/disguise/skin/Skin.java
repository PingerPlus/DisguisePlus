package net.pinger.disguise.skin;

public interface Skin {

    /**
     * Returns the value of this skin.
     *
     * @return the value
     */

    String getValue();

    /**
     * Returns the signature of this skin.
     *
     * @return the signature
     */

    String getSignature();

    /**
     * Transform this skin into a property object based on the current version of the server.
     *
     * @return the property
     */

    Object toProperty();



}
