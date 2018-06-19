package gui.controllers;

import handlers.CameraHandler;

/**
 * Abstract class for a controller.
 */
public abstract class Controller {

    private static CameraHandler cameraHandler;
    private static int currentRoomId = -1;

    /**
     * Method to close the controller.
     */
    public abstract void closeController();

    /**
     * Method which will be called every frame.
     * @param now the time of the call
     */
    public abstract void update(long now);

    /**
     * Get the cameraHandler, there is only one for all the controllers.
     * @return the cameraHandler
     */
    public static CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    /**
     * Set a new cameraHandler.
     * @param newHandler the new cameraHandler
     */
    public static void setCameraHandler(final CameraHandler newHandler) {
        Controller.cameraHandler = newHandler;
    }

    /**
     * Get the number of active cameras.
     * @return number of cameras
     */
    public int getCameras() {
        return cameraHandler.listSize();
    }

    /**
     * Get the current roomId.
     * @return the roomId
     */
    public static int getCurrentRoomId() {
        return currentRoomId;
    }

    /**
     * Set a new roomId.
     * @param newRoomId the new roomId
     */
    public static void setCurrentRoomId(final int newRoomId) {
        Controller.currentRoomId = newRoomId;
    }
}