package fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

import java.lang.annotation.*;

@Documented
@Valid
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PanValidator.class})
public @interface Pan {
    String message() default "Field must be a valid PAN (either encrypted with back-end encryption method or plain)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
