package com.springcourse.project.exeptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.springcourse.project.ui.model.response.ErrorResponse;

@ControllerAdvice
public class ExceptionHander {
	
	@ExceptionHandler(value={UserServiceException.class})
	public ResponseEntity<Object> serviceExceptionHandler(UserServiceException e, WebRequest req){
		
		ErrorResponse message = new ErrorResponse(new Date(), e.getMessage());
		return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value={Exception.class})
	public ResponseEntity<Object> exceptionHandler(Exception e, WebRequest req){
		
		ErrorResponse message = new ErrorResponse(new Date(), e.getMessage());
		return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
