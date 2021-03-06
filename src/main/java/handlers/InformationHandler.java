package handlers;

import javafx.util.Pair;
import org.opencv.core.Mat;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Handler for the information to be shown.
 */
public class InformationHandler {

    private Queue<String> infQueue = new LinkedList<>();
    private Queue<Pair<Mat, Long>> matQueue = new LinkedList<>();

    /**
     * Add information to the infQueue.
     * @param info The new information.
     */
    public void addInformation(final String info) {
        infQueue.add(info);
    }

    /**
     * Add matrix to the matQueue.
     * @param mat The new matrix.
     */
    public void addMatrix(final Pair<Mat, Long> mat) {
        matQueue.add(mat);
    }

    /**
     * Get information from the infQueue and remove if it exists, otherwise returns empty.
     * @return Information string.
     */
    public String getInformation() {
        if (infQueue.isEmpty()) {
            return "empty";
        }
        return infQueue.poll();
    }

    /**
     * Get matrices from the matQueue and remove if it exists, otherwise returns null.
     * @return matrix.
     */
    public Pair<Mat, Long> getMatrix() {
        if (matQueue.isEmpty()) {
            return null;
        }
        return matQueue.poll();
    }

    /**
     * Get the full information infQueue.
     * @return the infQueue.
     */
    public Queue<String> getInfQueue() {
        return infQueue;
    }

    /**
     * Get the full matrix matQueue.
     * @return the matQueue.
     */
    public Queue<Pair<Mat, Long>> getMatQueue() {
        return matQueue;
    }

    /**
     * Clear the MatQueue.
     */
    public void clearMatQueue() {
        matQueue.clear();
    }
}
