package camera;

import java.util.Comparator;
import org.opencv.core.Rect;



public class RectComparator implements Comparator<Rect> {


    @Override
    public int compare(Rect o1, Rect o2) {
        if (o1.area() > o2.area()) {
            return -1;
        }
        if (o1.area() == o2.area()) {
            return 0;
        }
        return 1;
    }
}
