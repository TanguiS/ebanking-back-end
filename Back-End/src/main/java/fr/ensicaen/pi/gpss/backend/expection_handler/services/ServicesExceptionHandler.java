package fr.ensicaen.pi.gpss.backend.expection_handler.services;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionHandlerWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.*;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.UNRESOLVED_MAPPING;

@ControllerAdvice
public class ServicesExceptionHandler extends BackOfficeExceptionHandlerWrapper {
    private static final Logger logger = LoggerFactory.getLogger("Services Exception Logger");

    public ServicesExceptionHandler(SingleResponseBuilder singleResponseBuilder) {
        super(singleResponseBuilder);
    }

    @ExceptionHandler({
            IllegalServicesArgumentException.class,
            UnachievableServicesDemandException.class
    })
    public ResponseEntity<String> handleServicesBadRequest(BackOfficeExceptionWrapper ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage(), ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return sendResponse(
                HttpStatus.BAD_REQUEST,
                new IllegalServicesArgumentException(UNRESOLVED_MAPPING,
                        fieldError == null ? null : fieldError.getDefaultMessage()
                )
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.FORBIDDEN, new UnauthorizedServicesException(
                UNRESOLVED_MAPPING, ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.error(ex.getMessage(), ex);
        StringBuilder errMessage = new StringBuilder("Type mismatch for argument ").append(ex.getName());
        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null) {
            errMessage.append(". Expected type: ").append(requiredType.getSimpleName());
        }
        errMessage.append(". Provided value: ").append(ex.getValue());
        return sendResponse(HttpStatus.BAD_REQUEST,
                new IllegalServicesArgumentException(UNRESOLVED_MAPPING, errMessage.toString())
        );
    }

    @ExceptionHandler({
            NotFoundServiceException.class,
            NoHandlerFoundException.class
    })
    public ResponseEntity<String> handleNoHandlerFoundExceptionCustom(NotFoundServiceException ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.NOT_FOUND, new NotFoundServiceException(UNRESOLVED_MAPPING, ex.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleEveryOtherException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return sendResponse(HttpStatus.BAD_REQUEST, new BadRequestException(UNRESOLVED_MAPPING, ex.getMessage()));
    }
}
