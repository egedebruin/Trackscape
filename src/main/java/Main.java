public class Main {
    /**
     * Skeleton main.
     * @param args args
     */
    public static void main(String[] args) {

        // Link to a live streaming camera.
        String string = "rtsp://192.168.0.117:554/"
            + "user=admin&password=&channel=1&stream=1.sdp?real_stream--rtp-caching=100";

        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir") + "\\libs\\opencv_java341.dll");

    }
}
