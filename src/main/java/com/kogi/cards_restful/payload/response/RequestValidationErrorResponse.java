package com.kogi.cards_restful.payload.response;

import org.springframework.validation.ObjectError;

import java.util.List;

public class RequestValidationErrorResponse {
  private String message;
  private List<ObjectError> errors;

  public RequestValidationErrorResponse(String message,List<ObjectError> errors) {
    this.message = message;
    this.errors = errors;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<ObjectError> getErrors() {
    return errors;
  }

  public void setErrors(List<ObjectError> errors) {
    this.errors = errors;
  }
}
