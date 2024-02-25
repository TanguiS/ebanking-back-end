package fr.ensicaen.pi.gpss.backend.expection_handler.services.exception;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;

public class UnauthorizedServicesException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Unable to process the request, user is forbidden to access this resource";

    public UnauthorizedServicesException(RequestID originRequest, String complementaryMessage) {
        super(complementaryMessage, MSG, originRequest);
    }
}
