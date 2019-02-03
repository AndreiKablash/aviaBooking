package com.htp.avia_booking.controller.impl;

import com.htp.avia_booking.controller.util.CommandException;
import com.htp.avia_booking.controller.util.CommandInterface;
import com.htp.avia_booking.dao.DaoException;
import com.htp.avia_booking.dao.factory.DaoFactory;
import com.htp.avia_booking.domain.source.objects.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class TestCommand implements CommandInterface {
    private static final String ACTION = "action";
    private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private TestCommand() {
    }

    public static CommandInterface getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Method performs the procedure for adding hotel room information on page and further viewing and updating
     * Also determines what action must be made for transition(forward or sendRedirect)
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return the path to go to a specific page
     * @throws CommandException when getting all nodes fail
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
      //  request.setAttribute("testList", Arrays.asList("Java", "C#", "GOLang"));
        try {
            request.setAttribute("testRole", Arrays.asList(DaoFactory.getDaoFactory().getRoleDao().getAll()));
        } catch (DaoException e) {
            e.printStackTrace();
        }
        request.setAttribute(ACTION, FORWARD_ACTION_ATTRIBUTE);

        return "/";
    }

    private static class SingletonHolder {
        private static final CommandInterface INSTANCE = new TestCommand();
    }
}
