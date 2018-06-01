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
        subSectionCount = 0;
        // Counts the open chests, chestList is ordened in the following manner:
        // OPENED : TO_BE_OPENED : WAITING_FOR_SECTION_TO_START
        for (Chest chest : room.getChestList()) {
            if (chest.getChestState() == Chest.Status.OPENED) {
                subSectionCount += chest.getNumberOfSubSections();
            } else if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                subSectionCount = chest.countSubsectionsCompleted();
            }
        }
        return subSectionCount;
    }

    /**
     * Get the room of this progress.
     * @return The room.
     */
    public Room getRoom() {
        return room;
    }
}
