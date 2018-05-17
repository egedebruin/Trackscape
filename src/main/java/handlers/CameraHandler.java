package handlers;

import camera.Camera;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * Class for handling the cameras. Holds a list of the cameras en controls it.
 */
public class CameraHandler {

    /**
     * The list of the cameras.
     */
    private List<Camera> cameraList;

    private HOGDescriptor hogDescriptor = new HOGDescriptor();

    private BackgroundSubtractorKNN knn =
        Video.createBackgroundSubtractorKNN(1, 1000, true);

    /**
     * Constructor for the CameraHandler class.
     */
    public CameraHandler() {
        cameraList = new ArrayList<>();
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

        return getSubtraction(newFrame, camera.getFirstFrame());
    }

    public Mat getSubtraction(Mat frame, Mat background) {
        MatOfRect rects = new MatOfRect();
        MatOfDouble matOfDouble = new MatOfDouble();

        Mat result = new Mat();

        knn.apply(frame, result);

        List<Rect> list = rects.toList();

        for (int i = 0; i < list.size(); i++) {
            if (matOfDouble.toList().get(i) > 1) {
                System.out.println(matOfDouble.toList().get(i));
                Rect rectangle = list.get(i);
                Point point1 = new Point();
                Point point2 = new Point();
                point1.x = rectangle.x;
                point1.y = rectangle.y;
                point2.x = rectangle.x + rectangle.width;
                point2.y = rectangle.y + rectangle.height;
                Imgproc.rectangle(frame, point1, point2, new Scalar(0, 255, 0), 2);
            }
        }
        return frame;
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
