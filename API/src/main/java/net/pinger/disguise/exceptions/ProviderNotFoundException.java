package net.pinger.disguise.exceptions;

public class ProviderNotFoundException extends Exception {

    public ProviderNotFoundException() {
        super("An applicable Provider has been not been found for this version");
    }

}
