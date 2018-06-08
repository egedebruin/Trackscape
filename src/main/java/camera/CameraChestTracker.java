package camera;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for tracking chests.
 */
public class CameraChestTracker {

    private List<MatOfPoint> previousContours;

    /**
     * Method which removes areas from frame, if they have overlap with the previous frame.
     * @param frame a black and white (1s and 0s) single channel frame
     * @param minChestArea The minimal area a rect has to be to be counted
     * @return The frame with possible found areas removed
     */
    public Mat trackChests(final Mat frame, final double minChestArea) {
        Mat tempFrame = frame.clone();

        // calculate the contours in frame
        List<MatOfPoint> contoursFrame = new ArrayList<>();
        Mat contourMatFrame = new Mat();
        Imgproc.findContours(tempFrame, contoursFrame,
            contourMatFrame, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        if (previousContours != null) {
            // Check if there is overlap between chests in subsequent frames.
            for (MatOfPoint points : contoursFrame) {
                Rect rect = Imgproc.boundingRect(new MatOfPoint(points.toArray()));
                if (rect.area() > minChestArea) {
                    for (MatOfPoint points2 : previousContours) {
                        Rect rect2 = Imgproc.boundingRect(new MatOfPoint(points2.toArray()));

                        if (rect2.area() > minChestArea && doOverlap(rect, rect2)) {
                            setRectToZerosInFrame(tempFrame, rect);
                        }
                    }
                }
            }
        }

        previousContours = contoursFrame;

        return tempFrame;
    }

    /**
     * Sets a subMatrix to only zeros (black).
     * @param tempFrame the frame where an area should be removed from
     * @param rect the position where zeros should be inserted
     */
    private void setRectToZerosInFrame(final Mat tempFrame, final Rect rect) {
        if (tempFrame.submat(rect).isSubmatrix()) {
            Mat zeros = Mat.zeros(rect.size(), tempFrame.type());
            zeros.copyTo(tempFrame.submat(rect));
        }
    }

    /**
     * Method that checks if two Rects overlap.
     * @param r1    Rect 1
     * @param r2    Rect 2
     * @return  true iff rect1 has overlap with r2, false otherwise.
     */
    private Boolean doOverlap(final Rect r1, final Rect r2) {
        if (r1.x + r1.width < r2.x || r1.x > r2.x + r2.width) {
            return false;
        }
        return r1.y + r1.height >= r2.y && r1.y <= r2.y + r2.height;
    }

}
