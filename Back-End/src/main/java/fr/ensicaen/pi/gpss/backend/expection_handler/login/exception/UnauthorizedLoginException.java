package fr.ensicaen.pi.gpss.backend.expection_handler.login.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class UnauthorizedLoginException extends BackOfficeExceptionWrapper {
    private static final String MSG = "User is forbidden to access login resource";
    public UnauthorizedLoginException() {
        super(MSG, RequestID.LOGIN);
    }
}
