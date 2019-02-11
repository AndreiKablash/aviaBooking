package com.htp.avia_booking.dao;

import com.htp.avia_booking.dao.connection_pool.ConnectionPool;
import com.htp.avia_booking.dao.connection_pool.ConnectionPoolException;
import com.htp.avia_booking.domain.Entity;
import com.htp.avia_booking.enums.DBOperation;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class AbstractDAO<T extends Entity> implements GenericDAO<T> {

    protected static final ConnectionPool pool = ConnectionPool.getInstance();
    protected Logger LOGGER = Logger.getLogger(String.valueOf(this.getClass()));
    protected int TWENTY_FOUR_HOURS_SEARCH_INTERVAL = 1;// used to set time interval for search in 1 day
    protected String TABLE_NAME_TEMP = "<%table_name%>";
    protected String GET_BY_ID = "SELECT * FROM <%table_name%> WHERE id = ?";
    protected String GET_ALL = "SELECT * FROM <%table_name%>";
    protected String GET_ALL_ENTITIES_BY_NAME = "SELECT * FROM <%table_name%> where name = ?";
    protected String DELETE_BY_ID = "DELETE FROM <%table_name%> WHERE id = ?";
    protected String INSERT_STATEMENT = null;
    protected String UPDATE_STATEMENT = null;

    /**
     * Method find node from database by identification number
     *
     * @param id unique number of node in database
     * @return {@link T} - found record
     * @throws DaoException
     */
    @Override
    public T getById(Long id) throws DaoException {
        String sqlStatement = this.GET_BY_ID.replace(TABLE_NAME_TEMP, this.getTableName());
        ResultSet rs = null;
        T entity;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement)) {
            statement.setLong(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                entity = fillEntity(rs);
                return entity;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException in getById method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in getById method", ex);
            throw new DaoException("Exception", ex);
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }
        }
    }

    /**
     * Method find all nodes from database
     *
     * @return List {@link T} all nodes in database
     * @throws DaoException
     */
    @Override
    public List<T> getAll() throws DaoException {
        String sqlStatement = this.GET_ALL.replace(TABLE_NAME_TEMP, this.getTableName());
        ResultSet rs = null;
        List<T> entityList = new ArrayList<>();
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement)) {
            rs = statement.executeQuery();
            while (rs.next()) {
                entityList.add(fillEntity(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException in getAll method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in getAll method", ex);
            throw new DaoException("Exception", ex);
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }

        }
        return entityList;
    }

    /**
     * Method find node from database by identification number
     *
     * @param name - value of field name in database
     * @returnList {@link T} all nodes in database
     * @throws DaoException
     */
    public List<T> getByName(String name) throws DaoException {
        String sqlStatement = this.GET_ALL_ENTITIES_BY_NAME.replace(TABLE_NAME_TEMP, this.getTableName());
        ResultSet rs = null;
        List<T> entityList = new ArrayList<>();
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement)) {
            statement.setString(1, name);
            rs = statement.executeQuery();
            while (rs.next()) {
                entityList.add(fillEntity(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("SQLException in getByName method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in getByName method", ex);
            throw new DaoException("Exception", ex);
        }finally{
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }

        }
        return entityList;
    }

    /**
     * Method to remove record from database by identification number
     *
     * @param entity object that necessary save in database
     * @return boolean value which defines state of transaction
     * @throws DaoException
     */
    @Override
    public boolean delete(T entity) throws DaoException {
        String sqlStatement = this.DELETE_BY_ID.replace(TABLE_NAME_TEMP, this.getTableName());
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.error("SQLException in delete method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in delete method", ex);
            throw new DaoException("Exception", ex);
        }
    }

    /**
     * Method to create or update record in database
     *
     * @param entity object that necessary save in database
     * @return unique number of created record
     * @throws DaoException
     */
    @Override
    public Long create(T entity) throws DaoException {
        String sqlStatement = this.INSERT_STATEMENT.replace(TABLE_NAME_TEMP, this.getTableName());
        ResultSet rs = null;
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            connect.setAutoCommit(false);
            this.populateParameters(statement, entity, DBOperation.CREATE);
            statement.executeUpdate();
            connect.commit();
            rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException ex) {
            LOGGER.error("SQLException in crete method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in crete method", ex);
            throw new DaoException("Exception", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("SQLException", ex);
                }
            }
        }
    }

    /**
     * Method update one record in database with current information
     *
     * @param entity object with new actually information
     * @return unique number updated record
     * @throws DaoException
     */
    @Override
    public Long update(T entity) throws DaoException {
        String sqlStatement = this.UPDATE_STATEMENT.replace(TABLE_NAME_TEMP, this.getTableName());
        try (Connection connect = pool.getConnection();
             PreparedStatement statement = connect.prepareStatement(sqlStatement)) {
            this.populateParameters(statement, entity, DBOperation.UPDATE);
            statement.executeUpdate();
            return entity.getId();
        } catch (SQLException ex) {
            LOGGER.error("SQLException in update method", ex);
            throw new DaoException("Exception", ex);
        } catch (ConnectionPoolException ex) {
            LOGGER.error("ConnectionPoolException in update method", ex);
            throw new DaoException("Exception", ex);
        }
    }

    /**
     * Method to set HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND to search in 24-hours interval
     *
     * @param c object of Calendar
     */
    protected void clearTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Method to get table name
     *
     * @return string with table name in database
     */
    protected abstract String getTableName();

    /**
     * Method to fill T object with all information from database
     *
     * @param resultSet with information from database
     * @return T object with all identification information
     * @throws SQLException
     */
    protected abstract T fillEntity(ResultSet resultSet) throws SQLException, DaoException;


    /**
     * Abstract utility method update and create methods should be overridden
     *
     * @param preparedStatement
     * @param entity of T object
     * @param operation
     * @throws SQLException
     */
    protected abstract void populateParameters(PreparedStatement preparedStatement, T entity, DBOperation operation) throws SQLException;
}
