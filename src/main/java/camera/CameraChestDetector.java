package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
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
    private static final Scalar CHESTCOLOUR_LOWER = new Scalar(17, 120, 80);
    private static final Scalar CHESTCOLOUR_UPPER = new Scalar(35, 255, 205);
    private static final double MINCHESTAREA = 550;
    private Boolean isOpened = false;
    private final Comparator<Rect> comparator = new RectComparator();
    private List<Camera> cameraList = new ArrayList<>();

    /**
     * Method that checks for boxes in a frame.
     * Whenever a big enough box is detected in a frame
     * a bounding box will be drawn around it
     *
     * @param newFrame the frame that gets checked for the presence of boxes.
     * @param camera the camera that generated the frame
     * @param subtraction the subtraction of this frame.
     * @return true if chest is detected, false otherwise.
     */
    public List<Mat> checkForChests(final Mat newFrame, final Camera camera,
                                    final Mat subtraction) {
        int noOfChests = camera.getNumOfChestsInRoom();
        Mat dest = getChestsFromFrame(bgrToHsv(newFrame));
        Mat subtracted = new Mat();
        List<Mat> mats = new ArrayList<>();

        Mat tracked = camera.getTracker().trackChests(dest, MINCHESTAREA);

        Core.bitwise_and(tracked, subtraction, subtracted);
        detectChest(subtracted);

        if (isOpened) {
            mats = includeChestContoursInFrame(newFrame, subtracted, noOfChests);
        }
        return mats;
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
    private List<Mat> includeChestContoursInFrame(final Mat frame, final Mat blackWhiteChestFrame,
                                                    final int noOfChests) {
        List<Mat> detectedMats = new ArrayList<>();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat contourMat = new Mat();
        Imgproc.findContours(blackWhiteChestFrame, contours, contourMat,
            Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        ArrayList<Rect> rects = new ArrayList<>(noOfChests);
        for (MatOfPoint contour: contours) {
            MatOfPoint contourPoints = new MatOfPoint(contour.toArray());

            // Get bounding rect of contour
            Rect newrect = Imgproc.boundingRect(contourPoints);
            // If not all spots are filled add newrect to the biggest rects.
            if (newrect.area() > MINCHESTAREA) {
                addToRects(newrect, rects, noOfChests);
            }
        }
        final int axisOffset = 50;
        final int sizeOffset = 100;
        for (Rect rect : rects) {
            if (rect.area() > MINCHESTAREA) {
                Rect cutoutChest = new Rect(rect.x - axisOffset, rect.y - axisOffset,
                    rect.width + sizeOffset, rect.height + sizeOffset);
                Point topLeft = new Point(Math.max(cutoutChest.tl().x, 0),
                    Math.max(0, cutoutChest.tl().y));
                Point bottomRight = new Point(Math.min(cutoutChest.br().x,
                    frame.width() - 1), Math.min(cutoutChest.br().y, frame.height() - 1));
                //Imgproc.rectangle(frame, topLeft, bottomRight, CHESTBOXCOLOUR, 2);
                Rect r = new Rect(topLeft, bottomRight);
                detectedMats.add(frame.submat(r));
            }
        }
        return detectedMats;
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

    /**
     * Add camera to the chestDetector's cameraList.
     * @param cam the camera that needs to be added to the list
     */
    public void addCameraToDetector(final Camera cam) {
        cameraList.add(cam);
    }
}
