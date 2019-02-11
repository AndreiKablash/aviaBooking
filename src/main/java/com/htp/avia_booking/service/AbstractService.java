package com.htp.avia_booking.service;

import com.htp.avia_booking.dao.factory.DaoFactory;
import com.htp.avia_booking.domain.Entity;
import org.apache.log4j.Logger;

public abstract class AbstractService<T extends Entity> implements GenericServiceInterface<T> {
    protected DaoFactory factory = DaoFactory.getDaoFactory();
    protected Logger LOGGER = Logger.getLogger(String.valueOf(this.getClass()));
}
