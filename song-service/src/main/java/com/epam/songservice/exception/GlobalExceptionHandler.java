package com.epam.songservice.exception;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

import com.epam.songservice.dto.ErrorResponse;
import com.epam.songservice.dto.ValidationErrorResponse;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> invalidMetadata(
            MethodArgumentNotValidException exception) {

        var details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(groupingBy(
                        FieldError::getField,
                        TreeMap::new,
                        mapping(
                                FieldError::getDefaultMessage,
                                joining(", ")
                        )
                ));
        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse("Validation error", details, "400"));
    }

    @ExceptionHandler(InvalidSongIdException.class)
    public ResponseEntity<ErrorResponse> invalidId(InvalidSongIdException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(SongNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(SongAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> conflict(SongAlreadyExistsException exception) {
        return error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingParameter(
            MissingServletRequestParameterException exception) {
        return error(HttpStatus.BAD_REQUEST,
                     "Required parameter '" + exception.getParameterName() + "' is missing");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> invalidBody() {
        return error(HttpStatus.BAD_REQUEST, "Invalid request body. "
                + "Provide valid JSON with an integer ID");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> invalidContentType() {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported content type. "
                + "Use application/json");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> invalidMethod() {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method is not supported "
                + "for this endpoint");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> missingEndpoint() {
        return error(HttpStatus.NOT_FOUND, "Requested endpoint not found");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedFailure(Exception exception) {
        log.error("Song Service request failed", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, Integer.toString(status.value())));
    }
}
