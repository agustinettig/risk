package br.com.sburble.risk.util.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.sburble.risk.util.Constants;

@Documented
@Constraint(validatedBy = PrimaryDocumentLengthConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryDocumentLengthConstraint {

	String message() default "Field must have " + Constants.CPF_LENGTH + " or " + Constants.CNPJ_LENGTH + " digits";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}