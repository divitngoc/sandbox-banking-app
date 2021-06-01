package com.divitbui.factory;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.divitbui.model.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrorResponseFactory {

    public ErrorResponse buildError(Throwable e) {
        return buildError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ErrorResponse buildError(Throwable e, HttpStatus status) {
        log.debug("Error: {}", e.getStackTrace(), e);

        if (e instanceof BindException) {
            return buildError(((BindException) e).getBindingResult(), status);
        } else {
            return ErrorResponse.builder()
                                .httpStatus(status.value())
                                .message(e.getMessage())
                                .build();
        }
    }

    public ErrorResponse buildError(final BindingResult br, final HttpStatus status) {
        return ErrorResponse.builder()
                            .message(br.getFieldError().getDefaultMessage())
                            .field(getFieldPath(br.getFieldError().getArguments()))
                            .httpStatus(status.value())
                            .build();
    }

    private String getFieldPath(Object[] fieldArguments) {
        DefaultMessageSourceResolvable source = (DefaultMessageSourceResolvable) fieldArguments[0];
        return source.getCodes()[0];
    }

}