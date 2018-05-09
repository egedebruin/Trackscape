package camera;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CameraTest {

	@org.junit.jupiter.api.BeforeEach
	void setUp() {
	}

	@org.junit.jupiter.api.AfterEach
	void tearDown() {
	}

	@Test
	void constructorTest(){
		assertNotNull(new Camera());
	}
}