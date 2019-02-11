package com.htp.avia_booking.controller.command;

import com.htp.avia_booking.controller.command.impl.LoginCommand;
import com.htp.avia_booking.controller.command.impl.LogoutCommand;
import com.htp.avia_booking.controller.command.impl.RegistrationCommand;
import com.htp.avia_booking.controller.command.impl.admin.ViewUsersCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandHelper {
    private static final String ATTRIBUTE_COMMAND = "command";
    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";

    private final Map<CommandName, CommandInterface> commands = new HashMap<>();

    public CommandHelper() {
        commands.put(CommandName.AUTHORISATION, LoginCommand.getInstance());
        commands.put(CommandName.REGISTRATION, RegistrationCommand.getInstance());
        commands.put(CommandName.LOGOUT, LogoutCommand.getInstance());
        commands.put(CommandName.ADMIN_VIEW_USERS, ViewUsersCommand.getInstance());
    }

    private enum CommandName {
        AUTHORISATION,REGISTRATION,LOGOUT,ADMIN_VIEW_USERS
    }

    public CommandInterface getCommand(HttpServletRequest request) {
        String commandName = request.getParameter(ATTRIBUTE_COMMAND);
        if (commandName != null) {
            CommandName name = CommandName.valueOf(commandName.toUpperCase().replace(DASH, UNDERSCORE));
            return commands.get(name);
        } else {
            return null;
        }
    }
}
