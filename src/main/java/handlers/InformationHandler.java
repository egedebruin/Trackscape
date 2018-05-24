package handlers;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Handler for the information to be shown.
 */
public class InformationHandler {

    private Queue<String> queue = new LinkedList<>();

    /**
     * Add information to the queue.
     * @param info The new information.
     */
    public void addInformation(final String info) {
        queue.add(info);
    }

    /**
     * Get information from the queue and remove if it exists, otherwise returns empty.
     * @return The information.
     */
    public String getInformation() {
        if (queue.isEmpty()) {
            return "empty";
        }
        return queue.poll();
    }

    /**
     * Get the full information queue.
     * @return the queue.
     */
    public Queue<String> getQueue() {
        return queue;
    }
}
