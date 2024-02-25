package fr.ensicaen.pi.gpss.backend.controller.account;

import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.response.accountant.AccountantDashboardDTO;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.service.account.AccountantAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.GET_ACCOUNTANT_DASHBOARD;

@RestController
@RequestMapping("/accountant")
@Validated
public class AccountantController {
    private final SingleResponseBuilder responseBuilder;
    private final AccountantAccountService accountantAccountService;

    public AccountantController(
            SingleResponseBuilder singleResponseBuilder, AccountantAccountService accountantAccountService
    ) {
        responseBuilder = singleResponseBuilder;
        this.accountantAccountService = accountantAccountService;
    }

    @GetMapping("/dashboard/{numberOfItemPerPage}/{pageNumber}")
    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public ResponseEntity<String> getAccountantDashboard(
            @Valid @NotNull @Positive @PathVariable Integer numberOfItemPerPage,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer pageNumber
    ) {
        AccountantDashboardDTO output;
        try {
            output = accountantAccountService.getAccountantDashboard(
                    numberOfItemPerPage, pageNumber
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(GET_ACCOUNTANT_DASHBOARD, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(GET_ACCOUNTANT_DASHBOARD, e.getMessage());
        }

        return responseBuilder.setOkStatus().setRequest(GET_ACCOUNTANT_DASHBOARD).setData(output).build();
    }
}

