package room;

/**
 * Class that represents an opened chest.
 */
public class OpenedChest extends Chest {

    private static final int NOSUBSECTION = 1;
    private static final int TARGETTIME = 0;

    /**
     * Constructor.
     */
   public OpenedChest() {
       super(NOSUBSECTION, TARGETTIME);
       setApprovedChestFoundByHost();
   }

}
