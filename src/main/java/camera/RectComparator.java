package camera;

import java.util.Comparator;
import org.opencv.core.Rect;


/**
 * Class representing a comparator between rects.
 */
public class RectComparator implements Comparator<Rect> {


    @Override
    public int compare(final Rect o1, final Rect o2) {
        if (o1.area() > o2.area()) {
            return -1;
        }
        if (o1.area() == o2.area()) {
            return 0;
        }
        return 1;
    }
}
