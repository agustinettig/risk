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
@Constraint(validatedBy = ContactNumberLengthConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactNumberLengthConstraint {
	String message() default "Field must have " + Constants.CUSTOMER_PHONE_NUMBER_LENGTH + " or "
			+ Constants.CUSTOMER_PHONE_NUMBER_LENGTH_WITH_NINE_IN_FRONT + " digits";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
