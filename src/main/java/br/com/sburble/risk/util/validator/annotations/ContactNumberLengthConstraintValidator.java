package br.com.sburble.risk.util.validator.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.sburble.risk.util.Constants;

public class ContactNumberLengthConstraintValidator
		implements ConstraintValidator<ContactNumberLengthConstraint, Integer> {

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return value != null && validate(value);
	}

	private boolean validate(Integer value) {
		var contactNumber = String.valueOf(value);
		return contactNumber.length() == Constants.CUSTOMER_PHONE_NUMBER_LENGTH
				|| contactNumber.length() == Constants.CUSTOMER_PHONE_NUMBER_LENGTH_WITH_NINE_IN_FRONT;
	}
}
