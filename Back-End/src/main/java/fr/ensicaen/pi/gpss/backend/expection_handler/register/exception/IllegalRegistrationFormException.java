package fr.ensicaen.pi.gpss.backend.expection_handler.register.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class IllegalRegistrationFormException extends BackOfficeExceptionWrapper {
    private static final String MSG =
            "Registration form is incorrect : the form does not meet the requirement to register someone";
    public IllegalRegistrationFormException() {
        super(MSG, RequestID.REGISTER);
    }
}
