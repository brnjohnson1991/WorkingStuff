package com.sg.mastermind.controller;

import com.sg.mastermind.dao.GameEmptyException;
import com.sg.mastermind.dao.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author BRNJO
 */
@ControllerAdvice
@RestController
public class MastermindControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    public final ResponseEntity<Error> handleGameNotFoundException(
            GameNotFoundException e,
            WebRequest request) {
        Error err = new Error();
        err.setMessage(e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameEmptyException.class)
    public final ResponseEntity<Error> handleGameEmptyException(
            GameEmptyException e,
            WebRequest request) {
        Error err = new Error();
        err.setMessage(e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Error> handleBadRequestException(
            BadRequestException e,
            WebRequest request) {
        Error err = new Error();
        err.setMessage(e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
