package fr.ensicaen.pi.gpss.backend.expection_handler.services.exception;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;

public class IllegalServicesArgumentException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Illegal Argument";
    public IllegalServicesArgumentException(RequestID originRequest) {
        super(MSG, originRequest);
    }

    public IllegalServicesArgumentException(RequestID originRequest, String complementaryMessage) {
        super(complementaryMessage, MSG, originRequest);
    }
}
