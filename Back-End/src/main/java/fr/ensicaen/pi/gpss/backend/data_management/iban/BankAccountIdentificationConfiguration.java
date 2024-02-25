package fr.ensicaen.pi.gpss.backend.data_management.iban;

import fr.ensicaen.pi.gpss.backend.tools.security.AesCypher;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BankAccountIdentificationConfiguration {
    @Value("${be.ebanking.app.iban.key}")
    private String key;
    @Value("${be.ebanking.app.iban.salt}")
    private String salt;
    @Value("${be.ebanking.app.iban.bank_code}")
    private String bankCode;
    @Value("${be.ebanking.app.iban.branch_code}")
    private String branchCode;
    @Value("${be.ebanking.app.iban.country_code}")
    private String countryCode;
    private BankAccountIdentificationComponent component;
    private AesCypher bankIbanCypher;

    private BankAccountIdentificationConfiguration() {
    }

    @PostConstruct
    private void initiate() {
        bankIbanCypher = new AesCypher(key, salt);
        component = new BankAccountIdentificationComponent(bankCode, branchCode, countryCode);
    }

    public BankAccountIdentification newEnsiBankIbanGenerator() {
        return fromComponent(component);
    }

    public BankAccountIdentification fromExistingIban(@NotBlank String encryptedOrNotIban) {
        return new BankAccountIdentification(
                bankIbanCypher, encryptedOrNotIban
        );
    }

    public BankAccountIdentification fromComponent(@NonNull BankAccountIdentificationComponent component) {
        return new BankAccountIdentification(
                bankIbanCypher,
                new BankAccountIdentificationGeneration(component).getIban()
        );
    }

    public AesCypher getIbanCypher() {
        return bankIbanCypher;
    }
}
