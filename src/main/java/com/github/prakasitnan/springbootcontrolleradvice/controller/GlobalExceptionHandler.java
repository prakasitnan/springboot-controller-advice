package com.github.prakasitnan.springbootcontrolleradvice.controller;

import com.github.prakasitnan.springbootcontrolleradvice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Log4j2
@ControllerAdvice(basePackages = "com.github.prakasitnan.springbootcontrolleradvice.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("status", 404);
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());

        return modelAndView;
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("status", 404);
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {

        log.error(ex.getMessage(), ex);

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("status", 500);
        modelAndView.addObject("error", "Internal Server Error");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }
}
