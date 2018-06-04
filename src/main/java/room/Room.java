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
     */
    public void setNextChestOpened() {
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                chest.setApprovedChestFoundByHost(true);
            }
        }
    }
}
