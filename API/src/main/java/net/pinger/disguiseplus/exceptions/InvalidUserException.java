package net.pinger.disguiseplus.exceptions;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException() {
        super("Couldn't find the specified username");
    }

}
