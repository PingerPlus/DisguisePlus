package net.pinger.disguiseplus.exception;

import net.pinger.disguiseplus.skin.SkinFactory;

/**
 * This is an exception which gets thrown upon failing to download skins
 * from either the server or the local files.
 *
 * @see SkinFactory
 * @author Pinger
 * @since 2.0
 */

public class DownloadFailedException extends RuntimeException {

	public DownloadFailedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
