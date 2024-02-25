package fr.ensicaen.pi.gpss.backend.expection_handler.register.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class PendingRegistrationException extends BackOfficeExceptionWrapper {
    private static final String MSG =
            "Registration form is valid. But, their is a Conflict with a registered user and " +
            "the registration demand is pending";
    public PendingRegistrationException() {
        super(MSG, RequestID.REGISTER);
    }
}
