package gui.controllers;

import camera.Camera;
import handlers.CameraHandler;
import handlers.JsonHandler;
import room.Room;

public class RoomController {

    private Room room;
    private CameraHandler cameraHandler;

    public RoomController(String configFile) {
        JsonHandler jsonHandler = new JsonHandler(configFile);
        room = jsonHandler.createSingleRoom();
        cameraHandler = room.getCameraHandler();
    }

    public void closeController() {

    }

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }
}
