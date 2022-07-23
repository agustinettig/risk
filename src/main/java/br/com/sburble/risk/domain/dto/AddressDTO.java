package br.com.sburble.risk.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.sburble.risk.domain.enums.AddressStateEnum;
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
public class AddressDTO implements Serializable {

	private static final long serialVersionUID = 5058662654583858368L;

	@ApiModelProperty(value = "Street", example="John Doe St.", required = true)
	@Size(min = 1, max = 200)
	@NotBlank
	private String street;

	@ApiModelProperty(value = "Number",example="\"123\"", required = true)
	@Size(min = 1, max = 15)
	@NotBlank
	private String number;

	@ApiModelProperty(value = "Additional information",example="Close to the train station", required = false)
	@Size(max = 250)
	private String additionalInformation;

	@ApiModelProperty(value = "County", example="County name", required = true)
	@Size(min = 1, max = 150)
	@NotBlank
	private String county;

	@ApiModelProperty(value = "City", example="City name",required = true)
	@Size(min = 1, max = 150)
	@NotBlank
	private String city;

	@ApiModelProperty(value = "State", example="ST",required = true)
	@NotNull
	private AddressStateEnum state;

	@ApiModelProperty(value = "Zip code", example="\"10010010\"",required = true)
	@NotBlank
	@Pattern(regexp = "^[0-9]{8}$", message = "Must contain only numbers and have 8 digits")
	private String zipCode;

	@ApiModelProperty(value = "Reference", example="Some reference",required = false)
	@Size(max = 250)
	private String reference;
}
