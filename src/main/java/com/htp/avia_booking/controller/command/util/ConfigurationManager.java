package com.htp.avia_booking.controller.command.util;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private ResourceBundle resourceBundle;

    private static final String BUNDLE_NAME = "config";

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public static void main(String[] args) {
        ConfigurationManager.getInstance();
    }
    public String getProperty(String key) {
        return (String)resourceBundle.getObject(key);
    }
}
