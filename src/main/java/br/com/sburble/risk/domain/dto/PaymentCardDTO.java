package br.com.sburble.risk.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class PaymentCardDTO implements Serializable{

	private static final long serialVersionUID = 1887480264872525134L;

	@ApiModelProperty(value = "First 6 digits", example = "\"123456\"", required = true)
	@NotBlank
	@Pattern(regexp = "^[0-9]{6}$", message = "Must contain only numbers and have 6 digits")
	private String bin;

	@ApiModelProperty(value = "Last 4 digits", example = "\"7890\"", required = true)
	@NotBlank
	@Pattern(regexp = "^[0-9]{4}$", message = "Must contain only numbers and have 4 digits")
	private String end;

	@ApiModelProperty(value = "Owner name", example = "John Doe", required = true)
	@Size(min = 1, max = 150)
	@NotBlank
	private String ownerName;
}
