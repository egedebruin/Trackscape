package camera;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;

/**
 * Class for activity of a camera.
 */
public class CameraActivity {

    private List<Mat> frameParts = new ArrayList<>();
    private List<List<Double>> activityList;
    private List<BackgroundSubtractorKNN> knns = new ArrayList<>();
    private static final int FRAMES = 1;
    private double lastActivity = 0;
    private int frameCounter;
    private boolean started = false;

    /**
     * Constructor for the class.
     */
    public CameraActivity() {
        this.activityList = new ArrayList<>();
        final int threshold = 1000;
        for (int i = 0; i < FRAMES; i++) {
            frameParts.add(new Mat());
            activityList.add(new ArrayList<>());
            knns.add(Video.createBackgroundSubtractorKNN(1, threshold, false));
        }
        activityList.add(new ArrayList<>());
        knns.add(Video.createBackgroundSubtractorKNN(1, threshold, false));
    }

    /**
     * Add all activities for every part of the frame.
     * @param newFrame The frame to get the activityList from.
     * @param counter The new frameCounter.
     */
    public void addActivities(final Mat newFrame, final int counter) {
        frameCounter = counter;
        for (int i = 0; i < FRAMES; i++) {
            addActivity(frameParts.get(i), i, knns.get(i));
        }
        lastActivity = addActivity(newFrame, FRAMES, knns.get(FRAMES));

    }

    /**
     * Method in which a frame get divided into frames equally large parts.
     * frames should be a number where the sqrt is an integer
     *
     * @param frame the input frame
     */
    public void divideFrame(final Mat frame) {
        for (int i = 0; i < FRAMES; i++) {
            int sqrt = (int) Math.sqrt(FRAMES);
            int midCol = frame.width() / sqrt;
            int midRow = frame.height() / sqrt;

            int col = midCol * (i % sqrt);
            int row = (i * sqrt) / FRAMES * midRow;

            Mat result = frame.colRange(col, col + midCol);
            result = result.rowRange(row, row + midRow);
            frameParts.set(i, result);
        }
    }

    /**
     * Add current activityList to list.
     * @param frame the current frame
     * @param partNumber current part of the frame
     * @param knn the backgroundsubstractor
     * @return double of the change
     */
    public double addActivity(final Mat frame, final int partNumber,
                              final BackgroundSubtractorKNN knn) {
        Mat subtraction = new Mat();
        knn.apply(frame, subtraction);
        Scalar meanValues = Core.mean(subtraction);

        double change = 0;
        for (double v : meanValues.val) {
            change += v;
        }

        final int minFrames = 30;

        // Only add the activityList to the list when at least some frames are processed.
        if (frameCounter > minFrames) {
            if (started) {
                activityList.get(partNumber).add(change);
            }
            return change;
        }
        return 0;
    }

    /**
     * Calculate the ratio in which the last activity falls.
     * @return The ratio.
     */
    public double calculateRatio() {
        List<Double> activities = activityList.get(FRAMES);
        int i = 0;

        for (Double activity : activities) {
            if (activity <= lastActivity) {
                i++;
            }
        }
        return (double) i / (double) activities.size();
    }

    /**
     * Getter for lastActivity.
     * @return the last activityList.
     */
    public double getLastActivity() {
        return lastActivity;
    }

    /**
     * Get the number of frames that one frame should be divided in.
     * @return frames
     */
    public int getFrames() {
        return FRAMES;
    }

    /**
     * Get the activityList of this camera.
     *
     * @return The activityList.
     */
    public List<List<Double>> getActivityList() {
        return activityList;
    }

    /**
     * Get the list of background subtractors.
     * @return knns
     */
    public List<BackgroundSubtractorKNN> getKnns() {
        return knns;
    }

    /**
     * Get the frameParts list.
     * @return frameParts.
     */
    public List<Mat> getFrameParts() {
        return frameParts;
    }

    /**
     * Set the frameCounter.
     * @param newFrameCounter new framecount
     */
    public void setFrameCounter(final int newFrameCounter) {
        this.frameCounter = newFrameCounter;
    }

    /**
     * Set if the camera is started.
     * @param newStarted the boolean started.
     */
    public void setStarted(final boolean newStarted) {
        this.started = newStarted;
    }
}
