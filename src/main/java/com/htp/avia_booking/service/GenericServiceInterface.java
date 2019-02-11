package com.htp.avia_booking.service;



import java.util.List;

/**
 * Basic Service interface with template parameters.
 *
 * @param <TO> generic type of objects passed to methods and can be returned
 *             Provides create and viewAll operations with {@link TO} objects.
 */
public interface GenericServiceInterface<TO> {

    /**
     * Method adding object in database and creates the appropriate entry there
     *
     * @param entity object necessary to adding in database
     * @return {@link TO} object, that method can create
     * @throws ServiceException
     */
    TO create(TO entity) throws ServiceException;

    /**
     * Method provides viewing all information and package this information in view object
     *
     * @return {@link TO} object necessary for view all objects
     * @throws ServiceException
     */
    List<TO> loadAll() throws ServiceException;


    TO update(TO entity) throws ServiceException;

}
