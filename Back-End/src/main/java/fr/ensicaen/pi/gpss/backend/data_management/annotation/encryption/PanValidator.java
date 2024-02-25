package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PanValidator implements ConstraintValidator<Pan, String> {
    private final PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration;

    public PanValidator(
            PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration
    ) {
        this.primaryAccountNumberConfiguration = primaryAccountNumberConfiguration;
    }

    @Override
    public void initialize(Pan constraintAnnotation) {
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
            primaryAccountNumberConfiguration.fromExistingPan(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
