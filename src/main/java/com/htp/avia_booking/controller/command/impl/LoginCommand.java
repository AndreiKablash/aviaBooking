package com.htp.avia_booking.controller.command.impl;

import com.htp.avia_booking.controller.command.CommandException;
import com.htp.avia_booking.controller.command.CommandInterface;
import com.htp.avia_booking.controller.command.util.ConfigurationManager;
import com.htp.avia_booking.controller.command.util.MessageManager;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.service.ServiceException;
import com.htp.avia_booking.service.interfaces.UserService;
import com.htp.avia_booking.service.impl.UserServiceImpl;
import com.htp.avia_booking.service.validator.ValidationException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginCommand implements CommandInterface {

        private static final UserService SERVICE = UserServiceImpl.getInstance();

        private static final ConfigurationManager MANAGER = ConfigurationManager.getInstance();
        private static final MessageManager MESSAGE = MessageManager.getInstance();

        private static final String LOGIN = "login";
        private static final String PASSWORD = "password";
        private static final String ADMIN_ROLE = "admin";
        private static final String USER_ROLE = "user";
        private static final String SESSION = "sessionName";
        private static final String ACTION = "action";
        private static final String REDIRECT_ACTION_ATTRIBUTE = "redirect";
        private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private LoginCommand(){}

        public static CommandInterface getInstance(){
            return SingletonHolder.INSTANCE;
        }

    private static class SingletonHolder {
            private static final CommandInterface INSTANCE = new LoginCommand();
        }

        @Override
        public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

            String page;
            try {
                User tempUser = new User();
                String login = request.getParameter(LOGIN);
                tempUser.setLogin(login);
                String password = request.getParameter(PASSWORD);
                tempUser.setPassword(password);

                HttpSession session = request.getSession(true);
                User user = SERVICE.authorization(tempUser);

                if(user == null) {
                    request.setAttribute(ACTION,FORWARD_ACTION_ATTRIBUTE);
                    request.setAttribute("errorLoginPassMessage", MESSAGE.getProperty("message.loginerror"));
                    page = MANAGER.getProperty("path.page.login");
                } else {
                    if (user.getRole().listIterator().next().getName().equals(ADMIN_ROLE)) {
                        User admin = new User();
                        admin.setPassword(user.getPassword());
                        admin.setLogin(user.getLogin());
                        admin.setRole(user.getRole());
                        session.setAttribute(ADMIN_ROLE, login);
                        page = MANAGER.getProperty("path.page.adminview");
                    } else {
                        User simpleUser = new User();
                        simpleUser.setRole(user.getRole());
                        simpleUser.setLogin(user.getLogin());
                        simpleUser.setPassword(user.getPassword());
                        simpleUser.setId(user.getId());
                        session.setAttribute(USER_ROLE, login);
                        page = MANAGER.getProperty("path.page.userview");
                    }
                    request.setAttribute(ACTION, REDIRECT_ACTION_ATTRIBUTE);
                }
            } catch (ValidationException e) {
                request.setAttribute(ACTION,FORWARD_ACTION_ATTRIBUTE);
                request.setAttribute("errorLoginPassMessage", MESSAGE.getProperty("message.loginerror"));
                page = MANAGER.getProperty("path.page.login");
            } catch (ServiceException e) {
                throw new CommandException("Command Exception", e);
            }
            return page;
        }
    }

