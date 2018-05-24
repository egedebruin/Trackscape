package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;

/**
 * Abstract class describing an object found in a camerastream/video/image.
 */
public abstract class CameraObjectDetector {

    private BackgroundSubtractorKNN knn = Video.createBackgroundSubtractorKNN();

    /**
     * Method that changes colourspaces of a Mat from BGR to HSV.
     * @param mat matrix to be converted to hsv colour space
     * @return Mat a hsv colour space representation of the previously bgr matrix
     */
    public Mat bgrToHsv(final Mat mat) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
        return hsv;
    }

    /**
     * Subtracts the subtractor frame from the frame, returns a black and white image where white
     * pixels are new pixels.
     * @param frame The frame to be subtracted.
     * @return The result of the subtraction.
     */
    public Mat subtractFrame(final Mat frame) {
        Mat subtraction = new Mat();

        knn.apply(frame, subtraction);

        return subtraction;
    }

}
