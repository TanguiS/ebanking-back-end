package fr.ensicaen.pi.gpss.backend.expection_handler.register.exception;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;

public class EmailAlreadyUsedException extends BackOfficeExceptionWrapper {
    private static final String MSG =
            "Registration form is incorrect : Specified email is already used";
    public EmailAlreadyUsedException() {
        super(MSG, RequestID.REGISTER);
    }
}
