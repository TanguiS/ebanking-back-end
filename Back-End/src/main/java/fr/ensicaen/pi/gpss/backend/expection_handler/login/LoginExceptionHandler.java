package fr.ensicaen.pi.gpss.backend.expection_handler.login;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionHandlerWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.IllegalLoginFormException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UnauthorizedLoginException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UserNotFoundException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UserWrongPasswordException;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler extends BackOfficeExceptionHandlerWrapper {
    private static final Logger logger = LoggerFactory.getLogger("Login Exception Logger");

    public LoginExceptionHandler(SingleResponseBuilder singleResponseBuilder) {
        super(singleResponseBuilder);
    }

    @ExceptionHandler({IllegalLoginFormException.class, UserNotFoundException.class, UserWrongPasswordException.class})
    public ResponseEntity<String> handleLogInBadRequest(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UnauthorizedLoginException.class)
    public ResponseEntity<String> handleLogInForbidden(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.FORBIDDEN, ex);
    }
}
