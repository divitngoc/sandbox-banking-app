package com.divitbui.controller.error;

import javax.servlet.UnavailableException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.exception.BalanceNotFoundException;
import com.divitbui.exception.InvalidRequestException;
import com.divitbui.exception.TransferFailedException;
import com.divitbui.factory.ErrorResponseFactory;
import com.divitbui.model.response.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorResponseFactory errorResponseFactory;

    @ExceptionHandler({
            AccountNotFoundException.class,
            BalanceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFound(Exception e) {
        return errorResponseFactory.buildError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BindException.class,
            InvalidRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidRequest(Throwable e) {
        return errorResponseFactory.buildError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnavailableException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ErrorResponse handlePreconditionFailed(Exception e) {
        return errorResponseFactory.buildError(e, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({
            Exception.class,
            TransferFailedException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleServerError(Throwable e) {
        log.error("Internal server error: [{}]", e.getMessage(), e);
        return errorResponseFactory.buildError(e);

    }

}
