package com.htp.avia_booking.dao.connection_pool;

import java.sql.Connection;

    /**
     * Provides a common interface to work with pool of connections.
     */
public interface ConnectionPoolInterface {
     /**
     * Initialize pool with necessary properties from file.
     * @throws ConnectionPoolException
     */
    void init() throws ConnectionPoolException;

    /**
     * Close connection pool
     *
     * @throws ConnectionPoolException
     */
    void dispose() throws ConnectionPoolException;

    /**
     * Getting connection from pool
     *
     * @return connection from pool
     * @throws ConnectionPoolException
     */
    Connection getConnection() throws ConnectionPoolException;

    /**
     * Return connection in  pool
     */
    void putConnection();
}
