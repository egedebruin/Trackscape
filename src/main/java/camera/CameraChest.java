package camera;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;



/**
 * Class for describing a chest found in a camerastream/video/image.
 */
public class CameraChest extends CameraObject {
    private static final Scalar BOXCOLOUR_LOWER = new Scalar(18,100,100);
    private static final Scalar BOXCOLOUR_UPPER = new Scalar(35,255,255);
    private static final int MINBOXSIZE = 10;
    public static Boolean isOpened = false;

    /**
     * Method that checks for boxes in a frame.
     * Whenever a big enough box is detected in a frame
     * a bounding box will be drawn around it
     *
     * @param newFrame the frame that gets checked for the presence of boxes.
     */
    public static void checkForChests(Mat newFrame) {
        Mat dest = getChestsFromFrame(bgrToHsv(newFrame));
        detectChest(dest);
        if (isOpened) {
            includeChestContoursInFrame(newFrame, dest);
        }
    }

    /**
     * Method that detects a chest if the coloured inside of a chest
     * is visible in at least 100 pixels.
     * if such a chest is detected isOpened becomes true
     * @param image Black/White image where white corresponds to the BOXCOLOUR regions
     */
    private static void detectChest(Mat image) {
        isOpened = Core.countNonZero(image) > 300;
    }

    /**
     * Method that draws bounding boxes around all chests in a frame.
     * @param frame the frame that needs bounding boxes
     * @param blackWhiteChestFrame the frame that needs bounding boxes,
     *                            but the boxes are already found
     */
    private static void includeChestContoursInFrame(Mat frame, Mat blackWhiteChestFrame) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat contourMat = new Mat();
        Imgproc.findContours(blackWhiteChestFrame,contours,contourMat,
            Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        for (MatOfPoint contour: contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            org.opencv.core.Rect rect = Imgproc.boundingRect(points);

            // Don't draw boxes around area smaller than MINBOXSIZE pixels
            if (rect.height * rect.width < CameraChest.MINBOXSIZE) {
                return;
            }
            System.out.println("Chest is opened");
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
        }
    }

    /**
     * Method that creates a black/white image of the frame showing the chest candidates in white.
     * @param hsvMatrix a frame in hsv colour space
     * @return the black/white frame showing chest candidates
     */
    private static Mat getChestsFromFrame(Mat hsvMatrix) {
        Mat dest = new Mat();
        Core.inRange(hsvMatrix,CameraChest.BOXCOLOUR_LOWER,CameraChest.BOXCOLOUR_UPPER,dest);

        return dest;
    }
}
