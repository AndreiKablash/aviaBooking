package com.htp.avia_booking.controller.command.util;

import java.util.ResourceBundle;

public class MessageManager {

    private static MessageManager instance;
    private ResourceBundle resourceBundle;

    private static final String BUNDLE_NAME = "messages";

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String)resourceBundle.getObject(key);
    }

}
