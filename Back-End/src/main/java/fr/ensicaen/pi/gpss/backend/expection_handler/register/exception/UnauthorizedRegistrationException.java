package fr.ensicaen.pi.gpss.backend.expection_handler.register.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class UnauthorizedRegistrationException extends BackOfficeExceptionWrapper {
    private static final String MSG =
            "Registration form is valid. But, their is a Conflict with a user " +
            "information who is forbidden to access this resource";
    public UnauthorizedRegistrationException() {
        super(MSG, RequestID.REGISTER);
    }
}
