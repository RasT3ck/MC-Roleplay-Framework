package org.rast3ck.mcrp.core.data.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.rast3ck.mcrp.core.data.IDataStorage;
import org.rast3ck.mcrp.core.data.PlayerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class JsonDataStorage implements IDataStorage {

    private final Path dataDirectory;

    public JsonDataStorage(Path worldDirectory) {
        this.dataDirectory = worldDirectory.resolve("mcrp").resolve("player-data");
    }

    @Override
    public PlayerData load(UUID playerId) {

        Path file = getFile(playerId);

        if (!Files.exists(file)) {
            return new PlayerData(playerId);
        }

        try {
            JsonObject json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            PlayerData data = new PlayerData(playerId);
            if (json.has("money")) {
                data.setMoney(json.get("money").getAsDouble());
            }
            return data;
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("Could not load player data for " + playerId, e);
        }
    }


    @Override
    public void save(UUID playerId, PlayerData data) {

        try {
            Files.createDirectories(dataDirectory);

            JsonObject json = new JsonObject();
            json.addProperty("money", data.getMoney());
            Files.writeString(getFile(playerId), json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not save player data for " + playerId, e);
        }
    }


    @Override
    public void delete(UUID playerId) {

        try {
            Files.deleteIfExists(getFile(playerId));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete player data for " + playerId, e);
        }
    }

    private Path getFile(UUID playerId) {
        return dataDirectory.resolve(playerId + ".json");
    }
}