package net.pinger.disguise.exceptions;

public class SkinCloudDownloadException extends RuntimeException {

    public SkinCloudDownloadException() {
        super("Failed to load skins directly from the cloud.");
    }

}
