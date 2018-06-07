package api;

import gui.controllers.MainController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Handler for /chest API calls.
 */
public class APIChestHandler extends AbstractHandler {

    private MainController controller;

    /**
     * Constructor for the APIChestHandler.
     * @param mainController the mainController for this handler.
     */
    public APIChestHandler(final MainController mainController) {
        controller = mainController;
    }


    @Override
    public void handle(final String s, final Request request, final HttpServletRequest httpRequest,
                       final HttpServletResponse response) throws IOException, ServletException {
        String body = "Ajax altijd nümmer 1";

        if (request.getParameter("opened") == null) {
            body = "Parameter opened not found in request";
        } else if (request.getParameter("opened").equals("true")) {
            if (controller.getRoomController().getProgress().getRoom().getChestsOpened()
                == controller.getRoomController().getProgress().getRoom().getChestList().size()) {
                body = "All chests are already opened";
            } else {
                String log = "";
                if (controller.getConfigured()) {
                    log = controller.getRoomController().confirmedChest();
                }
                controller.getTimeLogController().addInformation("Found chest " + log);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(body.length());

        PrintWriter out = response.getWriter();
        out.write(body);
        out.close();
    }
}
