package com.excilys.formation.cdb.exceptions;

public class MapperException extends Exception {

    private static final long serialVersionUID = -4277715960883395165L;

    public MapperException() {
    }

    public MapperException(String msg) {
        super(msg);
    }

    public MapperException(Throwable e) {
        super(e);
    }

    public MapperException(String msg, Throwable e) {
        super(msg, e);
    }

    public MapperException(String msg, Throwable e, boolean b1, boolean b2) {
        super(msg, e, b1, b2);
    }

}
