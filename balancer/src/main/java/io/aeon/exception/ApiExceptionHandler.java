package io.aeon.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<ExceptionMessage> handleApiException(ApiException ex) {
		ExceptionMessage message = new ExceptionMessage(ex.getStatus(), ex.getMessage(), ex.getStatus().value());
		log.warn(ex.getMessage());
		return new ResponseEntity<>(message, ex.getStatus());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionMessage> handleRuntimeException(RuntimeException ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ExceptionMessage message = new ExceptionMessage(status, ex.getMessage(), status.value());
		log.error(ex.getMessage());
		return new ResponseEntity<>(message, status);
	}
}
