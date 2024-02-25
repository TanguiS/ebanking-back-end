package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class IbanValidator implements ConstraintValidator<Iban, String> {
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;

    public IbanValidator(
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
    ) {
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }

    @Override
    public void initialize(Iban constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.isEmpty()) {
            return false;
        }

        try {
            bankAccountIdentificationConfiguration.fromExistingIban(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
