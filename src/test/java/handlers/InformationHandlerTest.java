package handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.io.File;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;

/**
 * Tests for the information handler class.
 */
public class InformationHandlerTest {

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
     * Test if the constructor does what it needs to do.
     */
    @Test
    void testConstructor() {
        InformationHandler handler = new InformationHandler();
        assertNotNull(handler.getInfQueue());
    }

    /**
     * Test empty information queue will return empty.
     */
    @Test
    void testGetInformationEmpty() {
        InformationHandler handler = new InformationHandler();
        assertEquals("empty", handler.getInformation());
    }

    /**
     * Test get information gets added information.
     */
    @Test
    void testGetInformation() {
        InformationHandler handler = new InformationHandler();
        handler.addInformation("ajax");
        assertEquals("ajax", handler.getInformation());
    }

    /**
     * Test get matrix returns null for empty queue.
     */
    @Test
    void testGetMatrixEmpty() {
        InformationHandler handler = new InformationHandler();
        assertNull(handler.getMatrix());
    }

    /**
     * Test get matrix gets added matrix.
     */
    @Test
    void testGetMatrix() {
        InformationHandler handler = new InformationHandler();
        handler.addMatrix(new Pair<>(new Mat(), (long) 1));
        assertNotNull(handler.getMatrix());
    }

    /**
     * Test clear mat queue.
     */
    @Test
    void testClearMatQueue() {
        InformationHandler handler = new InformationHandler();
        handler.addMatrix(new Pair<>(new Mat(), (long) 1));
        handler.clearMatQueue();
        assertNull(handler.getMatrix());
    }

    /**
     * Test getter for matQueue.
     */
    @Test
    void testGetMatQueue() {
        InformationHandler handler = new InformationHandler();
        assertNotNull(handler.getMatQueue());
    }

    /**
     * Test getter for infoQueue.
     */
    @Test
    void testGetInfoQueue() {
        InformationHandler handler = new InformationHandler();
        assertNotNull(handler.getInfQueue());
    }
}
