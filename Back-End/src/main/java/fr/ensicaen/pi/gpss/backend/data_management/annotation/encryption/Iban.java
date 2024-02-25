package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

import java.lang.annotation.*;

@Documented
@Valid
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IbanValidator.class})
public @interface Iban {
    String message() default "Field must be a valid IBAN (either encrypted with back-end encryption method or plain)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
