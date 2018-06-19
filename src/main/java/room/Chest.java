package room;

import org.opencv.core.MatOfPoint;

import static room.Chest.Status.WAITING_FOR_SECTION_TO_START;

/**
 * Class describing a Chest as the can be found in an escape room.
 */
public class Chest {

    /**
     * Enum of statuses.
     */
    public enum Status {
        /**
         * The chest is opened.
         */
        OPENED,
        /**
         * The chest should be opened.
         */
        TO_BE_OPENED,
        /**
         * The chest before this one is not opened yet.
         */
        WAITING_FOR_SECTION_TO_START;
    }

    private Status chestState;
    private MatOfPoint positionInFrame;
    private long targetDurationInSec;
    private long warningTimeInSec;
    private long timeFound = -1;
    private int numberOfSubSections;
    private boolean[] subsectionCompleted;
    private boolean approvedChestFoundByHost;

    /**
     * Constructor.
     *
     * @param noSubsections The amount of subsections to be completed by the players
     *                      before this chest should be found.
     * @param targetTimeInSeconds Time in seconds regarding the target time,
     *                            when peolpe take longer than this time they might enter a
     *                            critical stage.
     * @param warningTime The time where a warning needs to be given to the user.
     */
    public Chest(final int noSubsections, final long targetTimeInSeconds, final long warningTime) {
        numberOfSubSections = Math.max(noSubsections, 1);
        subsectionCompleted = new boolean[numberOfSubSections];
        targetDurationInSec = targetTimeInSeconds;
        warningTimeInSec = warningTime;
        chestState = WAITING_FOR_SECTION_TO_START;

    }

    /**
     * Method that resets teh chest to WAITING_FOR_SUBSECTION_TO_START.
     */
    public void resetChest() {
        chestState = WAITING_FOR_SECTION_TO_START;
        subsectionCompleted = new boolean[numberOfSubSections];
        approvedChestFoundByHost = false;
        timeFound = -1;
    }

    /**
     * Method used for updating the status of the Chest.
     *
     * status can switch    from:           to:
     *                      Waiting         To_Be_Opened
     *                      To_be_Opened    Opened
     *
     */
    private void updateStatus() {
        if (chestState == WAITING_FOR_SECTION_TO_START) {
            chestState = Status.TO_BE_OPENED;
            positionInFrame = new MatOfPoint();
            timeFound = -1;
        } else if (chestState == Status.TO_BE_OPENED
            && (approvedChestFoundByHost || countSubsectionsCompleted() == numberOfSubSections)) {
            chestState = Status.OPENED;
            if (timeFound < 0) {
                timeFound = System.nanoTime();
            }
        }
    }

    /**
     * Method to update a status of a chest when the preceding chest is opened.
     *
     * when preceding chest is opened, status can switch
     * from:           to:
     * Waiting         To_Be_Opened
     * To_be_Opened    Opened
     *
     * @param previousChest preceding chest
     */
    public void updateStatus(final Chest previousChest) {
        if (previousChest.getChestState() == Status.OPENED) {
            updateStatus();
        }
    }

    /**
     * Get the chest's status.
     * @return this.cestState
     */
    public Status getChestState() {
        return chestState;
    }

    /**
     * Loops over the subsections and count the amount of trues.
     * @return the amount of subsections which are completed
     */
    public int countSubsectionsCompleted() {
        // If the chest is opened all subsections are completed.
        if (chestState == Status.OPENED) {
            return numberOfSubSections;
        }

        int count = 0;
        for (boolean subsection : subsectionCompleted) {
            if (subsection) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get numberOfSubsections.
     *
     * @return this.numberOfSubSections
     */
    public int getNumberOfSubSections() {
        return numberOfSubSections;
    }

    /**
     * Setter for approvedChestFoundByHost.
     */
    public void setApprovedChestFoundByHost() {
        approvedChestFoundByHost = true;
        chestState = Status.OPENED;
    }

    /**
     * Method that sets the next subsection to completed.
     */
    public void subSectionCompleted() {
        subsectionCompleted[Math.min(countSubsectionsCompleted(), numberOfSubSections - 1)] = true;
    }

    /**
     * Get the target duration of a chest in seconds.
     * @return the target duration in seconds
     */
    public long getTargetDurationInSec() {
        return targetDurationInSec;
    }

    /**
     * Get the time of how long it took to find this chest.
     * @return the time
     */
    public long getTimeFound() {
        return timeFound;
    }

    /**
     * Set the timeFound.
     * @param timestamp timestamp in nanotime
     */
    public void setTimeFound(final long timestamp) {
        this.timeFound = timestamp;
    }

    /**
     * Get the warning time.
     * @return the time in seconds
     */
    public long getWarningTimeInSec() {
        return warningTimeInSec;
    }
}
