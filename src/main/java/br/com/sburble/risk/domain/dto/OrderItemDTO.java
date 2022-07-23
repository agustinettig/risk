package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
public class OrderItemDTO implements Serializable {

	private static final long serialVersionUID = 2936852643784948338L;

	@ApiModelProperty(value = "Name", example = "Candy", required = true)
	@Size(max = 150)
	@NotBlank
	private String name;

	@ApiModelProperty(value = "Unit price", example = "2.00", required = false)
	@Digits(integer = 20, fraction = 4)
	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal unitPrice;

	@ApiModelProperty(value = "Ammount", example = "2", required = false)
	@Positive
	private Integer amount;
}
