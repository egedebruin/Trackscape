import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String string = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        String file = "files" + File.separator + "webcast.mov";

        // Load OpenCV library
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_java341.dll");
        //OpenCV.loadShared();

        Mat frame = new Mat();
        VideoCapture camera = new VideoCapture();
        System.out.println(camera.open(file));

    }
}
