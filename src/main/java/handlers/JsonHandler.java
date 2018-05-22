package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling Json config files.
 */
public class JsonHandler {

    /**
     * Class variables.
     */
    private JsonElement jsonElement;

    /**
     * Constructor for JsonHandler.
     * @param fileName The name of the json config file.
     */
    public JsonHandler(String fileName) {
        File file = new File(fileName);
        JsonParser parser = new JsonParser();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            jsonElement = parser.parse(reader);
        } catch (FileNotFoundException e) {
            jsonElement = null;
        }
    }

    /**
     * Get the links to the cameras of the room.
     * @param roomId The Id of the room.
     * @return List of the links to the cameras.
     */
    public List<String> getCameraLinks(int roomId) {
        JsonElement room = getRoomById(roomId);
        List<String> cameras = new ArrayList<>();
        JsonElement element = room.getAsJsonObject().get("cameras");
        for (JsonElement camera : element.getAsJsonArray()) {
            String link = camera.getAsJsonObject().get("link").getAsString();
            cameras.add(link);
        }
        return cameras;
    }

    /**
     * Get the amount of people in the room.
     * @param roomId The id of the room.
     * @return The amount of people.
     */
    public int getAmountPeople(int roomId) {
        JsonElement room = getRoomById(roomId);
        return room.getAsJsonObject().get("people").getAsInt();
    }

    /**
     * Get the amount of chests in the room.
     * @param roomId The id of the room.
     * @return The amount of chests.
     */
    public int getAmountChests(int roomId) {
        JsonElement room = getRoomById(roomId);
        return room.getAsJsonObject().get("chests").getAsInt();
    }

    /**
     * Get the Json element of a room.
     * @param roomId The id of the room.
     * @return The Json element of the room.
     */
    private JsonElement getRoomById(int roomId) {
        JsonArray array = jsonElement.getAsJsonArray();
        for (JsonElement room : array) {
            if (room.getAsJsonObject().get("roomId").getAsInt() == roomId) {
                return room;
            }
        }
        return null;
    }

    /**
     * Get the json element.
     * @return The json element.
     */
    public JsonElement getJsonElement() {
        return jsonElement;
    }
}
