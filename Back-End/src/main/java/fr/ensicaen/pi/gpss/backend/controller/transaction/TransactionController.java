package fr.ensicaen.pi.gpss.backend.controller.transaction;

import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.service.transaction.CollectTransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.COLLECTED_TRANSACTION;

@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {
    private final SingleResponseBuilder responseBuilder;
    private final CollectTransactionService collectTransactionService;

    public TransactionController(
            SingleResponseBuilder responseBuilder,
            CollectTransactionService collectTransactionService
    ) {
        this.responseBuilder = responseBuilder;
        this.collectTransactionService = collectTransactionService;
    }

    @PreAuthorize("hasAnyRole('ROLE_TRANSACTION_COLLECTOR', 'ROLE_SIMULATOR')")
    @PostMapping("/collect")
    public ResponseEntity<String> collect(
            @Valid @NotNull @RequestBody List<CollectedTransaction> transactions
    ) throws IllegalServicesArgumentException, UnachievableServicesDemandException {
        boolean out;
        try {
            out = collectTransactionService.collectTransactions(transactions);
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(COLLECTED_TRANSACTION, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(COLLECTED_TRANSACTION, "Could not persist transaction : "
                    + e.getMessage());
        }
        if (!out) {
            throw new UnachievableServicesDemandException(
                    COLLECTED_TRANSACTION,
                    "An error occurred when persisting transaction to DB"
            );
        }
        return responseBuilder.setCollectedTransactionRequest().setAcceptedStatus().build();
    }
}
