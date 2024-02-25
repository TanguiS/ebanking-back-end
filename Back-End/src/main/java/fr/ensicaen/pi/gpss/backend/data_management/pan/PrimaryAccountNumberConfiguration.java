package fr.ensicaen.pi.gpss.backend.data_management.pan;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.tools.security.AesCypher;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PrimaryAccountNumberConfiguration {
    @Value("${be.ebanking.app.pan.key}")
    private String key;
    @Value("${be.ebanking.app.pan.salt}")
    private String salt;
    @Value("${be.ebanking.app.pan.ensi_bin}")
    private String ensiBin;
    private AesCypher bankPanCypher;

    private PrimaryAccountNumberConfiguration() {
    }

    @PostConstruct
    private void initiate() {
        bankPanCypher = new AesCypher(key, salt);
    }

    public PrimaryAccountNumber newEnsiBankPanFromCardScheme(
            @NotNull CardScheme cardScheme, @NonNull BankAccountIdentification iban
    ) {
        PrimaryAccountNumberComponent component = new PrimaryAccountNumberComponent(
                String.valueOf(cardScheme.id()), ensiBin
        );
        return fromComponent(component, iban);
    }

    public PrimaryAccountNumber fromExistingPan(@NotBlank String encryptedOrNotPan) {
        return new PrimaryAccountNumber(bankPanCypher, encryptedOrNotPan);
    }

    public PrimaryAccountNumber fromComponent(
            @NonNull PrimaryAccountNumberComponent component,
            @NonNull BankAccountIdentification iban
    ) {
        return new PrimaryAccountNumber(bankPanCypher, new PrimaryAccountNumberGeneration(iban, component).getPAN());
    }

    public AesCypher getPanCypher() {
        return bankPanCypher;
    }
}
