package com.reactiveworks.ipl.service.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.reactiveworks.ipl.service.exceptions.DeliveryNotFoundException;
import com.reactiveworks.ipl.service.exceptions.InsufficientInformationException;
import com.reactiveworks.ipl.service.exceptions.MatchIdNotFoundException;
import com.reactiveworks.ipl.service.exceptions.MatchNotFoundException;
import com.reactiveworks.ipl.service.exceptions.response.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(MatchNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleMatchNotFoundException(MatchNotFoundException exp) {
        List<String> details = new ArrayList<>();
        System.out.println(exp.getLocalizedMessage());
        details.add(exp.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("match Not Found", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(DeliveryNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleDeliveryNotFoundException(DeliveryNotFoundException exp) {
        List<String> details = new ArrayList<>();
        System.out.println(exp.getLocalizedMessage());
        details.add(exp.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("delivery Not Found", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	
	@ExceptionHandler(MatchIdNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleMatchIdNotFoundException(MatchIdNotFoundException exp) {
        List<String> details = new ArrayList<>();
        System.out.println(exp.getLocalizedMessage());
        details.add(exp.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("id is missing", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(InsufficientInformationException.class)
	public final ResponseEntity<ErrorResponse> handleInsufficientInformationException(InsufficientInformationException exp) {
        List<String> details = new ArrayList<>();
        System.out.println(exp.getLocalizedMessage());
        details.add(exp.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("mandatory details are missing ", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	

}
