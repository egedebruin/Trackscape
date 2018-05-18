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
    private List<CameraObject> cameraObjectList;
    private VideoCapture videoCapture;
    private String link;
    private Mat lastFrame = new Mat();
    private boolean changed = false;
    private Mat upperLeft = new Mat();
    private Mat upperRight = new Mat();
    private Mat lowerLeft = new Mat();
    private Mat lowerRight = new Mat();
    private List<List<double[]>> activity;
    private List<double[]> activityUpperLeft;
    private List<double[]> activityUpperRight;
    private List<double[]> activityLowerLeft;
    private List<double[]> activityLowerRight;
    private BackgroundSubtractorKNN knn =
        Video.createBackgroundSubtractorKNN(1, 1000, false);
    private long firstTime = -1;
    private int frameCounter = 0;

    /**
     * Constructor for a Camera.
     *
     * @param newCapture The VideoCapture of this camera.
     * @param newLink    The link of this camera.
     */
    public Camera(final VideoCapture newCapture, final String newLink) {
        cameraObjectList = new ArrayList<>();
        videoCapture = newCapture;
        link = newLink;
        activity = new ArrayList<>();
        activityUpperLeft = new ArrayList<>();
        activityUpperRight = new ArrayList<>();
        activityLowerLeft = new ArrayList<>();
        activityLowerRight = new ArrayList<>();
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
        }

        divideFrame(newFrame);

        if (frameCounter % 5 == 0) {
            addActivity(upperLeft, activityUpperLeft);
            addActivity(upperRight, activityUpperRight);
            addActivity(lowerLeft, activityLowerLeft);
            addActivity(lowerRight, activityLowerRight);
        }
        frameCounter++;

        return lastFrame.clone();
    }

    public void divideFrame(Mat frame) {
      upperLeft = frame.colRange(0, frame.width()/2);
      upperLeft.rowRange(0, frame.height()/2);

      upperRight = frame.colRange(frame.width()/2, frame.width()-1);
      upperRight.rowRange(0, frame.height()/2);

      lowerLeft = frame.colRange(0, frame.width()/2);
      lowerLeft.rowRange(frame.height()/2, frame.height()-1);

      lowerRight = frame.colRange(frame.width()/2, frame.width()-1);
      lowerRight = frame.rowRange(frame.height()/2, frame.height()-1);
    }

    /**
     * Adds an activity to the list of activities.
     * @param frame The frame to get the activity from.
     */
    public void addActivity(final Mat frame, List<double[]> activityList) {
        Mat subtraction = new Mat();
        knn.apply(frame, subtraction);
        Scalar meanValues = Core.mean(subtraction);

        double meanChange = 0;
        for (double v : meanValues.val) {
            meanChange += v;
        }
        meanChange = meanChange / (double) meanValues.val.length;

        if (meanChange < 0.1) {
            System.out.println("No peepz in image?");
        }

        if (frameCounter > 30) {
            if (firstTime == -1) {
                firstTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis() - firstTime;

            double[] tuple = {currentTime / 1000.00, meanChange};

            activityList.add(tuple);
            activity.add(activityList);
        }
    }

    /**
     * Reads the videoCapture to load the new frame.
     *
     * @return The new frame in Mat format.
     */
    private Mat loadFrame() {
        Mat loadFrame = new Mat();
        videoCapture.read(loadFrame);
        return loadFrame;
    }

    /**
     * Overriding equals method.
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
     * Gets the activity of this camera.
     *
     * @return The activity.
     */
    public List<List<double[]>> getActivity() {
        return activity;
    }
}
