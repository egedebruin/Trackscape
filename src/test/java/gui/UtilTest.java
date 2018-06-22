package gui;

import com.sun.javafx.application.PlatformImpl;
import java.util.concurrent.TimeUnit;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the Util class.
 */
public class UtilTest {

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
     * Test whether mat gets converted to bufferedImage correctly.
     */
    @Test
    void matToBufferedImageTest() {
        final int rows = 10;
        final int columns = 10;
        final int maxChannels = 3;
        Mat zeroMat = Mat.zeros(rows, columns, CvType.CV_8UC1);
        Util.matToBufferedImage(zeroMat);
        assertEquals(Util.matToBufferedImage(zeroMat).getType(), BufferedImage.TYPE_BYTE_GRAY);
        assertEquals(zeroMat.channels(), 1);

        Mat colourMat = new Mat(rows, columns, CvType.CV_8UC3);
        assertEquals(Util.matToBufferedImage(colourMat).getType(), BufferedImage.TYPE_3BYTE_BGR);
        assertEquals(colourMat.channels(), maxChannels);
    }

    /**
     * Test whether getTimeString returns right results.
     */
    @Test
    void getTimeStringTest() {
        assertEquals(Util.getTimeString(TimeUnit.SECONDS.toNanos(1), true), "00:00:01");
        assertEquals(Util.getTimeString(TimeUnit.SECONDS.toNanos(1), false), "00:01");
    }

    /**
     * Verify that imageView is correctly created.
     */
    @Test
    void createImageViewLogoTest() {
        PlatformImpl.startup(() -> { });
        final String logo = "trackscape";
        final int imageViewWidth = 50;
        ImageView iv = Util.createImageViewLogo(logo, imageViewWidth);

        assertEquals(iv.getFitWidth(), imageViewWidth, 0.0);
        assertTrue(iv.isPreserveRatio());
        assertNotNull(iv.getImage());
    }

    /**
     * Test whether bufferedImage is correctly resized.
     */
    @Test
    void resizeBufferedImageTest() {
        final int size = 500;
        BufferedImage bi = new BufferedImage(
            size, size, BufferedImage.TYPE_INT_RGB);
        final int newWidth = 50;
        final int newHeight = 100;
        BufferedImage resizedBi = Util.resizeBufferedImage(bi, newWidth, newHeight);
        assertEquals(resizedBi.getWidth(), newWidth);
        assertEquals(resizedBi.getHeight(), newHeight);
    }
}
