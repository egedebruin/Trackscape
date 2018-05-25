package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



/**
 * Class for describing a chest found in a camerastream/video/image.
 */
public class CameraChestDetector extends CameraObjectDetector {
    private static final Scalar CHESTCOLOUR_LOWER = new Scalar(19, 100, 60);
    private static final Scalar CHESTCOLOUR_UPPER = new Scalar(35, 255, 205);
    private static final Scalar CHESTBOXCOLOUR = new Scalar(255, 0, 255);
    private static final double MINCHESTAREA = 600;
    private static final double APPROXSCALE = 0.02;
    private Boolean isOpened = false;
    private final Comparator<Rect> comparator = new RectComparator();

    /**
     * Method that checks for boxes in a frame.
     * Whenever a big enough box is detected in a frame
     * a bounding box will be drawn around it
     *
     * @param newFrame the frame that gets checked for the presence of boxes.
     * @param noOfChests the number of chests in the room.
     * @param subtraction the subtraction of this frame.
     * @return true if chest is detected, false otherwise.
     */
    public boolean checkForChests(final Mat newFrame, final int noOfChests, final Mat subtraction) {
        Mat dest = getChestsFromFrame(bgrToHsv(newFrame));
        Mat subtracted = new Mat();
        boolean isDetected = false;
        Core.bitwise_and(dest, subtraction, subtracted);
        detectChest(subtracted);
        if (isOpened) {
            isDetected = includeChestContoursInFrame(newFrame, subtracted, noOfChests);
        }
        return isDetected;
    }

    /**
     * Method that detects a chest if the coloured inside of a chest
     * is visible in at least 100 pixels.
     * if such a chest is detected isOpened becomes true
     * @param image Black/White image where white corresponds to the BOXCOLOUR regions
     */
    private void detectChest(final Mat image) {
        isOpened = Core.countNonZero(image) > MINCHESTAREA;
    }

    /**
     * Method that draws bounding boxes around all chests in a frame.
     * @param frame the frame that needs bounding boxes.
     * @param blackWhiteChestFrame the frame that needs bounding boxes,
     *                            but the boxes are already found.
     * @param noOfChests the number of chests in the room.
     *
     * @return true iff a boundingbox is drawn.
     */
    private boolean includeChestContoursInFrame(final Mat frame, final Mat blackWhiteChestFrame,
                                                    final int noOfChests) {
        boolean isContourDrawn = false;
        List<MatOfPoint> contours = new ArrayList<>();
        Mat contourMat = new Mat();
        Imgproc.findContours(blackWhiteChestFrame, contours, contourMat,
            Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Rect> rects = new ArrayList<>(noOfChests);
        for (MatOfPoint contour: contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * APPROXSCALE;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect newrect = Imgproc.boundingRect(points);
            // If not all spots are filled add newrect to the biggest rects.
            addToRects(newrect, rects, noOfChests);
        }
        for (Rect rect : rects) {
            if (rect.area() > MINCHESTAREA) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), CHESTBOXCOLOUR, 2);
                isContourDrawn = true;
            }
        }
        return isContourDrawn;
    }

    /**
     * Method that adds a new rect to rects if the new rect is big enough.
     * @param newrect   The candidate rect that might be added to rects.
     * @param rects     array of the noOfChests biggest rects.
     * @param noOfChests    number of chests.
     */
    private void addToRects(final Rect newrect, final ArrayList<Rect> rects, final int noOfChests) {
        if (rects.size() < noOfChests) {
            rects.add(newrect);
            rects.sort(comparator);
        } else {
            // if all spots are filled only add newrect if
            // it is larger than the smallest of the biggest
            if (newrect.area() > rects.get(rects.size() - 1).area()) {
                rects.set(rects.size() - 1, newrect);
                rects.sort(comparator);
            }
        }
    }

    /**
     * Method that creates a black/white image of the frame showing the chest candidates in white.
     * @param hsvMatrix a frame in hsv colour space
     * @return the black/white frame showing chest candidates
     */
    private Mat getChestsFromFrame(final Mat hsvMatrix) {
        Mat dest = new Mat();
        Core.inRange(hsvMatrix, CHESTCOLOUR_LOWER, CHESTCOLOUR_UPPER, dest);

        return dest;
    }
}
