package com.yostocks.stocksservice.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    //other exception handlers below
    // DataIntegrityViolationException

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    // empty message wasnt useful in case of that handler
    /*@ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }*/

    /*@ExceptionHandler(OptimisticLockingFailureException.class)
    protected ResponseEntity<Object> handleNullPointerException(OptimisticLockingFailureException ex) {
        ApiError apiError = new ApiError(CONFLICT);
        apiError.setMessage("optimistic locking failed");
        return buildResponseEntity(apiError);
    }*/

    // ie addFraction
    /*
     Class InterruptedException. Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread
     is interrupted, either before or during the activity. Occasionally a method may wish to test whether the current
     thread has been interrupted, and if so, to immediately throw this exception
     https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/InterruptedException.html
     */
    @ExceptionHandler(InterruptedException.class)
    protected ResponseEntity<Object> handleInterruptedException(InterruptedException ex) {
        ApiError apiError = new ApiError(CONFLICT);
        apiError.setMessage("InterruptedException occurred");
        return buildResponseEntity(apiError);
    }

    /*// validation exception (mainly refers to controller val. constraints, but also on the request models)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException ex){
        ApiError apiError = new ApiError(CONFLICT);
        apiError.setMessage("MethodArgumentNotValidException");
        return buildResponseEntity(apiError);
    }*/
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        ApiError apiError = new ApiError(ex.getStatusCode());
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpServerErrorException ex) {
        ApiError apiError = new ApiError(ex.getStatusCode());
        apiError.setMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

}