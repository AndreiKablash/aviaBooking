package com.htp.avia_booking.dao.connection_pool;

public class ConnectionPoolException extends Exception {
    private static final long serialVersionUID = 1L;

    public ConnectionPoolException(String definition, Exception e) {
        super(definition, e);
    }

}
