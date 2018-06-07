package camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CameraChestTracker {

    private Mat previousFrame = new Mat();

    /**
     * Method which removes areas from frame, if they have overlap with the previous frame.
     * @param frame a black and white (1s and 0s) single channel frame
     * @param minChestArea The minimal area a rect has to be to be counted
     * @return The frame with possible found areas removed
     */
    public Mat trackChests(final Mat frame, double minChestArea) {
        Mat tempFrame = frame.clone();
        if (!previousFrame.size().equals(frame.size())) {
            previousFrame = frame.clone();
//            System.out.println("previousframe updated");

        } else {
            Mat overlappingFrame = new Mat();

            // calculate the overlapping parts between previousFrame and frame
            Core.bitwise_and(previousFrame, tempFrame, overlappingFrame);

            // set frame to be the previousFrame
            previousFrame = frame.clone();

            System.out.println("previousframe cloned: " + Core.countNonZero(overlappingFrame));

//            // calculate the contours in overlappingFrame
//            List<MatOfPoint> contoursOverlappingFrame = new ArrayList<>();
//            Mat contourMatOverlappingFrame = new Mat();
//            Imgproc.findContours(overlappingFrame, contoursOverlappingFrame,
//                contourMatOverlappingFrame, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            // calculate the contours in frame
            List<MatOfPoint> contoursFrame = new ArrayList<>();
            Mat contourMatFrame = new Mat();
            Imgproc.findContours(tempFrame, contoursFrame,
                contourMatFrame, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            for (MatOfPoint points : contoursFrame) {
                Rect rect = Imgproc.boundingRect(new MatOfPoint(points.toArray()));
                if (rect.width * rect.height > minChestArea) {
                    Mat checkOverlap = new Mat();
                    Core.bitwise_and(tempFrame.submat(rect), overlappingFrame.submat(rect), checkOverlap);
                    System.out.println(Core.countNonZero(tempFrame.submat(rect)));
                    System.out.println(Core.countNonZero(overlappingFrame.submat(rect)));
                    System.out.println(Core.countNonZero(checkOverlap));
//                    if (Core.countNonZero(checkOverlap) > 0) {
//                        frame.submat(rect).setTo(Mat.zeros(rect.size(), frame.type()));
//                    }
                }
            }

        }
        return tempFrame;
    }

}
