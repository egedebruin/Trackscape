package handlers;

import camera.Camera;
import camera.CameraActivity;
import camera.CameraChestDetector;
import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.nanoTime;

/**
 * Class for handling the cameras. Holds a list of the cameras en controls it.
 */
public class CameraHandler {

    /**
     * The list of the cameras.
     */
    private List<Camera> cameraList = new ArrayList<>();
    private InformationHandler informationHandler;
    private CameraChestDetector cameraChestDetector = new CameraChestDetector();
    private boolean allChestsDetected = false;
    private long beginTime = -1;
    private boolean chestFound = false;

    /**
     * Constructor for CameraHandler without specified information handler.
     */
    public CameraHandler() {
        informationHandler = new InformationHandler();
    }

    /**
     * Constructor for the CameraHandler class with specified information handler.
     * @param information The informationHandler.
     */
    public CameraHandler(final InformationHandler information) {
        informationHandler = information;
    }

    /**
     * Add a new camera to use for streaming.
     *
     * @param link The link of the camera.
     * @return The new camera as a Camera object.
     */
    public Camera addCamera(final String link) {
        Camera cam = addCamera(link, -1);
        cameraChestDetector.addCameraToDetector(cam);
        return cam;
    }

    /**
     * Add a new camera to use for streaming.
     *
     * @param link The link of the camera.
     * @param chests Amount of chests.
     * @return The new camera.
     */
    public Camera addCamera(final String link, final int chests) {
        VideoCapture videoCapture = new VideoCapture();
        boolean opened = videoCapture.open(link);
        if (!opened) {
            return null;
        }
        Camera camera;
        if (chests == -1) {
            camera = new Camera(videoCapture, link);
        } else {
            camera = new Camera(videoCapture, link, chests);
        }
        cameraList.add(camera);
        return camera;
    }

    /**
     * Get new frames from the cameras.
     *
     * @return The new frames as a list of Mat.
     */
    public List<Mat> processFrames() {
        List<Mat> frames = new ArrayList<>();
        for (Camera camera : cameraList) {
            Mat newFrame = camera.getLastFrame();
            if (camera.getFirstFrame() == null) {
                camera.setFirstFrame(newFrame);
            }

            final int frequency = 10;
            if (camera.getFrameCounter() % frequency == 0) {
                Thread thread = new Thread(() -> processFrame(camera, newFrame));
                thread.start();
            }
            frames.add(newFrame);
        }

        return frames;
    }

    /**
     * Do all the calculations on the frame.
     * @param camera The camera of the frame.
     * @param newFrame The new frame.
     */
    public void processFrame(final Camera camera, final Mat newFrame) {
        CameraActivity activity = camera.getActivity();
        activity.divideFrame(newFrame);

        activity.addActivities(newFrame, camera.getFrameCounter());
        if (activity.getLastActivity() > 2 && beginTime == -1) {
            beginTime = nanoTime();
            informationHandler.addInformation("Detected activity");
        }

        Mat subtraction = cameraChestDetector.subtractFrame(newFrame);

        final int firstDetection = 100;
        if (camera.getFrameCounter() > firstDetection) {
            List<Mat> mats = cameraChestDetector.
                checkForChests(newFrame, camera, subtraction);
            chestFound = mats.size() > 0;
            for (Mat mat : mats) {
                Pair<Mat, Long> tuple = new Pair<>(mat, nanoTime());
                informationHandler.addMatrix(tuple);
            }
        }
    }

    /**
    * Get the size of the list of cameras.
    *
    * @return The size of the list.
    */
    public int listSize() {
        return cameraList.size();
    }

    /**
     * Clear the list of cameras and chest detected.
     */
    public void clearLists() {
        cameraList.clear();
    }

    /**
     * Close the handler by clearing lists and set active to false.
     */
    public void closeHandler() {
        clearLists();
        beginTime = -1;
    }

    /**
     * Get a camera from the list.
     *
     * @param index The index of the camera.
     * @return The camera.
     */
    public Camera getCamera(final int index) {
        return cameraList.get(index);
    }

    /**
     * Get the information Handler.
     * @return The information handler.
     */
    public InformationHandler getInformationHandler() {
        return informationHandler;
    }

    /**
     * Returns whether a camera is changed or not.
     * @return True if all cameras are changed, false otherwise.
     */
    public boolean isChanged() {
        for (Camera camera : cameraList) {
            if (!camera.isChanged()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set a new information handler.
     * @param handler The new information handler.
     */
    public void setInformationHandler(final InformationHandler handler) {
        this.informationHandler = handler;
    }

    /**
     * Get the begin time of the current CameraHandler.
     * @return The begin time in nano seconds.
     */
    public long getBeginTime() {
        return beginTime;
    }

    /**
     * Set allChestsDetected on true when all chests have been detected.
     * @param detectedAllChests the boolean value about whether all chests are detected
     */
    public void setAllChestsDetected(final boolean detectedAllChests) {
        this.allChestsDetected = detectedAllChests;
    }

    /**
     * See if all chests are detected.
     * @return allChestsDetected
     */
    public boolean areAllChestsDetected() {
        return allChestsDetected;
    }

    /**
     * Check if a chest is found (actually only for ChestDetectorTest).
     * @return True if chest is found, false otherwise
     */
    public boolean isChestFound() {
        return chestFound;
    }
}
