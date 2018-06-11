package api;

import gui.controllers.MainController;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
                       final HttpServletResponse response) throws IOException {
        String body = "Nothing happened";

        if (request.getParameter("opened") == null) {
            body = "Parameter opened not found in request";
        } else if (request.getParameter("opened").equals("true")) {
            if (controller.getRoomController().allChestsOpened()) {
                body = "All chests are already opened";
            } else {
                String log = "";
                if (controller.getConfigured()) {
                    log = controller.getRoomController().confirmedChestString(System.nanoTime());
                }
                controller.getTimeLogController().addInformation("Found chest " + log);
                body = "Found chest " + log;
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(body.length());

        PrintWriter out = response.getWriter();
        out.write(body);
        out.close();
    }
}
