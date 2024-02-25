package fr.ensicaen.pi.gpss.backend.expection_handler.services.exception;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;

public class BadRequestException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Bad Request";
    public BadRequestException(RequestID originRequest) {
        super(MSG, originRequest);
    }

    public BadRequestException(RequestID originRequest, String complementaryMessage) {
        super(complementaryMessage, MSG, originRequest);
    }
}
