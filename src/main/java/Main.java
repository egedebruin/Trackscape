import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String string = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov";
        String file = "webcast.mov";
        System.load(System.getProperty("user.dir") + "\\libs\\" + Core.NATIVE_LIBRARY_NAME + ".dll");

        Mat mat = new Mat();
        VideoCapture videoCapture = new VideoCapture();
        videoCapture.open(string);

        videoCapture.read(mat);

        System.out.println(mat.cols());
        
    }
}
