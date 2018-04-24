package com.excilys.formation.cdb.exceptions;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 1520327385784195689L;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
    }

    public ServiceException(String msg, Throwable e, boolean b1, boolean b2) {
        super(msg, e, b1, b2);
    }

}
