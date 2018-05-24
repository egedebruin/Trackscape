package camera;

import org.junit.jupiter.api.Test;
import org.opencv.core.Rect;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class for testing RectComparator.
 */
class RectComparatorTest {

    private static final int MAGIC20 = 20;
    private static final int MAGIC30 = 30;
    private static final int MAGIC40 = 40;

    /**
     * Tests if rect one is greater than rect two in area.
     */
    @Test
    void compareOneGreateerThanTwo() {
        Rect one = new Rect(1, 2, MAGIC30, MAGIC40);
        Rect two = new Rect(1, 2, MAGIC30, MAGIC20);

        RectComparator rectComparator = new RectComparator();
        assertEquals(rectComparator.compare(one, two), -1);
    }

    /**
     * Tests if rect one is lesser than rect two in area.
     */
    @Test
    void compareOneLesserThanTwo() {
        Rect one = new Rect(1, 2, MAGIC30, MAGIC20);
        Rect two = new Rect(1, 2, MAGIC30, MAGIC40);

        RectComparator rectComparator = new RectComparator();
        assertEquals(rectComparator.compare(one, two), 1);
    }

    /**
     * Tests if rect one is equal to rect two in area.
     */
    @Test
    void compareOneEqualToTwo() {
        Rect one = new Rect(1, 2, MAGIC30, MAGIC40);
        Rect two = new Rect(1, 2, MAGIC40, MAGIC30);

        RectComparator rectComparator = new RectComparator();
        assertEquals(rectComparator.compare(one, two), 0);
    }
}
