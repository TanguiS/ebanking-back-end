package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

import java.lang.annotation.*;

@Documented
@Valid
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IbanOrPanValidator.class})
public @interface IbanOrPan {
    String message() default "Field must be a valid IBAN or PAN (either encrypted with back-end encryption method or plain)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
