import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String string = "rtsp://192.168.0.117:554/user=admin&password=&channel=1&stream=1.sdp?real_stream--rtp-caching=100";
        String file = "files" + File.separator + "webcast.mov";

        // Load OpenCV library
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_java341.dll");
        //OpenCV.loadShared();

        Mat frame = new Mat();
        VideoCapture camera = new VideoCapture();
        System.out.println(camera.open(string));

        JFrame jframe = new JFrame("MyTitle");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setVisible(true);

        while (true) {
            if (camera.read(frame)) {

                ImageIcon image = new ImageIcon(toBufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();

            }
        }
    }

    static BufferedImage toBufferedImage(Mat videoMatImage) {

        int type = videoMatImage.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
        int bufferSize = videoMatImage.channels() * videoMatImage.cols() * videoMatImage.rows();
        byte[] buffer = new byte[bufferSize];
        videoMatImage.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(videoMatImage.cols(), videoMatImage.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
