package room;

import handlers.CameraHandler;

import handlers.InformationHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describing a room as found in the config file.
 */
public class Room {
    private long id;
    private List<Chest> chestList;
    private int numberOfPeople;
    private long targetDurationInSec;
    private List<String> linkList;
    private int port;
    private InformationHandler informationHandler;

    /**
     * Constructor.
     *
     * @param roomid the room id
     * @param nOPeople  the number of people
     * @param cameraLinks the links for the cameras
     * @param chests a list of chests
     * @param duration the target duration
     * @param portNumber the port number
     */
    public Room(final long roomid, final int nOPeople, final List<String> cameraLinks,
                final List<Chest> chests, final int duration, final int portNumber) {
        id = roomid;
        numberOfPeople = nOPeople;
        linkList = new ArrayList<>();
        linkList.addAll(cameraLinks);
        chestList = chests;
        targetDurationInSec = duration;
        port = portNumber;
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
     * Sets the next section with state TO_BE_OPENED completed.
     */
    public void setNextSectionOpened() {
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                chest.subSectionCompleted();
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
                chest.resetChest();
                while (completedSections > 0) {
                    chest.subSectionCompleted();
                    completedSections--;
                }
            }
        }
    }

    /**
     * Get the amount of chests opened.
     * @return amount of chests opened
     */
    public int getChestsOpened() {
        int chestsOpened = 0;
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.OPENED) {
                chestsOpened++;
            }
        }
        return chestsOpened;
    }

    /**
     * Get the port of the server.
     * @return the port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * Calculate the progress by checking each Chest.
     * For each opened chest the total number of subsections is added to the total.
     * For each to be opened chest the total amount of completed subsections get added to the total.
     * For each waiting for subsection to start chest nothing is added to the total.
     *
     * @return subSectionCount (=total)
     */
    public int calculateSubsectionsDone() {
        int progressMeter = 0;
        // Counts the open chests, chestList is ordered in the following manner:
        // OPENED : TO_BE_OPENED : WAITING_FOR_SECTION_TO_START
        for (Chest chest : chestList) {
            if (chest.getChestState() == Chest.Status.OPENED) {
                progressMeter += chest.getNumberOfSubSections();
            } else if (chest.getChestState() == Chest.Status.TO_BE_OPENED) {
                progressMeter += chest.countSubsectionsCompleted();
            }
        }
        return progressMeter;
    }

    /**
     * Get the total number of subsections.
     * @return the number of subsections
     */
    public int getTotalSubsections() {
        int total = 0;
        for (Chest chest : chestList) {
            total += chest.getNumberOfSubSections();
        }
        return total;
    }

    public List<String> getLinkList() {
        return linkList;
    }

    public InformationHandler getInformationHandler() {
        return informationHandler;
    }

    public void setInformationHandler(InformationHandler informationHandler) {
        this.informationHandler = informationHandler;
    }
}
