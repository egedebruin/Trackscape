package room;

public class OpenedChest extends Chest {

    private static final int NOSUBSECTION = 1;
    private static final int TARGETTIME = 0;

   public OpenedChest() {
       super(NOSUBSECTION, TARGETTIME);
       setApprovedChestFoundByHost(true);
   }

}
