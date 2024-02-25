package fr.ensicaen.pi.gpss.backend.controller.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.payload.response.banker.BankerDashboardDTO;
import fr.ensicaen.pi.gpss.backend.service.proxy.BankerProxyService;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.UserInformationResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.*;

@RestController
@RequestMapping("/banker")
@Validated
public class BankerController {
    private final SingleResponseBuilder responseBuilder;
    private final UserInformationResolver userResolver;
    private final BankerProxyService bankerProxyService;

    public BankerController(
            SingleResponseBuilder responseBuilder,
            UserInformationResolver userResolver,
            BankerProxyService bankerProxyService
    ) {
        this.responseBuilder = responseBuilder;
        this.userResolver = userResolver;
        this.bankerProxyService = bankerProxyService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<String> getBankerDashboard() {
        BankerDashboardDTO output;
        try {
            output = bankerProxyService.getBankerDashboard(userResolver.resolve());
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(GET_BANKER_DASHBOARD, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(GET_BANKER_DASHBOARD, e.getMessage());
        }
        return responseBuilder.setRequest(GET_BANKER_DASHBOARD).setOkStatus().setData(output).build();
    }

    @PatchMapping("/block/card/{idClient}/{idCard}")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<String> blockClientsCard(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCard
    ) {
        RequestCardDTO output;
        try {
            output = bankerProxyService.blockClientsCard(idClient, idCard, userResolver.resolve());
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(BLOCK_USER_CARD_FROM_BANKER, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(RequestID.BLOCK_USER_CARD, e.getMessage());
        }
        return responseBuilder.setAcceptedStatus().setRequest(BLOCK_USER_CARD_FROM_BANKER).setData(output).build();
    }

    @PostMapping("/add/bank/account/{idClient}")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<String> addBankAccountToClient(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @RequestBody PreFilledBankAccount preFilledBankAccount
    ) {
        BankAccountDTO output;
        try {
            output = bankerProxyService.addBankAccountToClient(
                    idClient, preFilledBankAccount, userResolver.resolve()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(ADD_USER_BANK_ACCOUNT, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(ADD_USER_BANK_ACCOUNT, e.getMessage());
        }
        return responseBuilder.setAcceptedStatus().setRequest(ADD_USER_BANK_ACCOUNT).setData(output).build();
    }

    @PostMapping("/add/request/card/{idClient}/{idCurrentBankAccount}/{idProductCard}")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<String> addRequestCardToClient(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idProductCard,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCurrentBankAccount,
            @Valid @NotNull @RequestBody PreFilledCardInformation preFilledCardInformation
    ) {
        RequestCardDTO output;
        try {
            output = bankerProxyService.addRequestCardToClient(
                    idClient, idProductCard, idCurrentBankAccount, preFilledCardInformation, userResolver.resolve()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(ADD_REQUEST_CARD_FROM_BANKER, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(ADD_REQUEST_CARD_FROM_BANKER, e.getMessage());
        }
        return responseBuilder.setAcceptedStatus().setRequest(ADD_REQUEST_CARD_FROM_BANKER).setData(output).build();
    }
}
