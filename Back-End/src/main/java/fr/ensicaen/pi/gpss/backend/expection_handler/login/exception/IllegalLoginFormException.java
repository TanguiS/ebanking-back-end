package fr.ensicaen.pi.gpss.backend.expection_handler.login.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class IllegalLoginFormException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Login form is incorrect : " +
            "the form does not meet the requirement to login someone";
    public IllegalLoginFormException() {
        super(MSG, RequestID.LOGIN);
    }
}
