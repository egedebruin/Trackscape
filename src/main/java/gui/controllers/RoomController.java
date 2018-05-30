package gui.controllers;

import handlers.CameraHandler;
import handlers.JsonHandler;
import room.Room;

/**
 * Controller for the gui with a room.
 */
public class RoomController {

    private Room room;
    private CameraHandler cameraHandler;

    /**
     * Constructor for RoomController, creates a new camerahandler depending on the config.
     * @param configFile The configfile for this room.
     */
    public RoomController(String configFile) {
        JsonHandler jsonHandler = new JsonHandler(configFile);
        room = jsonHandler.createSingleRoom();
        cameraHandler = room.getCameraHandler();
    }

    /**
     * Close the controller when the stream is closed.
     */
    public void closeController() {

    }

    /**
     * Get the camera handler.
     * @return The camera handler.
     */
    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }
}
