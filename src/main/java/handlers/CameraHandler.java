package handlers;

import camera.Camera;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CameraHandler {

    private List<Camera> cameraList;

    /**
     * Constructor for the CameraHandler class.
     */
    public CameraHandler() {
        cameraList = new ArrayList<>();
    }

    /**
     * Add a new camera to use for streaming.
     * @param link The link of the camera.
     * @return The new camera as a Camera object.
     */
    public Camera addCamera(String link) {
        VideoCapture videoCapture = new VideoCapture(link);
        boolean opened = videoCapture.open(link);
        if (!opened) {
            return null;
        }
        Camera camera = new Camera(videoCapture, link);
        cameraList.add(camera);
        return camera;
    }

    /**
     * Get a new frame from the camera.
     * @param camera The camera to get the new frame from.
     * @return The new frame as a BufferedImage.
     */
    public Mat getNewFrame(Camera camera) {
        return camera.getLastFrame();
    }

    public int listSize() {
        return cameraList.size();
    }

    public void clearList() {
        cameraList.clear();
    }

    public Camera getCamera(int index) {
        return cameraList.get(index);
    }

}
