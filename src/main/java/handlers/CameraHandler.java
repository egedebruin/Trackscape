package handlers;

import camera.Camera;
import camera.CameraActivity;
import camera.CameraChestDetector;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

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
    private boolean active = false;
    private ArrayList<Boolean> chestDetected = new ArrayList<>();

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
        VideoCapture videoCapture = new VideoCapture(link);
        boolean opened = videoCapture.open(link);
        if (!opened) {
            return null;
        }
        Camera camera = new Camera(videoCapture, link);
        cameraList.add(camera);
        informationHandler.addInformation("Added camera");
        return camera;
    }

    /**
     * Add a new camera to use for streaming.
     *
     * @param link The link of the camera.
     * @param chests Amount of chests.
     */
    public void addCamera(final String link, final int chests) {
        VideoCapture videoCapture = new VideoCapture(link);
        boolean opened = videoCapture.open(link);
        if (!opened) {
            return;
        }
        Camera camera = new Camera(videoCapture, link, chests);
        cameraList.add(camera);
        informationHandler.addInformation("Added camera");
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
                processFrame(camera, newFrame);
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
        if (activity.getLastActivity() > 2) {
            active = true;
        }

        Mat subtraction = cameraChestDetector.subtractFrame(newFrame);

        final int firstDetection = 100;
        if (camera.getFrameCounter() > firstDetection) {
            // Put true or false in the chestdetected arraylist on index cameraindex depending
            // on whether a chest is detected or not.
            if (chestDetected.size() > cameraList.indexOf(camera)) {
                chestDetected.set(cameraList.indexOf(camera), cameraChestDetector.
                    checkForChests(newFrame, camera.getNumOfChestsInRoom(), subtraction));
            } else {
                // If the camera is new, add a index position for it to the arraylist
                chestDetected.add(cameraList.indexOf(camera), cameraChestDetector.
                    checkForChests(newFrame, camera.getNumOfChestsInRoom(), subtraction));
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
     * Clear the list of cameras.
     */
    public void clearList() {
        cameraList.clear();
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
     * Returns if there is activity in these cameras.
     * @return True if there is activity, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Loop through the isChestdetected arraylist to check if there is at least 1 chest detected.
     * @return true if there is at least 1 chest detected, false otherwise
     */
    public boolean isChestDetected() {
        return chestDetected.contains(true);
    }

    /**
     * Set the active variable to true or false.
     * @param newActive the new value for active.
     */
    public void setActive(final boolean newActive) {
        this.active = newActive;
    }

    /**
     * Get the information Handler.
     * @return The information handler.
     */
    public InformationHandler getInformationHandler() {
        return informationHandler;
    }

    /**
     * Returns weather a camera is changed or not.
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
}
