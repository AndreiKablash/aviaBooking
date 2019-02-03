package com.htp.avia_booking.controller.impl;

import com.htp.avia_booking.controller.util.CommandException;
import com.htp.avia_booking.controller.util.CommandInterface;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.service.UserService;
import com.htp.avia_booking.service.impl.UserServiceImpl;
import com.htp.avia_booking.service.validator.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegistrationCommand implements CommandInterface {
    private static final UserService SERVICE = UserServiceImpl.getInstance();
  //  private static final PagesConfigManager MANAGER = PagesConfigManager.getInstance();

    private static final String USER_ROLE = "user";
    private static final String CLIENT_ROLE = "client";

    private static final String PASSPORT_ID_ATTRIBUTE = "id_passport";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String SURNAME_ATTRIBUTE = "surname";
    private static final String SECONDNAME_ATTRIBUTE = "second_name";
    private static final String EMAIL = "email";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String ACTION = "action";
    private static final String REDIRECT_ACTION_ATTRIBUTE = "redirect";
    private static final String FORWARD_ACTION_ATTRIBUTE = "forward";

    private static final String ERROR_FLAG = "errorFlag";
    private static final int ERROR_FLAG_VALUE = 1;
    private static final int ERROR_FLAG_VALUE_2 = 2;

    private RegistrationCommand(){}

    public static CommandInterface getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommandInterface INSTANCE = new RegistrationCommand();
    }

    /**
     * Method performs the procedure for adding a new customer to the system.
     * Getting all information about new client and then create new node in system.
     * Also determines what action must be made for transition(forward or sendRedirect).
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return the path to go to a specific page
     * @throws CommandException when creating fail
     */

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page =null;
        try {
//            String idPassport = request.getParameter(PASSPORT_ID_ATTRIBUTE);
//            String name = request.getParameter(NAME_ATTRIBUTE);
//            String surname = request.getParameter(SURNAME_ATTRIBUTE);
//            String secondName = request.getParameter(SECONDNAME_ATTRIBUTE);
//            String email = request.getParameter(EMAIL);
//            String role = USER_ROLE;

            String login = request.getParameter(LOGIN);
            String password = request.getParameter(PASSWORD);

            User user = new User();
//           // user.setPassportId(idPassport);
//            user.setUserName(name);
//            user.setSurname(surname);
//            //user.setSecondName(secondName);
//            user.setEmail(email);
//            user.setRole(role);
            user.setLogin(login);
            user.setPassword(password);

            User resultClient = SERVICE.create(user);
            if(resultClient == null) {
                request.setAttribute(ERROR_FLAG, ERROR_FLAG_VALUE_2);
                request.setAttribute(ACTION, REDIRECT_ACTION_ATTRIBUTE);
               // page = MANAGER.getProperty(PagePath.REGISTRATION.toString());
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute(CLIENT_ROLE, resultClient);
               // page = MANAGER.getProperty(PagePath.RESULT.toString());
                request.setAttribute(ACTION, REDIRECT_ACTION_ATTRIBUTE);
            }
        } catch (ValidationException e) {
            request.setAttribute(ERROR_FLAG, ERROR_FLAG_VALUE);
            request.setAttribute(ACTION, FORWARD_ACTION_ATTRIBUTE);
           // page = MANAGER.getProperty(PagePath.REGISTRATION.toString());
        } catch (Exception e) {
            throw new CommandException("Command Exception", e);
        }
        return page;
    }
}

