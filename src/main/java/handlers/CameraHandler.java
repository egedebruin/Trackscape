package handlers;

import camera.Camera;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * Class for handling the cameras. Holds a list of the cameras en controls it.
 */
public class CameraHandler {

    /**
     * The list of the cameras.
     */
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
        Mat newFrame = camera.getLastFrame();
        if (camera.getFirstFrame() == null) {
            camera.setFirstFrame(newFrame);
        }
        return getSubtraction(newFrame, camera);
    }

    public Mat getSubtraction(Mat frame, Camera camera) {
        Mat background = camera.getFirstFrame();
        Mat result = frame.clone();
        ArrayList list = new ArrayList();
        IntStream.range(0, background.cols()).parallel().forEach(i ->
            IntStream.range(0, background.rows()).parallel().forEach(j -> {
            boolean different = false;
            for (int k = 0; k < background.get(j, i).length; k++) {
                if (Math.abs(background.get(j, i)[k] - frame.get(j, i)[k]) > 20) {
                    different = true;
                }
            }
            if (!different) {
                result.put(j, i, 0, 0, 0);
            }
        }));
        return result;
    }

    /**
     * Get the size of the list of cameras.
     * @return The size of the list.
     */
    public int listSize() {
        return cameraList.size();
    }

    /**
     * Clear the list of cameras.
     */
    public void clearList() {
        cameraList.clear();
    }

    /**
     * Get a camera from the list.
     * @param index The index of the camera.
     * @return The camera.
     */
    public Camera getCamera(int index) {
        return cameraList.get(index);
    }

}
