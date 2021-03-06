package co.blackdoglabs.common.validation;

import co.blackdoglabs.common.validation.cutomValidator.AntiSamyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.Class;import java.lang.String;import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Removes any javascript attacks from supplied values
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = AntiSamyValidator.class)
public @interface NoJavascriptTags {

    String message() default "AntiSamyValidator found script tags in this request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
