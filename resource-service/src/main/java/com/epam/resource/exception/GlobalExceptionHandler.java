package com.epam.resource.exception;

import com.epam.resource.dto.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ErrorResponse> invalidResource(InvalidResourceException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingParameter(
            MissingServletRequestParameterException exception) {
        return error(HttpStatus.BAD_REQUEST, "Required parameter '"
                + exception.getParameterName() + "' is missing");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> invalidBody() {
        return error(HttpStatus.BAD_REQUEST, "Invalid request body");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> invalidContentType(
            HttpMediaTypeNotSupportedException exception) {
        return error(HttpStatus.BAD_REQUEST, "Invalid file format: "
                + exception.getContentType()
                + ". Only MP3 files are allowed");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> missingEndpoint() {
        return error(HttpStatus.NOT_FOUND, "Requested endpoint not found");
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> songServiceFailure(FeignException exception) {
        log.error("Song Service request failed", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update song metadata");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedFailure(Exception exception) {
        log.error("Resource Service request failed", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(message, Integer.toString(status.value())));
    }
}
