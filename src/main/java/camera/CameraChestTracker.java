package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CameraChestTracker {

    Mat previousFrame;

    /**
     * Method which removes areas from frame, if they have overlap with the previous frame.
     * @param frame a black and white (1s and 0s) single channel frame
     */
    public void TrackChests(final Mat frame) {
        if (previousFrame.empty()) {
            previousFrame = frame;
        } else {
            Mat overlappingFrame = new Mat();

            // calculate the overlapping parts between previousFrame and frame
            Core.bitwise_and(previousFrame, frame, overlappingFrame);

            // set frame to be the previousFrame
            previousFrame = frame;

            // calculate the contours in overlappingFrame
            List<MatOfPoint> contoursOverlappingFrame = new ArrayList<>();
            Mat contourMatOverlappingFrame = new Mat();
            Imgproc.findContours(overlappingFrame, contoursOverlappingFrame,
                contourMatOverlappingFrame, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            // calculate the contours in frame
            List<MatOfPoint> contoursFrame = new ArrayList<>();
            Mat contourMatFrame = new Mat();
            Imgproc.findContours(previousFrame, contoursFrame,
                contourMatFrame, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            //TODO: remove contours from frame which have a contour in overlapping as sub frame
        }
    }

}
