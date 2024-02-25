package fr.ensicaen.pi.gpss.backend.expection_handler.login.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class UserNotFoundException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Login form is incorrect : Invalid Email or Password";
    public UserNotFoundException() {
        super(MSG, RequestID.LOGIN);
    }
}
