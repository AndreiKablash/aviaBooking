package com.htp.avia_booking.controller.command.impl;

import com.htp.avia_booking.controller.command.CommandException;
import com.htp.avia_booking.controller.command.CommandInterface;
import com.htp.avia_booking.controller.command.util.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements CommandInterface {
    private static final ConfigurationManager MANAGER = ConfigurationManager.getInstance();
    private static final String INDEX_PAGE = "/index";
    private static final String ACTION = "action";
    private static final String REDIRECT_ACTION_ATTRIBUTE = "redirect";

    private LogoutCommand(){}

    public static CommandInterface getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommandInterface INSTANCE = new LogoutCommand();
    }

    @Override
    public String execute(HttpServletRequest request,HttpServletResponse response) throws CommandException {
        String page = MANAGER.getProperty("path.page.index");
        HttpSession session = request.getSession();
        session.invalidate();
        request.setAttribute(ACTION, REDIRECT_ACTION_ATTRIBUTE);
        return page;
    }
}