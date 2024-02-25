package fr.ensicaen.pi.gpss.backend.controller.account;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBeneficiary;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.service.account.ClientAccountService;
import fr.ensicaen.pi.gpss.backend.service.add.AddBeneficiaryService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.CardBlockerService;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.BankAccountResolver;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.UserInformationResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.ADD_REQUEST_CHEQUEBOOK_USER;
import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.GET_USER_NAMES;

@RestController
@RequestMapping("/client")
@Validated
public class ClientController {
    private final SingleResponseBuilder responseBuilder;
    private final CardBlockerService cardBlockerService;
    private final ClientAccountService clientAccountService;
    private final AddBeneficiaryService addBeneficiaryService;
    private final UserInformationResolver userResolver;
    private final BankAccountResolver bankAccountResolver;
    public ClientController(
            SingleResponseBuilder responseBuilder,
            CardBlockerService cardBlockerService,
            ClientAccountService clientAccountService,
            AddBeneficiaryService addBeneficiaryService,
            UserInformationResolver resolver,
            BankAccountResolver bankAccountResolver
    ) {
        this.responseBuilder = responseBuilder;
        this.cardBlockerService = cardBlockerService;
        this.addBeneficiaryService = addBeneficiaryService;
        userResolver = resolver;
        this.clientAccountService = clientAccountService;
        this.bankAccountResolver = bankAccountResolver;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> getDashboard() {
        List<BankAccountDTO> output;
        try {
            output = clientAccountService.findAllBankAccountByClientUser(userResolver.resolve());
        } catch (Exception e) {
            throw new IllegalServicesArgumentException(
                    GET_USER_NAMES,
                    "Server tried to get the user's names but failed - wrong arguments");
        }
        return responseBuilder.setRequest(GET_USER_NAMES).setOkStatus().setData(
                output.stream().map(TemplateDTO.class::cast).toList()
        ).build();
    }

    @PatchMapping("/block/card/{idCard}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> blockCardById(@Valid @NotNull @PositiveOrZero @PathVariable Integer idCard) {
        RequestCardDTO output;
        if (cardBlockerService.blockCard(idCard, userResolver.resolve())) {
            throw new IllegalServicesArgumentException(RequestID.BLOCK_USER_CARD, "Could not block card");
        }
        try {
            output = cardBlockerService.requestCardOnCardBlocked(idCard);
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(
                    RequestID.BLOCK_USER_CARD, "Could not generate a request, but card is blocked : " + e.getMessage()
            );
        }
        return responseBuilder.setRequest(RequestID.BLOCK_USER_CARD).setAcceptedStatus().setData(output).build();
    }

    @PostMapping("/add/beneficiary/{idCurrentBankAccount}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addBeneficiary(
            @Valid @NotNull @RequestBody PreFilledBeneficiary preFilledBeneficiary,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCurrentBankAccount
    ) {
        BeneficiaryDTO output;
        try {
            output = addBeneficiaryService.addNew(
                    preFilledBeneficiary, bankAccountResolver.resolveById(idCurrentBankAccount)
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(RequestID.ADD_USER_BENEFICIARY, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(
                    RequestID.ADD_USER_BENEFICIARY,
                    "Could not add a beneficiary : " + e.getMessage()
            );
        }
        return responseBuilder.setRequest(RequestID.ADD_USER_BENEFICIARY).setAcceptedStatus().setData(output).build();
    }

    @GetMapping("/add/request/chequebook/{idCurrentBankAccount}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> addRequestChequeBook(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCurrentBankAccount
    ) {
        RequestChequeBookDTO output;
        try {
            output = clientAccountService.addRequestChequeBook(idCurrentBankAccount);
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(ADD_REQUEST_CHEQUEBOOK_USER, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(ADD_REQUEST_CHEQUEBOOK_USER, e.getMessage());
        }
        return responseBuilder.setAcceptedStatus().setRequest(ADD_REQUEST_CHEQUEBOOK_USER).setData(output).build();
    }
}
