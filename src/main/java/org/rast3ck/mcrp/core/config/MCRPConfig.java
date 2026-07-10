package org.rast3ck.mcrp.core.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MCRPConfig implements ConfigFile {

    private static final Path CONFIG_FILE = Path.of("config", "mcrp.json").toAbsolutePath().normalize();

    public static boolean DEBUG = true;
    public static int SALARY_INTERVAL_MINUTES = 60;


    @Override
    public String getName() {

        return "mcrp";

    }


    @Override
    public void load() {

        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try {
            String content = Files.readString(CONFIG_FILE, StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            if (json.has("debug")) {
                DEBUG = json.get("debug").getAsBoolean();
            }
            if (json.has("salaryIntervalMinutes")) {
                SALARY_INTERVAL_MINUTES = Math.max(1, json.get("salaryIntervalMinutes").getAsInt());
            }
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("Could not load MCRP configuration", e);
        }
    }


    @Override
    public void save() {

        try {
            Files.createDirectories(CONFIG_FILE.getParent());

            JsonObject json = new JsonObject();
            json.addProperty("debug", DEBUG);
            json.addProperty("salaryIntervalMinutes", SALARY_INTERVAL_MINUTES);
            Files.writeString(CONFIG_FILE, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not save MCRP configuration", e);
        }
    }

}
