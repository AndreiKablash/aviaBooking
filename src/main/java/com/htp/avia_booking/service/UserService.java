package com.htp.avia_booking.service;

import com.google.protobuf.ServiceException;
import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.exception.NoSuchEntityException;

import java.util.List;

public interface UserService extends GenericServiceInterface<User> {

    User loadById(Long userId) throws ServiceException, NoSuchEntityException;

    /**
     * Method provides operation for login user
     *
     * @param user object, that provides authorization operation
     * @return {@link User} - login record
     * @throws ServiceException
     */
    User authorization(User user) throws ServiceException;

    List<User> viewAll() throws ServiceException;
}
