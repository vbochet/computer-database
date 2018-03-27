package com.excilys.formation.cdb.exceptions;

public class DaoException extends Exception {

    private static final long serialVersionUID = -4277715960883395165L;

    public DaoException() {
    }

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Throwable e) {
        super(e);
    }

    public DaoException(String msg, Throwable e) {
        super(msg, e);
    }

    public DaoException(String msg, Throwable e, boolean b1, boolean b2) {
        super(msg, e, b1, b2);
    }

}
