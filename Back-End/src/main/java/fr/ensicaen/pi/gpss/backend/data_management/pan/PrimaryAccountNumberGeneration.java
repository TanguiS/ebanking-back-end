package fr.ensicaen.pi.gpss.backend.data_management.pan;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.tools.StringOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import static fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberLength.ACCOUNT_IDENTIFIER;

@Validated
public class PrimaryAccountNumberGeneration {
    private final String pan;
    private final BankAccountIdentification iban;
    private final PrimaryAccountNumberComponent component;

    protected PrimaryAccountNumberGeneration(
            @NonNull BankAccountIdentification iban,
            @NonNull PrimaryAccountNumberComponent component
    ) {
        if (iban.isIdentificationNotConsistent()) {
            throw new IllegalArgumentException("iban could not be verified");
        }
        this.iban = iban;
        this.component = component;
        pan = generatePan();
    }

    private String generatePan() {
        String str = StringOperation.generateRandomStringWithNumberBaseOnSeed(
                ACCOUNT_IDENTIFIER.length(), iban.getEncryptedRib()
        );
        String generatedPan = component.getBin() + str;
        int luhn = generateCheckLuhn(generatedPan);
        return generatedPan + luhn;
    }

    public static int generateCheckLuhn(@NotBlank String panWithoutLuhn) {
        String reversedPanWithoutLuhn = new StringBuilder(panWithoutLuhn).reverse().toString();

        int index = 0;
        int sum = 0;
        for (char digitChar : reversedPanWithoutLuhn.toCharArray()) {
            int digit = Character.getNumericValue(digitChar);
            index++;
            int toSumDigit = isOdd(index) ? PrimaryAccountNumberShift.EVEN.shift() * digit : digit;
            sum += (toSumDigit > PrimaryAccountNumberShift.MAX_DIGIT.shift()) ? sumOfDigits(toSumDigit) : toSumDigit;
        }
        int modulus = PrimaryAccountNumberShift.MODULUS.shift();
        return (modulus - sum % modulus) % modulus;
    }

    private static boolean isOdd(@Positive int index) {
        return index % PrimaryAccountNumberShift.EVEN.shift() != 0;
    }

    private static int sumOfDigits(@Positive int number) {
        return Character.getNumericValue(Integer.toString(number).charAt(0)) +
                Character.getNumericValue(Integer.toString(number).charAt(1));
    }

    protected String getPAN() {
        return pan;
    }
}
