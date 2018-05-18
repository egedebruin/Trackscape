package handlers;

import camera.Camera;
import camera.CameraChest;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;

/**
 * Class for handling the cameras. Holds a list of the cameras en controls it.
 */
public class CameraHandler {

    /**
     * The list of the cameras.
     */
    private List<Camera> cameraList;
    public CameraChest cameraChest = new CameraChest();

    /**
     * Constructor for the CameraHandler class.
     */
    public CameraHandler() {
        cameraList = new ArrayList<>();
        HOGDescriptor hogDescriptor = new HOGDescriptor();
        hogDescriptor.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());
    }

    /**
     * Add a new camera to use for streaming.
     *
     * @param link The link of the camera.
     * @return The new camera as a Camera object.
     */
    public Camera addCamera(String link) {
        VideoCapture videoCapture = new VideoCapture(link);
        boolean opened = videoCapture.open(link);
        if (!opened) {
            return null;
        }
        Camera camera = new Camera(videoCapture, link);
        cameraList.add(camera);
        return camera;
    }

    /**
     * Get a new frame from the camera.
     *
     * @param camera The camera to get the new frame from.
     * @return The new frame as a BufferedImage.
     */
    public Mat getNewFrame(Camera camera) {
        Mat newFrame = camera.getLastFrame();
        if (camera.getFirstFrame() == null) {
            camera.setFirstFrame(newFrame);
        }
        Mat dest = getChestsFromFrame(bgrToHsv(newFrame));
        cameraChest.detectChest(dest);
        if (cameraChest.isOpened) {
            includeChestContoursInFrame(newFrame, dest);
        }
        return newFrame;
    }

    /**
     * Method that draws bounding boxes around all chests in a frame.
     * @param frame the frame that needs bounding boxes
     * @param blackWhiteChestFrame the frame that needs bounding boxes,
     *                            but the boxes are already found
     */
    public void includeChestContoursInFrame(Mat frame, Mat blackWhiteChestFrame) {
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
     * Method that changes colourspaces of a Mat from BGR to HSV.
     * @param mat matrix to be converted to hsv colour space
     * @return Mat a hsv colour space representation of the previously bgr matrix
     */
    public Mat bgrToHsv(Mat mat) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(mat,hsv,Imgproc.COLOR_BGR2HSV);
        return hsv;
    }

    /**
     * Method that creates a black/white image of the frame showing the chest candidates in white.
     * @param hsvMatrix a frame in hsv colour space
     * @return the black/white frame showing chest candidates
     */
    private Mat getChestsFromFrame(Mat hsvMatrix) {
        Mat dest = new Mat();
        Core.inRange(hsvMatrix,CameraChest.BOXCOLOUR_LOWER,CameraChest.BOXCOLOUR_UPPER,dest);

        return dest;
    }

    /**
     * Get the size of the list of cameras.
     *
     * @return The size of the list.
     */
    public int listSize() {
        return cameraList.size();
    }

    /**
     * Clear the list of cameras.
     */
    public void clearList() {
        cameraList.clear();
    }

    /**
     * Get a camera from the list.
     *
     * @param index The index of the camera.
     * @return The camera.
     */
    public Camera getCamera(int index) {
        return cameraList.get(index);
    }

}
