package net.pinger.disguise.exceptions;

public class RateLimitedException extends RuntimeException {

    public RateLimitedException(String message) {
        super(message);
    }

    public RateLimitedException(Throwable t) {
        super(t);
    }

    public RateLimitedException(String message, Throwable t) {
        super(message, t);
    }

}
