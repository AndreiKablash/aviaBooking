package com.htp.avia_booking.controller.command.impl.admin;

import com.htp.avia_booking.controller.command.CommandException;
import com.htp.avia_booking.controller.command.CommandInterface;
import com.htp.avia_booking.controller.command.util.ConfigurationManager;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.service.ServiceException;
import com.htp.avia_booking.service.impl.UserServiceImpl;
import com.htp.avia_booking.service.interfaces.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class ViewUsersCommand implements CommandInterface {

        private static final UserService SERVICE = UserServiceImpl.getInstance();

        private static final ConfigurationManager MANAGER = ConfigurationManager.getInstance();

        private static final String ACTION = "action";
        private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private ViewUsersCommand(){}

        public static CommandInterface getInstance(){
            return SingletonHolder.INSTANCE;
        }

    private static class SingletonHolder {
            private static final CommandInterface INSTANCE = new ViewUsersCommand();
        }

        @Override
        public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
            String page;
            try {
                List<User> userList = SERVICE.viewAll();
                request.setAttribute("userList", userList);
                page = MANAGER.getProperty("path.page.admin_all_users_view");
                request.setAttribute(ACTION, FORWARD_ACTION_ATTRIBUTE);
            } catch (ServiceException e) {
                throw new CommandException("Command Exception", e);
            }
            return page;
    }
}

