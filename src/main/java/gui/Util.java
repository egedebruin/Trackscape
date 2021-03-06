package gui;

import java.awt.Graphics2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Class with helper functions.
 */
public final class Util {

    /**
     * Constructor.
     */
    private Util() { }

    /**
     * Converts a Mat to a BufferedImage.
     *
     * @param videoMatImage The frame in Mat.
     * @return The BufferedImage.
     */
    public static BufferedImage matToBufferedImage(final Mat videoMatImage) {
        int type;
        if (videoMatImage.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = videoMatImage.channels()
            * videoMatImage.cols() * videoMatImage.rows();
        byte[] buffer = new byte[bufferSize];
        videoMatImage.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(
            videoMatImage.cols(), videoMatImage.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    /**
     * Convert nano seconds to right time string.
     *
     * @param time Time in nano seconds.
     * @param enableHours Boolean that defines whether hours should be enabled
     * @return Correct time string.
     */
    public static String getTimeString(final long time, final boolean enableHours) {
        final int sixtySeconds = 60;

        int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(time) % sixtySeconds;
        int minutes = (int) TimeUnit.NANOSECONDS.toMinutes(time) % sixtySeconds;
        int hours = (int) TimeUnit.NANOSECONDS.toHours(time);

        String sec = putTimeUnitInString(seconds);
        String min = putTimeUnitInString(minutes);
        String hr = putTimeUnitInString(hours);

        if (enableHours) {
            return hr + ":" + min + ":" + sec;
        } else {
            return min + ":" + sec;
        }
    }

    /**
     * Make a string of the timeUnit (add a 0 when it exists out of only one integer).
     * @param timeUnit seconds, minutes, or hours
     * @return String of the timeUnit
     */
    private static String putTimeUnitInString(final int timeUnit) {
        final int nineSeconds = 9;
        if (timeUnit <= nineSeconds) {
            return "0" + timeUnit;
        } else {
            return Integer.toString(timeUnit);
        }
    }

    /**
     * Create the imageView for a button or label.
     * @param fileName the name of the file
     * @param buttonWidth the width of the button
     * @return ImageView of the logo
     */
    public static ImageView createImageViewLogo(final String fileName, final int buttonWidth) {
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\" + fileName + ".png");
        Image img = new Image(streamEnd.toURI().toString());
        ImageView logo = new ImageView();
        logo.setFitWidth(buttonWidth);
        logo.setPreserveRatio(true);
        logo.setImage(img);
        return logo;
    }

    /**
     * Resize the BufferedImage to fit in the screen.
     * @param bi the BufferedImage
     * @param newWidth the new width
     * @param newHeight the new height
     * @return scaledImage
     */
    public static BufferedImage resizeBufferedImage(final BufferedImage bi,
                                             final int newWidth, final int newHeight) {
        BufferedImage scaledImage = null;
        if (bi != null) {
            scaledImage = new BufferedImage(newWidth, newHeight, bi.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(bi, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }

}
