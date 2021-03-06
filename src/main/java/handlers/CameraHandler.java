package handlers;

import camera.Camera;
import camera.CameraActivity;
import javafx.animation.AnimationTimer;
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
     * Enum for the activity.
     */
    enum Activity {
        ZERO, LOW, MEDIUM, HIGH
    }

    private static final int ACTIVITY_THRESHOLD = 5;
    private static final int FIRST_DETECTION = 80;

    private List<Camera> cameraList = new ArrayList<>();
    private InformationHandler informationHandler;
    private boolean allChestsDetected = false;
    private long beginTime = -1;
    private boolean chestFound = false;
    private Activity active = Activity.ZERO;
    private List<AnimationTimer> timers = new ArrayList<>();

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
        return addCamera(link, -1);
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

        if (link.startsWith("rtsp")) {
            streamTimer(camera);
        }
        return camera;
    }

    /**
     * Create a temporary timer for a stream.
     * @param camera the camera
     */
    private void streamTimer(final Camera camera) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                camera.loadFrame();
            }
        };
        timer.start();
        timers.add(timer);
    }

    /**
     * Get new frames from the cameras.
     *
     * @return The new frames as a list of Mat.
     */
    public List<Mat> processFrames() {
        final int frequency = 10;
        clearTimers();

        List<Mat> frames = new ArrayList<>();
        for (Camera camera : cameraList) {
            Mat newFrame = camera.getLastFrame();
            if (camera.getFirstFrame() == null) {
                camera.setFirstFrame(newFrame);
            }

            if (camera.getFrameCounter() % frequency == 0) {
                Thread thread = new Thread(() -> processFrame(camera, newFrame));
                thread.start();
            }
            frames.add(newFrame);
        }

        if (active != Activity.ZERO) {
            changeActivity();
        }
        return frames;
    }

    /**
     * Stop and clear all timers.
     */
    private void clearTimers() {
        for (AnimationTimer timer : timers) {
            timer.stop();
        }
        timers.clear();
    }

    /**
     * Do all the calculations on the frame.
     * @param camera The camera of the frame.
     * @param newFrame The new frame.
     */
    private void processFrame(final Camera camera, final Mat newFrame) {

        // First calculate the activity and set it accordingly in camera
        // if enough activity is found the escape room gets started
        // this is done by setting cameras on active (activity per camera gets started)
        processActivity(camera, newFrame);

        // Secondly Detect and Track chests in the camera
        // change chestFound accordingly
        // put found chests in the information handler
        processDetectionAndTrackingOfChests(camera, newFrame);
    }

    /**
     * Processes the frame from the current camera to calculate the activity.
     * @param camera the camera
     * @param frame the frame
     */
    private void processActivity(final Camera camera, final Mat frame) {
        CameraActivity activity = camera.getActivity();
        activity.divideFrame(frame);

        activity.addActivities(frame, camera.getFrameCounter());
        if (activity.getLastActivity() > ACTIVITY_THRESHOLD && beginTime == -1) {
            beginTime = nanoTime();
            informationHandler.addInformation("Detected activity");
            active = Activity.LOW;
            for (Camera cam : cameraList) {
                cam.getActivity().setStarted(true);
            }
        }
    }

    /**
     * Processes the frame from the current camera to detect and track chests.
     * @param camera the camera
     * @param frame the frame
     */
    private void processDetectionAndTrackingOfChests(final Camera camera, final Mat frame) {
        Mat subtraction = camera.getChestDetector().subtractFrame(frame);

        if (camera.getFrameCounter() > FIRST_DETECTION) {
            List<Mat> mats = camera.getChestDetector().
                checkForChests(frame, camera, subtraction);
            chestFound = mats.size() > 0;

            for (Mat mat : mats) {
                Pair<Mat, Long> tuple = new Pair<>(mat, nanoTime());
                informationHandler.addMatrix(tuple);
            }
        }
    }
    /**
     * Change the activity with the last known activity.
     */
    public void changeActivity() {
        final double oneThird = 0.33;
        final double twoThird = 0.67;

        double ratio = 0;
        for (Camera camera : cameraList) {
            ratio += camera.getActivity().calculateRatio();
        }
        ratio = ratio / (double) cameraList.size();

        if (ratio < oneThird) {
            active = Activity.LOW;
        } else if (ratio < twoThird) {
            active = Activity.MEDIUM;
        } else {
            active = Activity.HIGH;
        }
    }

    /**
     * Get the activity.
     * @return The activity.
     */
    public Activity getActive() {
        return active;
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

    /**
     * Set a new beginTimer for this cameraHandler.
     * @param newTime the new begin timer
     */
    public void setBeginTime(final long newTime) {
        this.beginTime = newTime;
    }

    /**
     * Get the animationTimers for the streams.
     * @return list of animationTimers
     */
    public List<AnimationTimer> getTimers() {
        return timers;
    }
}
