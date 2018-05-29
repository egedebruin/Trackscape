package gui;

import handlers.CameraHandler;
import handlers.JsonHandler;
import room.Room;

public class RoomController extends Controller{

    private CameraHandler cameraHandler;
    private Room room;

    RoomController(String configFile){
        super();
        JsonHandler jsonHandler = new JsonHandler(configFile);
        room = jsonHandler.createSingleRoom();
        
        cameraHandler = room.getCameraHandler();

    }
}
