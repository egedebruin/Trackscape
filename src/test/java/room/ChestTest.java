package room;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the Chest class.
 */
class ChestTest {

    private static final long TARGETTIME = 120;
    private Chest preceedingChest = new OpenedChest();

    /**
     * Test for the updateStatus method, every transition is tested.
     */
    @Test
    void updateStatusTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        chest.updateStatus(preceedingChest);
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.updateStatus(preceedingChest);
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus(preceedingChest);
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
        chest.updateStatus(preceedingChest);
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test for the getChestState method.
     */
    @Test
    void getChestStateTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
    }

    /**
     * Test for the countSubsections method.
     */
    @Test
    void countSubsectionsTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.countSubsectionsCompleted(), 0);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }

    /**
     * Test for the getNumberOfSubsections method.
     */
    @Test
    void getNumberOfSubSectionsTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getNumberOfSubSections(), 1);
        Chest chest2 = new Chest(-1, TARGETTIME);
        assertEquals(chest2.getNumberOfSubSections(), 1);
    }

    /**
     * Test for the countSubsectionsCompleted method.
     */
    @Test
    void subSectionCompletedTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.countSubsectionsCompleted(), 0);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }
}
