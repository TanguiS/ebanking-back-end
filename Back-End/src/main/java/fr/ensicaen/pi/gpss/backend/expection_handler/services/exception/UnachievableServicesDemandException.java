package fr.ensicaen.pi.gpss.backend.expection_handler.services.exception;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;

public class UnachievableServicesDemandException extends BackOfficeExceptionWrapper {
    private static final String MSG = "Unable to process the request, user demand is fair but can't be processed";
    public UnachievableServicesDemandException(RequestID originRequest) {
        super(MSG, originRequest);
    }

    public UnachievableServicesDemandException(RequestID originRequest, String complementaryMessage) {
        super(complementaryMessage, MSG, originRequest);
    }
}
