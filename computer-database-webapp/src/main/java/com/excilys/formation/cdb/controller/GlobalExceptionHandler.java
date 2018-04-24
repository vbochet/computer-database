package com.excilys.formation.cdb.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String EXCEPTION_ATTRIBUTE_NAME = "exception";

    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle(NoHandlerFoundException exception) {
        ModelAndView model = new ModelAndView("404");
        model.addObject(EXCEPTION_ATTRIBUTE_NAME, exception);
        return model;
    }

    // 500
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handle(RuntimeException exception) {
        ModelAndView model = new ModelAndView("500");
        model.addObject(EXCEPTION_ATTRIBUTE_NAME, exception);
        return model;
    }
}