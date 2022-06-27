package net.pinger.disguiseplus.exception;

public class SaveFailedException extends RuntimeException {

	public SaveFailedException(String message) {
		super(message);
	}

	public SaveFailedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
