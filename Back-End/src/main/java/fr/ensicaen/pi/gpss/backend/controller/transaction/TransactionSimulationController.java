package fr.ensicaen.pi.gpss.backend.controller.transaction;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.service.transaction.CollectTransactionSimulationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.GENERATE_TRANSACTION;

@RestController
@RequestMapping("/simulation/transaction")
@Validated
public class TransactionSimulationController {
    private final SingleResponseBuilder responseBuilder;
    private final CollectTransactionSimulationService collectTransactionSimulationService;
    private final TransactionController transactionController;

    public TransactionSimulationController(
            SingleResponseBuilder responseBuilder,
            CollectTransactionSimulationService collectTransactionSimulationService,
            TransactionController transactionController
    ) {
        this.responseBuilder = responseBuilder;
        this.collectTransactionSimulationService = collectTransactionSimulationService;
        this.transactionController = transactionController;

    }

    @PreAuthorize("hasRole('ROLE_SIMULATOR')")
    @GetMapping("/generate/collectable/{numberOfTransactionsToGenerate}")
    public ResponseEntity<String> generateTransactionFromNumber(
            @Valid @NotNull @Positive @PathVariable Integer numberOfTransactionsToGenerate
    ) {
        List<TransactionDTO> output;
        try {
            output = collectTransactionSimulationService.generateTransactions(numberOfTransactionsToGenerate);
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(GENERATE_TRANSACTION, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(
                    GENERATE_TRANSACTION, "Failure to generate : " + e.getMessage()
            );
        }
        return responseBuilder.setGenerateTransactionRequest()
                               .setOkStatus()
                               .setData(output.stream().map(TemplateDTO.class::cast).toList())
                               .build();
    }

    // This Mapping is an example on how the generated transactions could be used -
    // Should be refactored to a more sustainable process
    @PreAuthorize("hasRole('ROLE_SIMULATOR')")
    @PostMapping("/run/collectable")
    public ResponseEntity<String> runSimulation(
            @Valid @NotEmpty @RequestBody List<CollectedTransaction> fakeTransactions
    ) throws IllegalServicesArgumentException, UnachievableServicesDemandException {
        Timestamp startSimulation = new Timestamp(new Date().getTime());

        String output;
        try {
            output = transactionController.collect(fakeTransactions).getBody();
        } catch (IllegalServicesArgumentException e) {
            throw new IllegalServicesArgumentException(
                    RequestID.SIMULATE_TRANSACTION_COLLECTION,
                    "From Mapping 'collect' : " + e.getMessage()
            );
        } catch (UnachievableServicesDemandException e) {
            throw new UnachievableServicesDemandException(
                    RequestID.SIMULATE_TRANSACTION_COLLECTION,
                    "From Mapping 'collect' : " + e.getMessage()
            );
        }
        Timestamp endSimulation = new Timestamp(new Date().getTime());

        //for now, we have not discussed how to properly revert a simulation, to avoid any unwanted behavior,
        // they are instantaneously removed
        return responseBuilder.setSimulateTransactionCollectionRequest()
                               .setOkStatus()
                               .setData(
                                   "Simulation duration : " +
                                   (endSimulation.getTime() - startSimulation.getTime()) +
                                   " ms (does not include transactions removal)" +
                                   "\nCollect Mapping output :\n" +
                                   output
                               )
                               .build();
    }
}
