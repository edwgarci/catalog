package com.atg.catalog.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  private HttpStatus httpStatus;

  public CustomException(String message) {
    super(message);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public CustomException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
