package handlers;

import java.util.LinkedList;
import java.util.Queue;

public class InformationHandler {

    private Queue<String> queue = new LinkedList<>();

    public void addInformation(final String info) {
        queue.add(info);
    }

    public String getInformation() {
        if (queue.isEmpty()) {
            return "empty";
        }
        return queue.poll();
    }
}
