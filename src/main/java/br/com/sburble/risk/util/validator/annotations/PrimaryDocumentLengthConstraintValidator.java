package br.com.sburble.risk.util.validator.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.sburble.risk.util.Constants;

public class PrimaryDocumentLengthConstraintValidator
		implements ConstraintValidator<PrimaryDocumentLengthConstraint, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return isNotNull(value) && documentNumberHasPhoneNumberRegex(value)
				&& documentNumberHasPhoneNumberLength(value);
	}

	private boolean isNotNull(String contactNumber) {
		return contactNumber != null;
	}

	private boolean documentNumberHasPhoneNumberRegex(String documentNumber) {
		return documentNumber.matches("[0-9]+");
	}

	private boolean documentNumberHasPhoneNumberLength(String documentNumber) {
		return documentNumber.length() == Constants.CPF_LENGTH || documentNumber.length() == Constants.CNPJ_LENGTH;
	}
}
