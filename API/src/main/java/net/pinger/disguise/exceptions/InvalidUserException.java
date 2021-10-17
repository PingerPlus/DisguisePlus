package net.pinger.disguise.exceptions;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException() {
        super("Couldn't find the specified username");
    }

}
