import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        String string = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        String file = "files" + File.separator + "webcast.mov";

        // Set property to load OpenCV library
        System.setProperty("java.library.path", System.getProperty("user.dir") + File.separator + "libs");
        Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
        fieldSysPath.setAccessible( true );
        fieldSysPath.set( null, null );

        Scanner sc = new Scanner(file);
        sc.next();

        // Load OpenCV library
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat frame = new Mat();
        VideoCapture camera = new VideoCapture();
        camera.open(file);
        
    }
}
