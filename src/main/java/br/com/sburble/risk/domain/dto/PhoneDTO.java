package br.com.sburble.risk.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;

import br.com.sburble.risk.domain.enums.PhoneTypeEnum;
import br.com.sburble.risk.util.validator.annotations.ContactNumberLengthConstraint;
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
public class PhoneDTO implements Serializable{

	private static final long serialVersionUID = 4581575311703210966L;

	@ApiModelProperty(value = "Type", example = "UNDEFINED", required = true)
	@NotNull
	@Value("${customer.phoneType.default}")
	private PhoneTypeEnum type;

	@ApiModelProperty(value = "DDD", example = "11", required = true)
	@Positive
	@NotNull
	@Min(value = 11)
	@Max(value = 99)
	private Integer ddd;

	@ApiModelProperty(value = "Contact number", example = "123456789", required = true)
	@Positive
	@NotNull
	@ContactNumberLengthConstraint
	private Integer number;

	@ApiModelProperty(value = "Extension", example = "\"203\"", required = false)
	@Size(max = 10)
	private String extension;

}
