package room;

import handlers.CameraHandler;

import java.util.List;

/**
 * Class describing a room as found in the config file.
 */
public class Room {
    private long id;
    private List<Chest> chestList;
    private CameraHandler cameraHandler;
    private int numberOfPeople;
    private long targetDurationInSec;
    private long startTime;
    private int chestsOpened;

    /**
     * Constructor.
     *
     * @param roomid the room id
     * @param nOPeople  the number of people
     * @param cameraLinks the links for the cameras
     * @param chests a list of chests
     * @param duration the target duration
     */
    public Room(final long roomid, final int nOPeople, final List<String> cameraLinks,
                final List<Chest> chests, final int duration) {
        id = roomid;
        cameraHandler = new CameraHandler();
        numberOfPeople = nOPeople;
        for (String link: cameraLinks) {
            cameraHandler.addCamera(link, chests.size());
        }
        chestList = chests;
        targetDurationInSec = duration;
        startTime = System.currentTimeMillis();
    }

    /**
     * Getter id.
     * @return id.
     */
    public long getId() {
        return id;
    }

    /**
     * Set id.
     * @param ids the id
     */
    public void setId(final long ids) {
        this.id = ids;
    }

    /**
     * Get chestList.
     * @return chestlist
     */
    public List<Chest> getChestList() {
        return chestList;
    }

    /**
     * Set chestList.
     * @param chestsList the chestList
     */
    public void setChestList(final List<Chest> chestsList) {
        this.chestList = chestsList;
    }

    /**
     * Get cameraHandler.
     * @return cameraHandler
     */
    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    /**
     * Set cameraHandler.
     * @param camerasHandler the camerahandler
     */
    public void setCameraHandler(final CameraHandler camerasHandler) {
        this.cameraHandler = camerasHandler;
    }

    /**
     * Get numberOfPeople.
     * @return numberOfPeople
     */
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    /**
     * Set numberOfPeople.
     * @param numbersOfPeople the number of people
     */
    public void setNumberOfPeople(final int numbersOfPeople) {
        this.numberOfPeople = numbersOfPeople;
    }

    /**
     * Get targetDuration in seconds.
     * @return targetDuration
     */
    public long getTargetDuration() {
        return targetDurationInSec;
    }

    /**
     * Set targetDuration in seconds.
     * @param startsTime the starting time
     */
    public void setTargetDuration(final long startsTime) {
        this.targetDurationInSec = startsTime;
    }

    /**
     * Update the state of the escape room room.
     */
    public void updateRoom() {
        Chest previousChest = new OpenedChest();
        for (Chest chest : chestList) {
            chest.updateStatus(previousChest);
            previousChest = chest;
        }
    }

    /**
     * Sets the next chest with state TO_BE_OPENED OPENED, by approving it to be opened.
     * @param timestamp the timestamp when the chest was opened
     */
    public void setNextChestOpened(final long timestamp) {
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                chest.setApprovedChestFoundByHost(true);
                chest.setTimeFound(timestamp);
                break;
            }
        }
    }

    /**
     * Sets the chests and subsections completed up till the subsection at completedSections.
     * @param completedSubSections number of completed sections
     */
    public void setChestSectionsCompletedTill(final int completedSubSections) {
        int completedSections = completedSubSections;
        for (Chest chest : chestList) {
            if (chest.getNumberOfSubSections() <= completedSections) {
                chest.setApprovedChestFoundByHost(true);
                if (chest.getTimeFound() < 0) {
                    chest.setTimeFound(System.nanoTime());
                }
                completedSections -= chest.getNumberOfSubSections();
            } else {
                while (completedSections > 0) {
                    chest.resetChest();
                    chest.subSectionCompleted();
                    completedSections--;
                }
            }
        }
    }

    /**
     * Sets all chests up till completedsections.
     * used when chests need to make negative progress.
     * @param completedSections number of completed sections
     */
    public void unsetChestSectionsCompletedTill(final int completedSections) {
        for (Chest chest : chestList) {
            chest.resetChest();
        }
        setChestSectionsCompletedTill(completedSections);
    }

    /**
     * Set allChestsDetected variable of cameraHandler
     * on true when all chests have been detected.
     * @param detectedAllChests boolean that says whether all chests are detected
     */
    public void setAllChestsDetected(final boolean detectedAllChests) {
        cameraHandler.setAllChestsDetected(detectedAllChests);
    }

    /**
     * Get the amount of chests opened.
     * @return amount of chests opened
     */
    public int getChestsOpened() {
        chestsOpened = 0;
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.OPENED) {
                chestsOpened++;
            }
        }
        return chestsOpened;
    }
}
