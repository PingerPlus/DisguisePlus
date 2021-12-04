package net.pinger.disguise.exceptions;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String message) {
        super(message);
    }

    public InvalidUrlException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
