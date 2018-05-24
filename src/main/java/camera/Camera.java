package camera;

import java.util.Objects;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Class describing a camera.
 * Camera holds relevant information about data generated by a camera
 */
public class Camera {

    private static final int DEFAULTNUMOFCHESTS = 3;
    private VideoCapture videoCapture;
    private String link;
    private Mat firstFrame;
    private Mat lastFrame = new Mat();
    private boolean changed = false;
    private int numOfChestsInRoom;

    /**
     * Constructor for a camera with possibility to specify no chests and persons.
     *
     * @param newCapture The VideoCapture of this camera.
     * @param newLink The link of this camera.
     * @param numOfChests The amount of chests present in the room.
     */
    public Camera(final VideoCapture newCapture, final String newLink,
                  final int numOfChests) {
        this.videoCapture = newCapture;
        this.link = newLink;
        this.numOfChestsInRoom = numOfChests;
    }

    /**
     * Constructor for a Camera.
     *
     * @param newCapture The VideoCapture of this camera.
     * @param newLink The link of this camera.
     */
    public Camera(final VideoCapture newCapture, final String newLink) {
        this.videoCapture = newCapture;
        this.link = newLink;
        this.numOfChestsInRoom = DEFAULTNUMOFCHESTS;
    }

    /**
     * Gets the last known frame of this camera.
     * @return The frame in Mat format.
     */
    public Mat getLastFrame() {
        Mat newFrame = loadFrame();

        if (newFrame != null && newFrame.rows() != 0 && newFrame.cols() != 0) {
            lastFrame = newFrame;
            changed = true;
        } else {
            changed = false;
        }

        return lastFrame.clone();
    }

    /**
     * Reads the videoCapture to load the new frame.
     * @return The new frame in Mat format.
     */
    private Mat loadFrame() {
        Mat loadFrame = new Mat();
        videoCapture.read(loadFrame);
        return loadFrame;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Camera)) {
            return false;
        }
        Camera camera = (Camera) o;
        return Objects.equals(link, camera.link);
    }

    /**
     * Returns if the frame of the camera is changed.
     * @return If frame is changed.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Get the first frame known of the camera.
     * @return The Mat of the first frame.
     */
    public Mat getFirstFrame() {
        return firstFrame;
    }

    /**
     * Set the first frame of the camera.
     * @param frame The Mat of the new first frame.
     */
    public void setFirstFrame(final Mat frame) {
        this.firstFrame = frame;
    }

    /**
     * Getter for numOfChestsInRoom.
     * @return this.numOfChestsInRoom
     */
    public int getNumOfChestsInRoom() {
        return this.numOfChestsInRoom;
    }
}
