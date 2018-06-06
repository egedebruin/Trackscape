package room;

import handlers.JsonHandler;

/**
 * Class describing the progress in a room.
 */
public class Progress {
    private Room room;
    private int subSectionCount;

    /**
     * Constructor.
     * @param configfile configuration file where the room,
     *                   for which progress needs to be computed, is held in.
     * @param roomid the id of the room.
     */
    public Progress(final String configfile, final int roomid) {
        room = new JsonHandler(configfile).createRooms().get(roomid);
        subSectionCount = 0;
    }

    /**
     * Constructor.
     * @param configfile configuration file where the room,
     *                   for which progress needs to be computed, is held in.
     */
    public Progress(final String configfile) {
        room = new JsonHandler(configfile).createSingleRoom();
        subSectionCount = 0;
    }

    /**
     * Calculate the progress by checking each Chest.
     * For each opened chest the total number of subsections is added to the total.
     * For each to be opened chest the total amount of completed subsections get added to the total.
     * For each waiting for subsection to start chest nothing is added to the total.
     *
     * @return subSectionCount (=total)
     */
    public int calculateProgress() {
        int progressMeter = 0;
        // Counts the open chests, chestList is ordered in the following manner:
        // OPENED : TO_BE_OPENED : WAITING_FOR_SECTION_TO_START
        for (Chest chest : room.getChestList()) {
            if (chest.getChestState() == Chest.Status.OPENED) {
                progressMeter += chest.getNumberOfSubSections();
            } else if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                progressMeter += chest.countSubsectionsCompleted();
            }
        }
        return progressMeter;
    }

    /**
     * Get the room of this progress.
     * @return The room.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Get the total amount of sections.
     * @return Number of sections.
     */
    public int getTotalSections() {
        int total = 0;
        for (Chest chest : room.getChestList()) {
            total += chest.getNumberOfSubSections();
        }
        return total;
    }

    /**
     * Update the progress of the escape room.
     */
    public void updateProgress() {
        room.updateRoom();
        subSectionCount = calculateProgress();
    }

    /**
     * Calculates the no subsections that are completed according to the progressbar.
     * @param progressBarIndex the index
     * @return the number of subsections up till index
     */
    public int getSubSectionCountFromBarIndex(final int progressBarIndex) {
        subSectionCount = (progressBarIndex / 2) + 1;
        return subSectionCount;
    }

    /**
     * Calculates the position to where the progressbar needs to be filled.
     * @return the index to where the progressbar needs to be filled.
     */
    public int getFillCount() {
        return (calculateProgress() - 1) * 2;
    }

    /**
     * Set the no subsections.
     * @param newSubSectionCount the new no subsections
     */
    public void setSubSectionCount(final int newSubSectionCount) {
        this.subSectionCount = newSubSectionCount;
    }

    /**
     * GEt subsectioncount.
     * @return this.subsectioncount
     */
    public int getSubSectionCount() {
        return subSectionCount;
    }
}
