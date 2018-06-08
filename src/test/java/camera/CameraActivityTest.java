package camera;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Test for the Camera activity Class.
 */
public class CameraActivityTest {

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_java341.dll");
    }

    /**
     * Test if divideFrame divides all pixels equally.
     */
    @Test
    void divideFrameTest() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        Camera camera = new Camera(videoCapture, link);
        CameraActivity activity = camera.getActivity();
        Mat frame = camera.getLastFrame();

        assertNotNull(frame);

        activity.divideFrame(camera.getLastFrame());
        List<Mat> fp = activity.getFrameParts();

        assertNotNull(fp);
        assertNotEquals(new ArrayList<Mat>(), activity.getFrameParts());
        if (activity.getFrames() > 1) {
            assertEquals(fp.get(0).size(), fp.get(1).size());   //equal divisions
        }
    }

    /**
     * Verify that activity is added.
     */
    @Test
    void addActivityTest() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        Camera camera = new Camera(videoCapture, link);
        CameraActivity activity = camera.getActivity();

        final int frames = 10;
        for (int i = 0; i < frames; i++) {
            camera.loadFrame();
        }
        Mat frame1 = camera.loadFrame();
        Mat frame2 = camera.loadFrame();
        int parts = activity.getFrames();

        assertNotNull(camera.getActivity());
        assertEquals(activity.getActivityList().get(parts), new ArrayList());

        activity.setFrameCounter(0);
        activity.addActivity(frame1, parts, activity.getKnns().get(parts));
        assertEquals(activity.getActivityList().get(parts), new ArrayList());

        activity.setFrameCounter(Integer.MAX_VALUE);
        activity.addActivity(frame2, parts, activity.getKnns().get(parts));

        assertNotEquals(activity.getActivityList().get(parts), new ArrayList());
    }

}
