package gui.controllers;

import handlers.CameraHandler;
import room.Progress;

/**
 * Controller for the gui with a room.
 */
public class RoomController {

    private Progress progress;
    private CameraHandler cameraHandler;

    /**
     * Constructor for RoomController, creates a new camerahandler depending on the config.
     * @param configFile The configfile for this room.
     */
    public RoomController(final String configFile) {
        progress = new Progress(configFile);
        cameraHandler = progress.getRoom().getCameraHandler();
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
