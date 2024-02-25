package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.AuthorizationPolicyDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.AuthorizationPolicyRequirementDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.SoftwareCardDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AuthorizationType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.PriorityUseLevel;
import fr.ensicaen.pi.gpss.backend.service.add.AddCardProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Configuration
public class CardProductInitializer {
    private final AddCardProductService addCardProductService;
    private final boolean isAllowed;

    public CardProductInitializer(
            AddCardProductService addCardProductService,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.addCardProductService = addCardProductService;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        initiateVISAElectronProduct();
        initiateMASTERCARDPlusProduct();
        initiateVISAGoldProduct();
    }

    private static SoftwareCardDTO visaSecond() {
        return SoftwareCardDTO.builder()
                .cardScheme(CardScheme.VISA)
                .priorityUseLevel(PriorityUseLevel.SECOND)
                .build();
    }

    private static SoftwareCardDTO cbFirst() {
        return SoftwareCardDTO.builder()
                .cardScheme(CardScheme.CB)
                .priorityUseLevel(PriorityUseLevel.FIRST)
                .build();
    }

    private static AuthorizationPolicyDTO onlineSecond() {
        return AuthorizationPolicyDTO.builder()
                .authorizationType(AuthorizationType.ON_LINE)
                .priorityUseLevel(PriorityUseLevel.SECOND)
                .build();
    }

    private static AuthorizationPolicyDTO offlineFirst(AuthorizationPolicyRequirementDTO offlineRequirement) {
        return AuthorizationPolicyDTO.builder()
                .requirement(offlineRequirement)
                .priorityUseLevel(PriorityUseLevel.FIRST)
                .authorizationType(AuthorizationType.OFF_LINE)
                .build();
    }

    private void initiateVISAGoldProduct() {
        AuthorizationPolicyRequirementDTO offlineRequirement = AuthorizationPolicyRequirementDTO.builder()
                .lowerConsecutiveOfflineLimit(3)
                .upperConsecutiveOfflineLimit(6)
                .cumulativeTotalTransactionAmountLimit(500000)
                .cumulativeTotalTransactionAmountUpperLimit(1000000)
                .build();
        AuthorizationPolicyDTO offline = offlineFirst(offlineRequirement);
        AuthorizationPolicyDTO online = onlineSecond();
        SoftwareCardDTO cbSoftware = cbFirst();
        SoftwareCardDTO visaSoftware = visaSecond();
        CardProductDTO visaElectron = CardProductDTO.builder()
                .name("VISA GOLD")
                .cardType(CardType.DEBIT_CARD)
                .contactlessUpperLimitPerTransaction(50)
                .numberOfContactlessTransactionBeforeAskingPin(3)
                .softwareCards(Stream.of(cbSoftware, visaSoftware).toList())
                .authorizationPolicies(Stream.of(offline, online).toList())
                .build();
        addCardProductService.addNew(visaElectron);
    }

    private void initiateMASTERCARDPlusProduct() {
        AuthorizationPolicyRequirementDTO offlineRequirement = AuthorizationPolicyRequirementDTO.builder()
                .lowerConsecutiveOfflineLimit(3)
                .upperConsecutiveOfflineLimit(6)
                .cumulativeTotalTransactionAmountLimit(1000000)
                .cumulativeTotalTransactionAmountUpperLimit(1600000)
                .build();
        AuthorizationPolicyDTO offline = offlineFirst(offlineRequirement);
        AuthorizationPolicyDTO online = onlineSecond();
        SoftwareCardDTO cbSoftware = cbFirst();
        SoftwareCardDTO mastercardSoftware = SoftwareCardDTO.builder()
                .cardScheme(CardScheme.MASTERCARD)
                .priorityUseLevel(PriorityUseLevel.SECOND)
                .build();
        CardProductDTO mastercardPlus = CardProductDTO.builder()
                .name("MASTERCARD PLUS")
                .cardType(CardType.CREDIT_CARD)
                .contactlessUpperLimitPerTransaction(50)
                .numberOfContactlessTransactionBeforeAskingPin(3)
                .softwareCards(Stream.of(cbSoftware, mastercardSoftware).toList())
                .authorizationPolicies(Stream.of(offline, online).toList())
                .build();
        addCardProductService.addNew(mastercardPlus);
    }

    private void initiateVISAElectronProduct() {
        AuthorizationPolicyRequirementDTO offlineRequirement = AuthorizationPolicyRequirementDTO.builder()
                .lowerConsecutiveOfflineLimit(3)
                .upperConsecutiveOfflineLimit(6)
                .cumulativeTotalTransactionAmountLimit(300000)
                .cumulativeTotalTransactionAmountUpperLimit(600000)
                .build();
        AuthorizationPolicyDTO offline = offlineFirst(offlineRequirement);
        AuthorizationPolicyDTO online = onlineSecond();
        SoftwareCardDTO cbSoftware = cbFirst();
        SoftwareCardDTO visaSoftware = visaSecond();
        CardProductDTO visaElectron = CardProductDTO.builder()
                .name("VISA ELECTRON")
                .cardType(CardType.DEBIT_CARD)
                .contactlessUpperLimitPerTransaction(50)
                .numberOfContactlessTransactionBeforeAskingPin(3)
                .softwareCards(Stream.of(cbSoftware, visaSoftware).toList())
                .authorizationPolicies(Stream.of(offline, online).toList())
                .build();
        addCardProductService.addNew(visaElectron);
    }
}
