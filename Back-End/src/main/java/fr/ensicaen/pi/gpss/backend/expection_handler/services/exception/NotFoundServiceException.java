package fr.ensicaen.pi.gpss.backend.expection_handler.services.exception;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;

public class NotFoundServiceException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Unable to process the request, requested mapping does not exist";
    public NotFoundServiceException(RequestID originRequest) {
        super(MSG, originRequest);
    }

    public NotFoundServiceException(RequestID originRequest, String complementaryMessage) {
        super(complementaryMessage, MSG, originRequest);
    }
}
