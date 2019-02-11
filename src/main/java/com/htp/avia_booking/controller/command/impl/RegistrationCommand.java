package com.htp.avia_booking.controller.command.impl;

import com.htp.avia_booking.controller.command.CommandException;
import com.htp.avia_booking.controller.command.CommandInterface;
import com.htp.avia_booking.controller.command.util.ConfigurationManager;
import com.htp.avia_booking.controller.command.util.MessageManager;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.domain.source.objects.Role;
import com.htp.avia_booking.service.impl.UserServiceImpl;
import com.htp.avia_booking.service.interfaces.UserService;
import com.htp.avia_booking.service.validator.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RegistrationCommand implements CommandInterface {

    private static final String USER_ROLE = "user";
    private static final String USER_ROLE_DESCRIPTION = "simple user";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String SURNAME_ATTRIBUTE = "surname";
    private static final String DOCUMENT_ID_ATTRIBUTE = "documentId";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String LOGIN_ATTRIBUTE = "login";
    private static final String PASSWORD_ATTRIBUTE = "password";
    private static final String PHONE_ATTRIBUTE = "phone";
    private static final String ACTION = "action";
    private static final String REDIRECT_ACTION_ATTRIBUTE = "redirect";
    private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private static final UserService SERVICE = UserServiceImpl.getInstance();
    private static final ConfigurationManager MANAGER = ConfigurationManager.getInstance();
    private static final MessageManager MESSAGE = MessageManager.getInstance();

    private RegistrationCommand() {
    }

    public static CommandInterface getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommandInterface INSTANCE = new RegistrationCommand();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

        String page;
        try {
            String name = request.getParameter(NAME_ATTRIBUTE);
            String surname = request.getParameter(SURNAME_ATTRIBUTE);
            String documentId = request.getParameter(DOCUMENT_ID_ATTRIBUTE);
            String email = request.getParameter(EMAIL_ATTRIBUTE);
            String login = request.getParameter(LOGIN_ATTRIBUTE);
            String password = request.getParameter(PASSWORD_ATTRIBUTE);
            String phone = request.getParameter(PHONE_ATTRIBUTE);

            User user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setDocumentId(documentId);
            user.setEmail(email);
            user.setLogin(login);
            user.setPassword(password);
            user.setPhone(phone);
            List<Role> roleList = new ArrayList<>();
            Role roleUser = new Role();
            roleUser.setName(USER_ROLE);
            roleUser.setDescription(USER_ROLE_DESCRIPTION);
            roleList.add(roleUser);
            user.setRole(roleList);

            User resultUser = SERVICE.create(user);
            if (resultUser == null) {
                request.setAttribute(ACTION, FORWARD_ACTION_ATTRIBUTE);
                request.setAttribute("errorRegistrationPassMessage", MESSAGE.getProperty("message.registrationerror"));
                page = MANAGER.getProperty("path.page.registration");
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute(USER_ROLE, login);
                page = MANAGER.getProperty("path.page.userview");
                request.setAttribute(ACTION, REDIRECT_ACTION_ATTRIBUTE);

            }
        } catch (ValidationException e) {
            request.setAttribute(ACTION, FORWARD_ACTION_ATTRIBUTE);
            request.setAttribute("errorRegistrationPassMessage", MESSAGE.getProperty("message.registrationerror"));
            page = MANAGER.getProperty("path.page.registration");
        } catch (Exception e) {
            throw new CommandException("Command Exception", e);
        }
        return page;
    }
}

