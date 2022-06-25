package net.pinger.disguiseplus.exceptions;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String message) {
        super(message);
    }

    public InvalidUrlException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
