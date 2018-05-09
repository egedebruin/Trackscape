import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws Exception {
        String string = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        String file = "files" + File.separator + "webcast.mov";

        // Set property to load OpenCV library
        System.setProperty("java.library.path", System.getProperty("user.dir") + File.separator + "libs");
        Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
        fieldSysPath.setAccessible( true );
        fieldSysPath.set( null, null );

        // Load OpenCV library
        nu.pattern.OpenCV.loadShared();

        Mat frame = new Mat();
        VideoCapture camera = new VideoCapture();
        System.out.println(camera.open(file));

    }
}
