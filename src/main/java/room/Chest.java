package room;

import org.opencv.core.MatOfPoint;

import java.util.concurrent.TimeUnit;

/**
 * Class describing a Chest as the can be found in an escape room.
 */
public class Chest {

    /**
     * Enum of statuses.
     */
    private enum Status {
        OPENED, TO_BE_OPENED, WAITING_FOR_SECTION_TO_START;
    }
    private Status chestState;
    private MatOfPoint positionInFrame;
    private long targetDurationInSec;
    private long beginOfSectionTimeInSec;
    private long timeFound;
    private int numberOfSubSections;
    private boolean approvedChestFoundByHost;

    /**
     * Constructor.
     *
     * @param noSubsections The amount of subsections to be completed by the players
     *                      before this chest should be found.
     * @param targetTimeInSeconds Time in seconds regarding the target time,
     *                            when peolpe take longer than this time they might enter a
     *                            critical stage.
     */
    public Chest(int noSubsections, long targetTimeInSeconds) {
        numberOfSubSections = noSubsections;
        targetDurationInSec = targetTimeInSeconds;
        chestState = Status.WAITING_FOR_SECTION_TO_START;
    }

    /**
     * Method used for updating the status of the Chest.
     *
     * status can go    from:           to:
     *                  Waiting         To_Be_Opened
     *                  To_be_Opened    Opened
     *
     * @return the current Status of the chest.
     */
    public Status updateStatus() {
        if (chestState == Status.WAITING_FOR_SECTION_TO_START) {
            chestState = Status.TO_BE_OPENED;
            beginOfSectionTimeInSec = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
            positionInFrame = new MatOfPoint();
        } else if (chestState == Status.TO_BE_OPENED && approvedChestFoundByHost) {
            chestState = Status.OPENED;
            timeFound = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()) - beginOfSectionTimeInSec;
        }
        return chestState;
    }

    

}
