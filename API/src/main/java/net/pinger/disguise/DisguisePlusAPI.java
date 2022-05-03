package net.pinger.disguise;

import net.pinger.disguise.packet.PacketProvider;

public class DisguisePlusAPI {

    private static Disguise disguise;

    /**
     * Cancel initialization of this class
     */

    private DisguisePlusAPI() {}

    /**
     * Sets the instance of the plugin that will be responsible
     * for handling the api methods.
     *
     * @param disguise the plugin
     */

    public static void setDisguise(Disguise disguise) {
        if (DisguisePlusAPI.disguise != null) {
            throw new IllegalArgumentException("Disguise has already been initialized");
        }

        DisguisePlusAPI.disguise = disguise;
    }

    /**
     * Returns the packet that corresponds to the version of the server.
     *
     * @return the packet
     */

    public static PacketProvider<?> getPacketProvider() {
        return disguise.getPacketProvider();
    }

    /**
     * Returns the holder of all skins.
     *
     * @return the skin factory
     */

    public static SkinFactory getSkinFactory() {
        return disguise.getSkinFactory();
    }

    /**
     * Returns the {@link DisguiseManager} instance.
     *
     * @return the disguise manager
     */

    public static DisguiseManager getDisguiseManager() {
        return disguise.getManager();
    }


}
