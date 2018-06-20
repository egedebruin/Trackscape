package camera;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        if (CameraActivity.FRAMES > 1) {
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

        loadFrames(camera);

        Mat frame1 = camera.loadFrame();
        Mat frame2 = camera.loadFrame();
        int parts = CameraActivity.FRAMES;

        assertNotNull(camera.getActivity());
        assertEquals(activity.getActivityList().get(parts), new ArrayList());

        activity.setStarted(true);
        activity.setFrameCounter(0);
        activity.addActivity(frame1, parts, activity.getKnns().get(parts));
        assertEquals(activity.getActivityList().get(parts), new ArrayList());

        activity.setFrameCounter(Integer.MAX_VALUE);
        activity.addActivity(frame2, parts, activity.getKnns().get(parts));

        assertNotEquals(activity.getActivityList().get(parts), new ArrayList());
    }

    /**
     * Test calculateRatio method.
     */
    @Test
    void calculateRatioTest() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        Camera camera = new Camera(videoCapture, link);
        CameraActivity activity = camera.getActivity();
        assertEquals(0, activity.calculateRatio());

        activity.getActivityList().get(CameraActivity.FRAMES).add(1.0);
        activity.setLastActivity(2);

        assertEquals(1, activity.calculateRatio());
    }

    /**
     * Loads a set amount of frames for the given camera.
     * @param camera the camera that gets its frame loaded
     */
    private void loadFrames(final Camera camera) {
        final int frames = 10;
        for (int i = 0; i < frames; i++) {
            camera.loadFrame();
        }
    }


}
