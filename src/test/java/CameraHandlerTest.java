import camera.Camera;
import org.junit.jupiter.api.Test;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CameraHandlerTest {

	private final String streamLink = "rtsp://192.168.0.117:554/"
		+ "user=admin&password=&channel=1&stream=1.sdp?real_stream--rtp-caching=100";

	static {
		// These should be at the start of the application,
		// so if the main changes this should be included.
		// Load OpenCV library.
		System.load(System.getProperty("user.dir")
			+ File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
		System.load(System.getProperty("user.dir")
			+ File.separator + "libs" + File.separator + "opencv_java341.dll");
	}

	@Test
	void addCameraTest() {
		CameraHandler c = new CameraHandler();
		Camera cam = c.addCamera(streamLink);
		assertEquals(new Camera(new VideoCapture(),streamLink), cam);
	}

	@Test
	void addUnknownCameraTest() {
		CameraHandler c = new CameraHandler();
		Camera cam = c.addCamera("error");
		assertNull(cam);
	}

	@Test
	void getNewFrameTest() {
		CameraHandler c = new CameraHandler();
		Camera cam = c.addCamera(streamLink);
		BufferedImage bi = c.getNewFrame(cam);
		assertNotNull(bi);
	}
}