import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws IOException, IllegalAccessException, NoSuchFieldException {
        String string = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        String file = "webcast.mov";

        System.setProperty("java.library.path", System.getProperty("user.dir") + File.separator + "libs");
        Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
        fieldSysPath.setAccessible( true );
        fieldSysPath.set( null, null );

        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = new Mat();
        VideoCapture videoCapture = new VideoCapture();
        videoCapture.open(string);

        videoCapture.read(mat);

        System.out.println(mat.cols());
        
    }
}
