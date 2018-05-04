package com.excilys.formation.cdb.exceptions;

public class PageException extends Exception {

    private static final long serialVersionUID = -6230142790094543246L;

    public PageException() {
    }

    public PageException(String msg) {
        super(msg);
    }

    public PageException(Throwable e) {
        super(e);
    }

    public PageException(String msg, Throwable e) {
        super(msg, e);
    }

    public PageException(String msg, Throwable e, boolean b1, boolean b2) {
        super(msg, e, b1, b2);
    }

}
