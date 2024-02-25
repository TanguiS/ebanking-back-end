package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.service.add.AddBankerClientService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({
        "getMasterForm",
        "getTransactionCollectorForm",
        "getSimulatorForm",
        "getBankerForm",
        "getAccountantForm",
        "getBabylonClientForm"}
)
public class BankerAccountInitializer {
    private final AddBankerClientService addBankerClientService;
    private final UserInitializer userInitializer;
    private final boolean isAllowed;

    public BankerAccountInitializer(
            AddBankerClientService addBankerClientService,
            UserInitializer userInitializer,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.addBankerClientService = addBankerClientService;
        this.userInitializer = userInitializer;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        addBankerClientService.addNewClient(userInitializer.getBankerDTO(), userInitializer.getBabylonDTO());
        addBankerClientService.addNewClient(userInitializer.getBankerDTO(), userInitializer.getBabylonJuniorDTO());
    }
}
