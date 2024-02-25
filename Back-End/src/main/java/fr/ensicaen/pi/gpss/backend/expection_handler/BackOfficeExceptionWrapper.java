package fr.ensicaen.pi.gpss.backend.expection_handler;

import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
public abstract class BackOfficeExceptionWrapper extends RuntimeException {
    private final String internalMessage;
    private final RequestID originRequest;


    protected BackOfficeExceptionWrapper(
            @Nullable String message, @NotBlank String internalMessage, @NonNull RequestID originRequest
    ) {
        super(message);
        this.internalMessage = internalMessage;
        this.originRequest = originRequest;
    }

    protected BackOfficeExceptionWrapper(@NotBlank String internalMessage, @NonNull RequestID originRequest) {
        this(null, internalMessage, originRequest);
    }

}
