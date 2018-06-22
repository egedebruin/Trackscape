package room;

import api.APIHandler;
import handlers.JsonHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describing the progress in a room.
 */
public class Progress {
    private Room room;
    private int subSectionCount;
    private APIHandler apiHandler;

    /**
     * Constructor.
     * @param configfile configuration file where the room,
     *                   for which progress needs to be computed, is held in.
     * @param roomid the id of the room.
     */
    public Progress(final String configfile, final int roomid) {
        room = new JsonHandler(configfile).createRooms().get(roomid);
        init();
    }

    /**
     * Constructor.
     * @param configfile configuration file where the room,
     *                   for which progress needs to be computed, is held in.
     */
    public Progress(final String configfile) {
        room = new JsonHandler(configfile).createSingleRoom();
        init();
    }

    /**
     * Constructor.
     * @param players the amount of players in the game
     * @param totalDuration the total duration of the game in seconds
     * @param sectionList the list with the amount of sections per chest
     * @param durationList the list with the duration for each chest
     * @param warningList the list with warning time of each chest
     */
    public Progress(final int players, final int totalDuration, final List<Integer> sectionList,
                    final List<Integer> durationList, final List<Integer> warningList) {
        ArrayList<Chest> chestList = new ArrayList<>();
        int chests = sectionList.size();
        for (int i = 0; i < chests; i++) {
            Chest chest = new Chest(sectionList.get(i), durationList.get(i), warningList.get(i));
            chestList.add(chest);
        }
        final int roomId = 36;
        final int portNumber = 8080;
        room = new Room(roomId, players, new ArrayList<>(), chestList,
            totalDuration, portNumber);
        init();
    }

    /**
     * Initialize the progress.
     */
    private void init() {
        subSectionCount = 0;

        apiHandler = new APIHandler(room);
        int port = room.getPort();
        apiHandler.setServer(port);

        apiHandler.startServer();
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
        return room.calculateSubsectionsDone();
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
     * Get subsectioncount.
     * @return this.subsectioncount
     */
    public int getSubSectionCount() {
        return subSectionCount;
    }

    /**
     * Stop the apiServer.
     */
    public void stopServer() {
        apiHandler.stopServer();
    }

    /**
     * Configure a new progress when clicked on progressBar.
     * @param index the index of new progress
     * @return the new amount of chests opened
     */
    public int newProgress(final int index) {
        int completedSections = getSubSectionCountFromBarIndex(index);
        setSubSectionCount(completedSections);
        room.setChestSectionsCompletedTill(completedSections);
        updateProgress();
        return room.getChestsOpened();
    }

    /**
     * Check if all chests are opened.
     * @return true if all chests are opened, false otherwise
     */
    public boolean allChestsOpened() {
        return room.getChestList().size() == room.getChestsOpened();
    }

    /** Method for when a chest if confirmed by the host.
     * @param timestamp the timestamp when the chest was opened
     * @return string format of how many chests are opened
     */
    public String confirmedChestString(final long timestamp) {
        room.setNextChestOpened(timestamp);
        return room.getChestsOpened() + "/" + room.getChestList().size();
    }

    /**
     * Get the ApiHandler.
     * @return the APIHandler
     */
    public APIHandler getApiHandler() {
        return apiHandler;
    }
}
