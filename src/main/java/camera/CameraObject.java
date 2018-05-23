package camera;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Abstract class describing an object found in a camerastream/video/image.
 */
public abstract class CameraObject {

    /**
     * Method that changes colourspaces of a Mat from BGR to HSV.
     * @param mat matrix to be converted to hsv colour space
     * @return Mat a hsv colour space representation of the previously bgr matrix
     */
    public static Mat bgrToHsv(final Mat mat) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(mat ,hsv, Imgproc.COLOR_BGR2HSV);
        return hsv;
    }
}
