package handlers;

import gui.controllers.MainController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class APIChestHandler extends AbstractHandler {

    MainController controller;

    public APIChestHandler (MainController mainController) {
        controller = mainController;
    }


    @Override
    public void handle(String s, Request request, HttpServletRequest httpRequest,
                       HttpServletResponse response) throws IOException, ServletException {
        String body = "Ajax altijd nümmer 1";

        if (request.getParameter("opened") == null) {
            body = "Parameter opened not found in request";
        }
        else if (request.getParameter("opened").equals("true")) {
            String log = "";
            if (controller.getConfigured()) {
                log = controller.getRoomController().confirmedChest();
            }
            controller.getTimeLogController().addInformation("Found chest " + log);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentLength(body.length());

        PrintWriter out = response.getWriter();
        out.write(body);
        out.close();
    }
}
