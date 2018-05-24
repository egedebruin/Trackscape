package camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * Class describing a camera.
 * Camera holds relevant information about data generated by a camera
 */
public class Camera {

    /**
     * Class parameters.
     */
    private List<CameraObjectDetector> cameraObjectDetectorList;
    private VideoCapture videoCapture;
    private String link;
    private Mat firstFrame;
    private Mat lastFrame = new Mat();
    private boolean changed = false;
    private List<Mat> frameParts = new ArrayList<>();
    private List<List<double[]>> activity;
    private List<BackgroundSubtractorKNN> knns = new ArrayList<>();
    private long firstTime = -1;
    private int frameCounter = 0;
    private static final int FRAMES = 4;

    /**
     * Constructor for a Camera.
     *
     * @param newCapture The VideoCapture of this camera.
     * @param newLink The link of this camera.
     */
    public Camera(final VideoCapture newCapture, final String newLink) {
        final int threshold = 1000;
        cameraObjectDetectorList = new ArrayList<>();
        videoCapture = newCapture;
        link = newLink;
        activity = new ArrayList<>();
        for (int i = 0; i < FRAMES; i++) {
            frameParts.add(new Mat());
            activity.add(new ArrayList<>());
            knns.add(Video.createBackgroundSubtractorKNN(1, threshold, false));
        }
        activity.add(new ArrayList<>());
        knns.add(Video.createBackgroundSubtractorKNN(1, threshold, false));
    }

    /**
     * Gets the last known frame of this camera.
     *
     * @return The frame in Mat format.
     */
    public Mat getLastFrame() {
        Mat newFrame = loadFrame();

        if (newFrame != null && newFrame.rows() != 0 && newFrame.cols() != 0) {
            lastFrame = newFrame;
            changed = true;
        } else {
            changed = false;
            return lastFrame.clone();
        }

        divideFrame(newFrame);

        addActivities(newFrame);

        return lastFrame.clone();
    }

    /**
     * Add all activities for every part of the frame.
     * @param newFrame The frame to get the activity from.
     */
    private void addActivities(final Mat newFrame) {
        final int modulus = 5;

        if (frameCounter % modulus == 0) {
            for (int i = 0; i < FRAMES; i++) {
                addActivity(frameParts.get(i), i, knns.get(i));
            }
        }

        addActivity(newFrame, FRAMES, knns.get(FRAMES));
        frameCounter++;
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
     * Add current activity to list.
     * @param frame the current frame
     * @param partNumber current part of the frame
     * @param knn the backgroundsubstractor
     */
    public void addActivity(final Mat frame, final int partNumber,
                            final BackgroundSubtractorKNN knn) {
        Mat subtraction = new Mat();
        knn.apply(frame, subtraction);
        Scalar meanValues = Core.mean(subtraction);

        double change = 0;
        for (double v : meanValues.val) {
            change += v;
        }

        final int minFrames = 30;
        final double timeWindow = 1000.00;

        // Only add the activity to the list when at least some frames are processed.
        if (frameCounter > minFrames) {
            if (firstTime == -1) {
                firstTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis() - firstTime;

            double[] tuple = {currentTime / timeWindow, change};

            activity.get(partNumber).add(tuple);
        }
    }

    /**
     * Reads the videoCapture to load the new frame.
     *
     * @return The new frame in Mat format.
     */
    public Mat loadFrame() {
        Mat loadFrame = new Mat();
        videoCapture.read(loadFrame);
        return loadFrame;
    }

    /**
     * Overriding equals method.
     *
     * @param o the object to be compared
     * @return boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Camera)) {
            return false;
        }
        Camera camera = (Camera) o;
        return Objects.equals(link, camera.link);
    }

    /**
     * Returns if the frame of the camera is changed.
     *
     * @return If frame is changed.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Get the activity of this camera.
     *
     * @return The activity.
     */
    public List<List<double[]>> getActivity() {
        return activity;
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
     * Get the first frame known of the camera.
     * @return The Mat of the first frame.
     */
    public Mat getFirstFrame() {
        return firstFrame;
    }

    /**
     * Set the first frame of the camera.
     * @param frame The Mat of the new first frame.
     */
    public void setFirstFrame(final Mat frame) {
        this.firstFrame = frame;
    }

    /**
     * Get the number of frames that one frame should be divided in.
     * @return frames
     */
    public int getFrames() {
        return FRAMES;
    }

    /**
     * Set the frameCounter.
     * @param newFrameCounter new framecount
     */
    public void setFrameCounter(final int newFrameCounter) {
        this.frameCounter = newFrameCounter;
    }
}
