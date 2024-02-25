package fr.ensicaen.pi.gpss.backend.controller.account;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.UnachievableServicesDemandException;
import fr.ensicaen.pi.gpss.backend.payload.request.UserCredentialUpdate;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.payload.response.user_manager.UserManagerDashboardDTO;
import fr.ensicaen.pi.gpss.backend.service.account.UserManagerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.GET_USER_MANAGER_DASHBOARD;
import static fr.ensicaen.pi.gpss.backend.payload.request.RequestID.UPDATE_USER_CREDENTIAL;

@RestController
@RequestMapping("/user/manager")
@Validated
public class UserManagerController {
    private final SingleResponseBuilder responseBuilder;
    private final UserManagerService userManagerService;

    public UserManagerController(SingleResponseBuilder responseBuilder, UserManagerService userManagerService) {
        this.responseBuilder = responseBuilder;
        this.userManagerService = userManagerService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResponseEntity<String> getDashboard() {
        UserManagerDashboardDTO output;
        try {
            output = userManagerService.getUserManagerDashboard();
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(GET_USER_MANAGER_DASHBOARD, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(GET_USER_MANAGER_DASHBOARD, e.getMessage());
        }
        return responseBuilder.setOkStatus().setRequest(GET_USER_MANAGER_DASHBOARD).setData(output).build();
    }

    @PatchMapping("/credential/update/{idUser}")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResponseEntity<String> updateUserCredential(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idUser,
            @Valid @NotNull @RequestBody UserCredentialUpdate credential
    ) {
        UserInformationDTO output;
        try {
            output = userManagerService.updateUserCredentialToAuthorizeWithRole(idUser, credential);
        } catch (IllegalArgumentException e) {
            throw new IllegalServicesArgumentException(UPDATE_USER_CREDENTIAL, e.getMessage());
        } catch (Exception e) {
            throw new UnachievableServicesDemandException(UPDATE_USER_CREDENTIAL, e.getMessage());
        }
        return responseBuilder.setAcceptedStatus().setRequest(UPDATE_USER_CREDENTIAL).setData(output).build();
    }
}
