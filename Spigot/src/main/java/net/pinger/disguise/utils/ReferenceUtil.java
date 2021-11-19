package net.pinger.disguise.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

public class ReferenceUtil {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();
}
