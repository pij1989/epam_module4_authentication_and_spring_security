package com.epam.esm.model.error;

import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAndErrorHandler {
    private static final String USER_NOT_FOUND = "error.401.user.username";
    private static final String INCORRECT_PASSWORD = "error.401.user.password";
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionAndErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> customHandleNotFound(Exception e) {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((NotFoundException) e).getArgs(),
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> customHandleBadRequest(Exception e) {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((BadRequestException) e).getArgs(),
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<Object> customHandlerUnauthorized(Exception e) {
        CustomError customError = new CustomError();
        if (e instanceof UsernameNotFoundException) {
            customError.setErrorMessage(messageSource.getMessage(USER_NOT_FOUND, new Object[]{e.getMessage()},
                    LocaleContextHolder.getLocale()));
        }
        if (e instanceof BadCredentialsException) {
            customError.setErrorMessage(messageSource.getMessage(INCORRECT_PASSWORD, new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        customError.setErrorCode(Integer.toString(HttpStatus.UNAUTHORIZED.value()));
        return new ResponseEntity<>(customError, HttpStatus.UNAUTHORIZED);
    }
}
