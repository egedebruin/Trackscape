package gui.controllers;

import handlers.CameraHandler;

public abstract class Controller {

    private static CameraHandler cameraHandler;

    public abstract void closeController();

    public abstract void update(long now);

    public static CameraHandler getCameraHandler() {
        return cameraHandler;
    }

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
}