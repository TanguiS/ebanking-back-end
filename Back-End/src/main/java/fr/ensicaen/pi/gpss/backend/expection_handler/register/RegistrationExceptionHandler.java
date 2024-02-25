package fr.ensicaen.pi.gpss.backend.expection_handler.register;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionHandlerWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.register.exception.*;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler extends BackOfficeExceptionHandlerWrapper {
    private static final Logger logger = LoggerFactory.getLogger("Registration Exception Logger");

    public RegistrationExceptionHandler(SingleResponseBuilder singleResponseBuilder) {
        super(singleResponseBuilder);
    }

    @ExceptionHandler({IllegalRegistrationFormException.class, EmailAlreadyUsedException.class})
    public ResponseEntity<String> handleRegistrationBadRequest(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(AlreadyRegisteredException.class)
    public ResponseEntity<String> handleRegistrationConflict(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(PendingRegistrationException.class)
    public ResponseEntity<String> handleRegistrationAccepted(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.ACCEPTED, ex);
    }

    @ExceptionHandler(UnauthorizedRegistrationException.class)
    public ResponseEntity<String> handleRegistrationForbidden(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.FORBIDDEN, ex);
    }
}
