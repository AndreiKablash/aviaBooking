package com.htp.avia_booking.service.interfaces;


import com.htp.avia_booking.domain.objects.User;
import com.htp.avia_booking.service.GenericServiceInterface;
import com.htp.avia_booking.service.ServiceException;


import java.util.List;

public interface UserService extends GenericServiceInterface<User> {

    User loadById(Long userId) throws ServiceException;

    User authorization(User user) throws ServiceException;

    List<User> viewAll() throws ServiceException;

    boolean deleteUser(long id) throws ServiceException;
}
