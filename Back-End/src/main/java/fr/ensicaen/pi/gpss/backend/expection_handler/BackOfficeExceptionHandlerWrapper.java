package fr.ensicaen.pi.gpss.backend.expection_handler;

import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class BackOfficeExceptionHandlerWrapper {
    private final SingleResponseBuilder responseBuilder;

    protected BackOfficeExceptionHandlerWrapper(SingleResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    protected ResponseEntity<String> sendResponse(@NonNull HttpStatus status, @NonNull BackOfficeExceptionWrapper ex) {
        String data = getPossibleAdditionalInformation(ex.getMessage(), ex.getInternalMessage());
        return responseBuilder.setStatus(status).setRequest(ex.getOriginRequest()).setData(data).build();
    }

    private static String getPossibleAdditionalInformation(@Nullable String originalMessage, @NotBlank String data) {
        if (originalMessage != null) {
            data += " - Additional information : " + originalMessage;
        }
        return data;
    }
}
