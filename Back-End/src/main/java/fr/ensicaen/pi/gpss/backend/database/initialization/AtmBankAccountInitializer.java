package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.service.add.AddATMBankAccountService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtmBankAccountInitializer {
    private final AddATMBankAccountService addATMBankAccountService;
    private final boolean isAllowed;

    public AtmBankAccountInitializer(
            AddATMBankAccountService addATMBankAccountService,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.addATMBankAccountService = addATMBankAccountService;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        addATMBankAccountService.addNew(1000000);
        addATMBankAccountService.addNew(2500000);
    }
}
