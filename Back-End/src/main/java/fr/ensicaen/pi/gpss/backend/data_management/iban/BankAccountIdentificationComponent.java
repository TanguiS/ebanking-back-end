package fr.ensicaen.pi.gpss.backend.data_management.iban;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class BankAccountIdentificationComponent {
    private final String bankCode;
    private final String branchCode;
    private final String countryCode;
    private final Integer modulus;
    private final String accountNumber;
    private final Integer checkDigit;
    private final Integer checkBankReferenceDigit;

    public BankAccountIdentificationComponent(
            @NotBlank String bankCode,
            @NotBlank String branchCode,
            @NotBlank String countryCode,
            @Nullable String accountNumber,
            @Nullable Integer checkDigit,
            @Nullable Integer checkBankReferenceDigit
    ) {
        this.bankCode = bankCode;
        this.branchCode = branchCode;
        this.countryCode = countryCode;
        modulus = BankAccountIdentificationShift.MODULUS.shift();
        this.accountNumber = accountNumber;
        this.checkDigit = checkDigit;
        this.checkBankReferenceDigit = checkBankReferenceDigit;
    }

    public BankAccountIdentificationComponent(
            @NonNull String bankCode,
            @NonNull String branchCode,
            @NonNull String countryCode
    ) {
        this(bankCode, branchCode, countryCode, null, null, null);
    }

    @NonNull
    public String getBankCode() {
        return bankCode;
    }

    @NonNull
    public String getBranchCode() {
        return branchCode;
    }

    @NonNull
    public String getCountryCode() {
        return countryCode;
    }

    @NonNull
    public Integer getModulus() {
        return modulus;
    }

    @Nullable
    public String getAccountNumber() {
        return accountNumber;
    }

    @Nullable
    public Integer getCheckDigit() {
        return checkDigit;
    }

    @Nullable
    public Integer getCheckBankReferenceDigit() {
        return checkBankReferenceDigit;
    }
}
