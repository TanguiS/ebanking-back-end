package fr.ensicaen.pi.gpss.backend.data_management.iban;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.Iban;
import fr.ensicaen.pi.gpss.backend.tools.security.AesCypher;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

import static fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationLength.*;

@Validated
public class BankAccountIdentification {
    private final AesCypher cypher;
    private BankAccountIdentificationComponent component;
    private String iban;
    private String rib;
    private static final String ERR_MESSAGE = "Bank identification are wrong : internal error";

    protected BankAccountIdentification(@NonNull AesCypher aesCypher, @NotBlank String encryptedOrNotIban) {
        cypher = aesCypher;
        parseIban(encryptedOrNotIban);
    }

    private void parseIban(@NotBlank String iban) throws IllegalArgumentException {
        if (EncryptedDataPrefix.isEncryptedIban(iban)) {
            iban = cypher.decrypt(iban, EncryptedDataPrefix.IBAN.prefix());
        }
        this.iban = iban;
        String countryCode;
        int checkDigit;
        String bankCode;
        String accountNumber;
        String branchCode;
        int checkBankReferenceDigit;

        try {
            countryCode = this.iban.substring(0, COUNTRY_CODE.length());
            checkDigit = Integer.parseInt(
                    this.iban.substring(COUNTRY_CODE.length(),
                    COUNTRY_CODE.length() + CHECK_DIGIT.length())
            );
            bankCode = this.iban.substring(
                    COUNTRY_CODE.length() + CHECK_DIGIT.length(),
                    COUNTRY_CODE.length() + CHECK_DIGIT.length() + BANK_CODE.length()
            );
            branchCode = this.iban.substring(
                    COUNTRY_CODE.length() + CHECK_DIGIT.length() + BANK_CODE.length(),
                    COUNTRY_CODE.length() + CHECK_DIGIT.length() + BANK_CODE.length() + BRANCH_CODE.length()
            );
            accountNumber = this.iban.substring(
                    COUNTRY_CODE.length() + CHECK_DIGIT.length() + BANK_CODE.length() + BRANCH_CODE.length(),
                    this.iban.length() - CHECK_BANK_REFERENCE_DIGIT.length()
            );
            checkBankReferenceDigit = Integer.parseInt(this.iban.substring(
                    this.iban.length() - CHECK_BANK_REFERENCE_DIGIT.length())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("(iban) could not be parsed");
        }

        component = new BankAccountIdentificationComponent(
                bankCode, branchCode, countryCode, accountNumber, checkDigit, checkBankReferenceDigit
        );
        rib = component.getBankCode() +
                component.getBranchCode() +
                accountNumber +
                String.format("%02d", checkBankReferenceDigit);
        if (isIdentificationNotConsistent()) {
            throw new IllegalArgumentException("(iban) The given Identification could not be verified");
        }
    }

    public boolean isIdentificationNotConsistent() {
        if (component == null ||
                component.getCheckDigit() == null ||
                component.getCheckBankReferenceDigit() == null ||
                component.getAccountNumber() == null) {
            return true;
        }
        try {
            int checkDigit = BankAccountIdentificationGeneration.calculateCheckDigit(
                    component.getAccountNumber(), component
            );
            int checkBankReferenceDigit = BankAccountIdentificationGeneration.calculateCheckBankReferenceDigit(
                    component.getAccountNumber(), component
            );

            //SonarLint bug: both variable are checked before being used;

            boolean isDigitValidated = checkDigit != component.getCheckDigit();
            boolean isBankReferenceDigitValidated = checkBankReferenceDigit != component.getCheckBankReferenceDigit();
            return isDigitValidated || isBankReferenceDigitValidated;
        } catch (Exception e) {
            return true;
        }
    }

    @Iban
    public String getEncryptedIban() throws IllegalStateException {
        if (isIdentificationNotConsistent()) {
            throw new IllegalStateException(ERR_MESSAGE);
        }
        return cypher.encrypt(iban, EncryptedDataPrefix.IBAN.prefix());
    }

    @Iban
    public String getEncryptedRib() throws IllegalStateException {
        if (isIdentificationNotConsistent()) {
            throw new IllegalStateException(ERR_MESSAGE);
        }
        return cypher.encrypt(rib, EncryptedDataPrefix.RIB.prefix());
    }

    @Iban
    public String getIban() throws IllegalStateException {
        if (isIdentificationNotConsistent()) {
            throw new IllegalStateException(ERR_MESSAGE);
        }
        return iban;
    }

    public String getRIB() throws IllegalStateException {
        if (isIdentificationNotConsistent()) {
            throw new IllegalStateException(ERR_MESSAGE);
        }
        return rib;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountIdentification ibanToCheck = (BankAccountIdentification) o;
        return Objects.equals(iban, ibanToCheck.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }
}
