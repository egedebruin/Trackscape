package api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import room.Room;

/**
 * Handler for /chest API calls.
 */
public class APIChestHandler extends AbstractHandler {

    private Room room;

    /**
     * Constructor for the APIChestHandler.
     * @param newRoom the room for this handler.
     */
    public APIChestHandler(final Room newRoom) {
        room = newRoom;
    }

    @Override
    public void handle(final String s, final Request request, final HttpServletRequest httpRequest,
                       final HttpServletResponse response) throws IOException {
        String body = "Nothing happened";

        if (request.getParameter("opened") == null) {
            body = "Parameter opened not found in request";
        } else if (request.getParameter("opened").equals("true")) {
            if (room.getChestList().size() == room.getChestsOpened()) {
                body = "All chests are already opened";
            } else {
                room.setNextChestOpened();
                String log = room.getChestsOpened() + "/" + room.getChestList().size();
                body = "Found chest " + log;
                room.getCameraHandler().getInformationHandler().addInformation("body");
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(body.length());

        PrintWriter out = response.getWriter();
        out.write(body);
        out.close();
    }
}
