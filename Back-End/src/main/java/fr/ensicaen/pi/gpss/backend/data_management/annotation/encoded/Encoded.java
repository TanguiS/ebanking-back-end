package fr.ensicaen.pi.gpss.backend.data_management.annotation.encoded;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

import java.lang.annotation.*;

@Documented
@Valid
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EncodedValidator.class})
public @interface Encoded {
    String message() default "Field must be an encoded one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
