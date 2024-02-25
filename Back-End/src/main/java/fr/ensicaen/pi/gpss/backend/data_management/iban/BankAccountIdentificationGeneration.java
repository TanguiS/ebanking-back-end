package fr.ensicaen.pi.gpss.backend.data_management.iban;

import fr.ensicaen.pi.gpss.backend.tools.StringOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

import static fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationLength.*;

@Validated
public class BankAccountIdentificationGeneration {
    private final BankAccountIdentificationComponent component;
    @Getter
    private String iban;

    protected BankAccountIdentificationGeneration(@NonNull BankAccountIdentificationComponent component) {
        this.component = component;
        generateArgs();
    }

    private void generateArgs() {
        String accountNumber = StringOperation.generateRandomStringWithNumberAndUpperCase(ACCOUNT_NUMBER.length());
        int checkDigit = calculateCheckDigit(accountNumber, component);
        int checkBankReferenceDigit = calculateCheckBankReferenceDigit(
                accountNumber,
                component
        );
        iban = generateBankAccountValues(checkDigit, checkBankReferenceDigit, accountNumber, component);
    }

    protected static int calculateCheckDigit(
            @NotBlank String accountNumber, @NonNull BankAccountIdentificationComponent component
    ) {
        String initIban = component.getCountryCode() +
                "00" +
                component.getBankCode() +
                component.getBranchCode() +
                accountNumber;
        String reversed = initIban.substring(COUNTRY_CODE.length() + CHECK_DIGIT.length()) +
                initIban.substring(0, COUNTRY_CODE.length() + CHECK_DIGIT.length());
        BigInteger numericReversedIban = StringOperation.stringToNumeric(reversed);
        BigInteger remain = numericReversedIban.mod(BigInteger.valueOf(component.getModulus()));
        return component.getModulus() + BankAccountIdentificationShift.MODULUS_CHECK_DIGIT.shift() - remain.intValue();
    }

    protected static int calculateCheckBankReferenceDigit(
            @NotBlank String accountNumber,
            @NonNull BankAccountIdentificationComponent component
    ) {
        BigInteger bankCode = StringOperation.stringToNumeric(component.getBankCode());
        BigInteger branchCode = StringOperation.stringToNumeric(component.getBranchCode());
        BigInteger accountNumberNumeric = StringOperation.stringToNumeric(accountNumber);
        BigInteger sumWithWeight = bankCode.multiply(
                BigInteger.valueOf(BankAccountIdentificationShift.BANK_CODE.shift()))
                .add(
                        branchCode.multiply(
                                BigInteger.valueOf(BankAccountIdentificationShift.BRANCH_CODE.shift())
                        )
                )
                .add(
                        accountNumberNumeric.multiply(
                                BigInteger.valueOf(BankAccountIdentificationShift.ACCOUNT_NUMBER.shift())
                        )
                );
        return BigInteger.valueOf(component.getModulus())
                .subtract(
                        sumWithWeight.mod(
                                BigInteger.valueOf(component.getModulus())
                        )
                )
                .intValue();
    }

    protected static String generateBankAccountValues(
            @Positive int checkDigit,
            @Positive int checkBankReferenceDigit,
            @NotBlank String accountNumber,
            @NonNull BankAccountIdentificationComponent component
    ) {
        return component.getCountryCode() +
                String.format("%02d", checkDigit) +
                component.getBankCode() +
                component.getBranchCode() +
                accountNumber +
                String.format("%02d", checkBankReferenceDigit);
    }
}
