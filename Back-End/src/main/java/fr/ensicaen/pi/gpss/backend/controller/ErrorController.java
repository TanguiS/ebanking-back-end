package fr.ensicaen.pi.gpss.backend.controller;

import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.NotFoundServiceException;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/error")
public class ErrorController {
    @GetMapping
    public ResponseEntity<String> handleGetError() {
        throw new NotFoundServiceException(RequestID.UNRESOLVED_MAPPING);
    }

    @PatchMapping
    public ResponseEntity<String> handlePatchError() {
        throw new NotFoundServiceException(RequestID.UNRESOLVED_MAPPING);
    }

    @PostMapping
    public ResponseEntity<String> handlePostMapping() {
        throw new NotFoundServiceException(RequestID.UNRESOLVED_MAPPING);
    }

    @DeleteMapping
    public ResponseEntity<String> handleDeleteMapping() {
        throw new NotFoundServiceException(RequestID.UNRESOLVED_MAPPING);
    }

    @PutMapping
    public ResponseEntity<String> handlePutMapping() {
        throw new NotFoundServiceException(RequestID.UNRESOLVED_MAPPING);
    }
}
