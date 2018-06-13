package api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import room.Room;

/**
 * Handler for /section API calls.
 */
public class APISectionHandler extends AbstractHandler {

    private Room room;

    /**
     * Constructor for the APISectionHandler.
     * @param newRoom the room for this handler.
     */
    public APISectionHandler(final Room newRoom) {
        room = newRoom;
    }

    @Override
    public void handle(final String s, final Request request, final HttpServletRequest httpRequest,
                       final HttpServletResponse response) throws IOException {
        String body = "Nothing happened";

        if (request.getParameter("completed") == null) {
            body = "Parameter completed not found in request";
        } else if (request.getParameter("completed").equals("true")) {
            if (room.getChestList().size() == room.getChestsOpened()) {
                body = "All chests are already opened";
            } else {
                room.setNextSectionOpened();
                String log = room.calculateSubsectionsDone() + "/" + room.getTotalSubsections();
                body = "Completed section " + log;
                room.getCameraHandler().getInformationHandler().addInformation(body);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(body.length());

        PrintWriter out = response.getWriter();
        out.write(body);
        out.close();
    }
}
