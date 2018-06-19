package handlers;

import static java.lang.Math.toIntExact;


import api.APIHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import room.Chest;
import room.Room;

import java.io.BufferedReader;
import java.io.File;
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
    public List<String> getCameraLinks(final long roomId) {
        JSONObject room = getRoomById(roomId);
        List<String> cameras = new ArrayList<>();
        JSONArray array = (JSONArray) room.get("cameras");
        if (array == null) {
            return new ArrayList<>();
        }
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
    public int getAmountPeople(final long roomId) {
        JSONObject room = getRoomById(roomId);
        Object res = room.get("people");
        if (res == null) {
            return 0;
        }
        long amount = (long) res;
        return toIntExact(amount);
    }

    /**
     * Get the target duration of the room.
     * @param roomId The id of the room.
     * @return The target duration of the room
     */
    public int getTargetDuration(final long roomId) {
        JSONObject room = getRoomById(roomId);
        Object res = room.get("targetDuration");
        if (res == null) {
            return 0;
        }
        long amount = (long) res;
        return toIntExact(amount);
    }

    /**
     * Get the port the server needs to listen to.
     * @param roomId The id of the room.
     * @return The port
     */
    public int getPortNumber(final long roomId) {
        JSONObject room = getRoomById(roomId);
        Object res = room.get("port");
        if (res == null) {
            return APIHandler.DEFAULT_PORT;
        }
        long amount = (long) res;
        return toIntExact(amount);
    }

    /**
     * Get the Json element of a room.
     * @param roomId The id of the room.
     * @return The Json element of the room.
     */
    private JSONObject getRoomById(final long roomId) {
        for (Object o : jsonElement) {
            JSONObject room = (JSONObject) o;
            if ((long) room.get("roomId") == roomId) {
                return room;
            }
        }
        return null;
    }

    /**
     * Create rooms from the JSON file.
     * @return List of rooms from the JSON file.
     */
    public List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>();
        for (Object o : jsonElement) {
            JSONObject roomObject = (JSONObject) o;
            int roomId = toIntExact((long) roomObject.get("roomId"));
            int amountPeople = getAmountPeople(roomId);
            int targetDuration = getTargetDuration(roomId);
            List<String> cameraLinks = getCameraLinks(roomId);
            List<Chest> chests = createChests(roomId);
            int port = getPortNumber(roomId);
            Room room = new Room(roomId, amountPeople, cameraLinks, chests, targetDuration, port);
            rooms.add(room);
        }
        return rooms;
    }

    /**
     * Create the first room from the JSON file.
     * @return the first room from the JSON file.
     */
    public Room createSingleRoom() {
        if (!jsonElement.isEmpty()) {
            JSONObject roomObject = (JSONObject) jsonElement.get(0);
            int roomId = toIntExact((long) roomObject.get("roomId"));
            int amountPeople = getAmountPeople(roomId);
            int targetDuration = getTargetDuration(roomId);
            List<String> cameraLinks = getCameraLinks(roomId);
            List<Chest> chests = createChests(roomId);
            int port = getPortNumber(roomId);
            return new Room(roomId, amountPeople, cameraLinks, chests, targetDuration, port);
        }
        return new Room(0, 0, new ArrayList<>(), null, 0, APIHandler.DEFAULT_PORT);
    }

    /**
     * Create chests from the room from the JSON file.
     * @param roomId The id of the room.
     * @return List of chests in the room.
     */
    public List<Chest> createChests(final long roomId) {
        JSONObject room = getRoomById(roomId);
        List<Chest> chests = new ArrayList<>();
        JSONArray array = (JSONArray) room.get("chests");
        if (array == null) {
            return new ArrayList<>();
        }
        int totalDuration = 0;
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            int sections = toIntExact((long) object.get("sections"));
            int targetDuration = toIntExact((long) object.get("targetDuration"));
            totalDuration += targetDuration;
            Chest chest = new Chest(sections, totalDuration);
            chests.add(chest);
        }
        return chests;
    }

    /**
     * Get the json element.
     * @return The json element.
     */
    public JSONArray getJsonElement() {
        return jsonElement;
    }

    /**
     * Method that counts the amount of chest in a specific room.
     * @param roomid the id of the room where the amount of present chest is wanted
     * @return  the amount of chests in the room with id: roomid
     */
    public int getAmountChests(final int roomid) {
        return createChests(roomid).size();
    }


}
