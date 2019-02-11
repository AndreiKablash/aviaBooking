package com.htp.avia_booking.dao.connection_pool;

import java.util.ResourceBundle;


public class DatabaseConfigManager {
    private static DatabaseConfigManager instance;
    private ResourceBundle resourceBundle;

    private static final String BUNDLE_NAME = "database";
    public static final String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME";
    public static final String DATABASE_URL = "DATABASE_URL";
    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final String DATABASE_LOGIN = "DATABASE_LOGIN";
    public static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";
    public static final String DATABASE_POOL_SIZE = "DATABASE_POOL_SIZE";

    public static DatabaseConfigManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConfigManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String)resourceBundle.getObject(key);
    }
}
