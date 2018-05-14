package camera;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CameraTest {

	@org.junit.jupiter.api.BeforeEach
	void setUp() {
		System.load(System.getProperty("user.dir") + "\\libs\\opencv_ffmpeg341_64.dll");
		System.load(System.getProperty("user.dir") + "\\libs\\opencv_java341.dll");
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown() {
	}

	@Test
	void constructorTest(){
		assertNotNull(new Camera(null, null));
	}
}