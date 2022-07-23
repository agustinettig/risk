package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import br.com.sburble.risk.domain.enums.CustomerGenderEnum;
import br.com.sburble.risk.util.validator.annotations.PrimaryDocumentLengthConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {

	private static final long serialVersionUID = 4743535393363314431L;

	@ApiModelProperty(value = "Primary document", example = "\"12345678912\"", required = true)
	@NotBlank
	@Pattern(regexp = "^[0-9]*$", message = "Must contain only numbers")
	@PrimaryDocumentLengthConstraint
	private String primaryDocument;

	@ApiModelProperty(value = "Name", example = "John Doe", required = true)
	@NotBlank
	@Size(min = 1, max = 500)
	private String name;

	@ApiModelProperty(value = "E-mail", example = "johndoe@mail.com", required = true)
	@NotBlank
	@Email
	@Size(min = 1, max = 150)
	private String email;

	@ApiModelProperty(value = "Birth date", example = "1991-01-01", required = false)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "${default.date.locale}", timezone = "${default.date.timeZone}")
	private LocalDate birthDate;

	@ApiModelProperty(value = "Gender", example = "M", required = false)
	private CustomerGenderEnum gender;

	@ApiModelProperty(value = "Billing address", required = false)
	@Valid
	private AddressDTO billingAddress;

	@ApiModelProperty(value = "Phone list", required = true)
	@Valid
	@NotEmpty
	private List<PhoneDTO> phones;

}
