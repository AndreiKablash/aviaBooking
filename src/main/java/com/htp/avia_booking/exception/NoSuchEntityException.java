package com.htp.avia_booking.exception;

public class NoSuchEntityException extends RuntimeException /*made from runtime exception*/ {

    private static final long serialVersionUID = 1L;

    public NoSuchEntityException(String message){
        super(message);
    }

//    public NoSuchEntityException(String message, Exception ex){
//        super(message, ex);
//    }

}
