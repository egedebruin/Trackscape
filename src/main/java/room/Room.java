package room;

import camera.Camera;

import java.util.ArrayList;

/**
 * Class describing a room as found in the config file.
 */
public class Room {
    private int id;
    private ArrayList<Chest> chestList;
    private ArrayList<Camera> cameraList;
    private int numberOfPeople;
    private long startTime;
}
