package com.htp.avia_booking.controller;

import com.htp.avia_booking.controller.command.CommandException;
import com.htp.avia_booking.controller.command.CommandHelper;
import com.htp.avia_booking.controller.command.CommandInterface;
import com.htp.avia_booking.controller.command.util.ConfigurationManager;
import com.htp.avia_booking.controller.command.util.MessageManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getRootLogger();
    private static final String ERROR_TAG = "error";
    private static final String ACTION = "action";
    private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private final CommandHelper helper = new CommandHelper();
    private final ConfigurationManager MANAGER = ConfigurationManager.getInstance();
    private final MessageManager MESSAGE = MessageManager.getInstance();

    public FrontController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String page;

        CommandInterface action = helper.getCommand(request);

        if (action != null) {
            try {
                page = action.execute(request, response);

                if (page != null) {
                    if (request.getAttribute(ACTION).equals(FORWARD_ACTION_ATTRIBUTE)) {
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                        dispatcher.forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + page);
                    }
                } else {
                    page = MANAGER.getProperty("path.page.index");
                    request.getSession().setAttribute("nullPage",
                            MESSAGE.getProperty("message.nullpage"));
                    response.sendRedirect(request.getContextPath() + page);
                }
            } catch (CommandException e) {
                LOGGER.error("CommandException", e);
                response.sendRedirect(request.getContextPath() + ERROR_TAG);
            }
        }
    }
}
