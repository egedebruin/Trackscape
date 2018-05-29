package room;

import handlers.CameraHandler;

import java.util.List;

/**
 * Class describing a room as found in the config file.
 */
public class Room {
    private long id;
    private List<Chest> chestList;
    CameraHandler cameraHandler;
    private int numberOfPeople;
    private long startTime;

    public Room(long roomid, int nOPeople, List<String> cameraLinks, List<Chest> chests) {
        id = roomid;
        cameraHandler = new CameraHandler();
        numberOfPeople = nOPeople;
        for (String link: cameraLinks) {
            cameraHandler.addCamera(link,chests.size());
        }
        chestList = chests;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Chest> getChestList() {
        return chestList;
    }

    public void setChestList(List<Chest> chestList) {
        this.chestList = chestList;
    }

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        this.cameraHandler = cameraHandler;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
