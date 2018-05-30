package room;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChestTest {

    private static final long TARGETTIME = 120;

    @Test
    void updateStatusTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        chest.updateStatus();
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.updateStatus();
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus();
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
        chest.updateStatus();
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
    }

    @Test
    void getChestStateTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
    }

    @Test
    void countSubsectionsTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.countSubsectionsCompleted(), 0);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }

    @Test
    void getNumberOfSubSectionsTest() {
        Chest chest = new Chest(5, TARGETTIME);
        assertEquals(chest.getNumberOfSubSections(), 5);
        Chest chest2 = new Chest(-5, TARGETTIME);
        assertEquals(chest2.getNumberOfSubSections(), 1);
    }

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
