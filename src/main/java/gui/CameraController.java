package gui;

import handlers.CameraHandler;

/**
 * Controller for Camera related methods.
 */
public class CameraController {
    /**
     * Class parameters.
     */
    private CameraHandler cameraHandler;

    /**
     * Constructor for CameraController.
     * @param camHandler the cameraHandler
     */
    public CameraController(final CameraHandler camHandler) {
        this.cameraHandler = camHandler;
    }
}
