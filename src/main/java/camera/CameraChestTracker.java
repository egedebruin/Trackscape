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

    private static final int BOUNDING_BOX_INCREASE_SCREEN_RATIO = 16;
    private List<MatOfPoint> previousContours;

    /**
     * Method which removes areas from frame, if they have overlap with the previous frame.
     * @param frame a black and white (1s and 0s) single channel frame
     * @param minChestArea The minimal area a rect has to be to be counted
     * @return The frame with possible found areas removed
     */
    public Mat trackChests(final Mat frame, final double minChestArea) {
        Mat tempFrame = frame.clone();

        // Calculate the contours in frame
        List<MatOfPoint> contoursFrame = new ArrayList<>();
        Imgproc.findContours(tempFrame, contoursFrame,
            new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        if (previousContours != null) {
            // Checks if there is a chest in the tempFrame
            // that has overlap with a chest in the previous frame.
            // if such a chest exists it will be removed from tempFrame
            checkForOverlap(contoursFrame, minChestArea, tempFrame);
        }
        previousContours = contoursFrame;

        return tempFrame;
    }

    /**
     * Check for overlap between chests in subsequent frames.
     * @param contoursFrame a frame that shows contours
     * @param minChestArea the minimum size of chest area
     * @param tempFrame clone of the current frame
     */
    private void checkForOverlap(final List<MatOfPoint> contoursFrame,
                                 final double minChestArea, final Mat tempFrame) {
        for (MatOfPoint points : contoursFrame) {
            Rect rect = Imgproc.boundingRect(new MatOfPoint(points.toArray()));
            if (rect.area() >= minChestArea) {
                for (MatOfPoint points2 : previousContours) {
                    Rect rect2 = Imgproc.boundingRect(new MatOfPoint(points2.toArray()));
                    if (rect2.area() >= minChestArea) {
                        compareRects(rect, rect2, tempFrame);
                    }
                }
            }
        }
    }

    /**
     * Compares two rects.
     * If they have overlap then tempFrame is adjusted to remove overlapping areas.
     * @param rect The first rect (from the current frame)
     * @param rect2 The second rect (from the previous frame)
     * @param tempFrame the frame that tracks chests
     */
    private void compareRects(final Rect rect, final Rect rect2, final Mat tempFrame) {
            // Increase the area in which overlap could be found
            rect2.width += tempFrame.width() / BOUNDING_BOX_INCREASE_SCREEN_RATIO;
            rect2.height += tempFrame.height() / BOUNDING_BOX_INCREASE_SCREEN_RATIO;

            if (doOverlap(rect, rect2)) {
                setRectToZerosInFrame(tempFrame, rect);
            }
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
