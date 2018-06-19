package room;

/**
 * Class that represents an opened chest.
 */
public class OpenedChest extends Chest {

    private static final int NO_SUBSECTION = 1;
    private static final int TARGET_TIME = 0;
    private static final int WARNING_TIME = 0;

    /**
     * Constructor.
     */
   public OpenedChest() {
       super(NO_SUBSECTION, TARGET_TIME, WARNING_TIME);
       setApprovedChestFoundByHost();
   }

}
