package camera;

import java.io.File;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CameraTest {

	@org.junit.jupiter.api.BeforeEach
	void setUp() {
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			System.load(System.getProperty("user.dir")
				+ File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
			System.load(System.getProperty("user.dir")
				+ File.separator + "libs" + File.separator + "opencv_java341.dll");
		}
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown() {
	}

	@Test
	void constructorTest(){
		assertNotNull(new Camera(null, null));
	}
}