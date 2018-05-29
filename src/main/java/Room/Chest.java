package Room;

import org.opencv.core.MatOfPoint;

public class Chest {
    private enum status {
        OPENED, TO_BE_OPENED, WAITING_FOR_SECTION_TO_START;
    }
    private MatOfPoint positionInFrame;
    private long targetDuration;

}
