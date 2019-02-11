package com.htp.avia_booking.dao;

import com.htp.avia_booking.domain.Entity;

import java.util.List;

/**
 * Basic Data Access Object interface with template parameters.
 *
 * @param <T> generic type of objects passed to methods
 *            Provides CRUD operations with {@link T} objects.
 */
public interface GenericDAO<T extends Entity> {
    /**
     * Method find all nodes from database
     *
     * @return List {@link T} all nodes in database
     * @throws DaoException
     */
    List<T> getAll() throws DaoException;

    /**
     * Method find node from database by identification number
     *
     * @param id unique number of node in database
     * @return {@link T} - found record
     * @throws DaoException
     */
    T getById(Long id) throws DaoException;

    /**
     * Method find node from database by identification number
     *
     * @param name - value of the name field in database
     * @return List {@link T} all nodes in database
     * @throws DaoException
     */
    List<T> getByName(String name) throws DaoException;

    /**
     * Method remove record from database by identification number
     *
     * @param entity object that necessary save in database
     * @return boolean value which defines state of transaction
     * @throws DaoException
     */
    boolean delete(T entity) throws DaoException;

    /**
     * Method that create records in database
     *
     * @param entity object that necessary save in database
     * @return unique number created record
     * @throws DaoException
     */
    Long create(T entity) throws DaoException;

    /**
     * Method update one record in database with current information
     *
     * @param entity object with new actually information
     * @return unique number updated record
     * @throws DaoException
     */
    Long update(T entity) throws DaoException;
}
