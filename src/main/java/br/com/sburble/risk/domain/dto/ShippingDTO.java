package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import br.com.sburble.risk.domain.enums.ShippingDeliveryTypeEnum;
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
public class ShippingDTO implements Serializable {

	private static final long serialVersionUID = 6923480905461402254L;

	@ApiModelProperty(value = "Delivery type", example = "EXPRESS", required = true)
	private ShippingDeliveryTypeEnum deliveryType;

	@ApiModelProperty(value = "Delivery price", example = "9.00", required = true)
	@Digits(integer = 20, fraction = 4)
	@DecimalMin(value = "0.0", inclusive = true)
	private BigDecimal price;

	@ApiModelProperty(value = "Delivery address", required = true)
	@Valid
	private AddressDTO address;

}
