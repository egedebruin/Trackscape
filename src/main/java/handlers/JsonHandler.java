package handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Class for handling Json config files.
 */
public class JsonHandler {

    /**
     * Class variables.
     */
    private JSONArray jsonElement;

    /**
     * Constructor for JsonHandler.
     * @param fileName The name of the json config file.
     */
    public JsonHandler(final String fileName) {
        File file = new File(fileName);
        JSONParser parser = new JSONParser();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            jsonElement = (JSONArray) parser.parse(reader);
        } catch (Exception e) {
            jsonElement = null;
        }
    }

    /**
     * Get the links to the cameras of the room.
     * @param roomId The Id of the room.
     * @return List of the links to the cameras.
     */
    public List<String> getCameraLinks(final int roomId) {
        JSONObject room = getRoomById(roomId);
        List<String> cameras = new ArrayList<>();
        JSONArray array = (JSONArray) room.get("cameras");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            cameras.add((String) object.get("link"));
        }
        return cameras;
    }

    /**
     * Get the amount of people in the room.
     * @param roomId The id of the room.
     * @return The amount of people.
     */
    public int getAmountPeople(final int roomId) {
        JSONObject room = getRoomById(roomId);
        long amount = (long) room.get("people");
        int result = Math.toIntExact(amount);
        return result;
    }

    /**
     * Get the amount of chests in the room.
     * @param roomId The id of the room.
     * @return The amount of chests.
     */
    public int getAmountChests(final int roomId) {
        JSONObject room = getRoomById(roomId);
        long amount = (long) room.get("chests");
        int result = Math.toIntExact(amount);
        return result;
    }

    /**
     * Get the Json element of a room.
     * @param roomId The id of the room.
     * @return The Json element of the room.
     */
    private JSONObject getRoomById(final int roomId) {
        for (Object o : jsonElement) {
            JSONObject room = (JSONObject) o;
            if ((long) room.get("roomId") == roomId) {
                return room;
            }
        }
        return null;
    }

    /**
     * Get the json element.
     * @return The json element.
     */
    public JSONArray getJsonElement() {
        return jsonElement;
    }
}
