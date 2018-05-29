package room;

import handlers.CameraHandler;

import java.util.List;

/**
 * Class describing a room as found in the config file.
 */
public class Room {
    private int id;
    private List<Chest> chestList;
    private CameraHandler cameraHandler;
    private int numberOfPeople;
    private long startTime;

    /**
     * Constructor.
     *
     * @param roomid the room id
     * @param nOPeople  the number of people
     * @param cameraLinks the links for the cameras
     * @param chests a list of chests
     */
    public Room(int roomid, int nOPeople, List<String> cameraLinks, List<Chest> chests) {
        id = roomid;
        cameraHandler = new CameraHandler();
        numberOfPeople = nOPeople;
        for (String link: cameraLinks) {
            cameraHandler.addCamera(link, chests.size());
        }
        chestList = chests;
    }

    /**
     * Getter id.
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set id.
     * @param ids the id
     */
    public void setId(int ids) {
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
     * Get startTime.
     * @return startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Set startTime.
     * @param startsTime the starting time
     */
    public void setStartTime(long startsTime) {
        this.startTime = startsTime;
    }
}
