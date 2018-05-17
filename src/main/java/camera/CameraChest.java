package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * Class for describing a chest found in a camerastream/video/image.
 */
public class CameraChest extends CameraObject {
    public static final Scalar BOXCOLOUR_LOWER = new Scalar(18,100,100);
    public static final Scalar BOXCOLOUR_UPPER = new Scalar(35,255,255);
    public Boolean isOpened = false;

    /**
     * Method that detects a chest if the coloured inside of a chest
     * is visible in at least 100 pixels.
     * if such a chest is detected isOpened becomes true
     * @param image Black/White image where white corresponds to the BOXCOLOUR regions
     */
    public void detectChest(Mat image) {
        if (Core.countNonZero(image) > 300) {
            this.isOpened = true;
        } else {
            this.isOpened = false;
        }
    }
}
