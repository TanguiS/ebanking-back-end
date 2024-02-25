package fr.ensicaen.pi.gpss.backend.data_management.annotation.encoded;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EncodedValidator implements ConstraintValidator<Encoded, String> {
    private final Pattern encodedPattern;

    public EncodedValidator() {
        String regex = "\\A\\$2a\\$\\d\\d\\$[./0-9A-Za-z]{53}";
        encodedPattern = Pattern.compile(regex);
    }

    @Override
    public void initialize(Encoded constraintAnnotation) {
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
        return encodedPattern.matcher(value).matches();
    }
}
