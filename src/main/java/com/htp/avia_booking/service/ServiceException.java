package com.htp.avia_booking.service;
/**
 * The class is used to create objects of the service  level exceptions
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Exception ex){
        super(message, ex);
    }
}
