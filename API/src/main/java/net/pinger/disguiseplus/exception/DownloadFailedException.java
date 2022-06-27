package net.pinger.disguiseplus.exception;

/**
 * This is an exception which gets thrown upon failing to download skins
 * from either the server or the local files.
 *
 * @see net.pinger.disguiseplus.SkinFactory
 * @author Pinger
 * @since 2.0
 */

public class DownloadFailedException extends RuntimeException {

	public DownloadFailedException(String message) {
		super(message);
	}

	public DownloadFailedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
