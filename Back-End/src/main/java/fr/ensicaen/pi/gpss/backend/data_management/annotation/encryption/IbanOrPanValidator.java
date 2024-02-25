package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class IbanOrPanValidator implements ConstraintValidator<IbanOrPan, String> {
    private final IbanValidator ibanValidator;
    private final PanValidator panValidator;

    public IbanOrPanValidator(IbanValidator ibanValidator, PanValidator panValidator) {
        this.ibanValidator = ibanValidator;
        this.panValidator = panValidator;
    }

    @Override
    public void initialize(IbanOrPan constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ibanValidator.isValid(value, context) || panValidator.isValid(value, context);
    }
}
