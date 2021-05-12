package com.atg.catalog.exception;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  @ResponseBody
  public ResponseEntity<Object> handleCustomException(CustomException ce) {
    return new ResponseEntity<>(ce.getMessage(), ce.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidations(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().stream()
        .forEach(
            objectError ->
                errors.add(
                    "["
                        + ((FieldError) objectError).getField()
                        + "] "
                        + objectError.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
  @ResponseBody
  public ResponseEntity<Object> handleConstraintViolationException(Exception cv) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cv.getMessage());
  }
}
