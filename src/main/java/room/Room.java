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

    /**
     * Constructor.
     *
     * @param roomid the room id
     * @param nOPeople  the number of people
     * @param cameraLinks the links for the cameras
     * @param chests a list of chests
     * @param duration the target duration
     */
    public Room(long roomid, int nOPeople, List<String> cameraLinks, List<Chest> chests,
                int duration) {
        id = roomid;
        cameraHandler = new CameraHandler();
        numberOfPeople = nOPeople;
        for (String link: cameraLinks) {
            cameraHandler.addCamera(link, chests.size());
        }
        chestList = chests;
        targetDurationInSec = duration;
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
    public void setId(long ids) {
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
    public void setChestList(List<Chest> chestsList) {
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
    public void setCameraHandler(CameraHandler camerasHandler) {
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
    public void setNumberOfPeople(int numbersOfPeople) {
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
    public void setTargetDuration(long startsTime) {
        this.targetDurationInSec = startsTime;
    }
}
