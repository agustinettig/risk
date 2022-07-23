package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
public class RiskDTO implements Serializable{

	private static final long serialVersionUID = 4315882882827654629L;

	@ApiModelProperty(value = "Order origin channel (ie: SITE, APP)", example = "APP", required = false)
	@Size(max = 150)
	private String origin;

	@ApiModelProperty(value = "Observarion", example = "DROGASIL", required = false)
	@Size(max = 250)
	private String observation;

	@ApiModelProperty(value = "Maximum analysis response time (in minutes)", example = "40", required = false)
	@PositiveOrZero
	private Integer customSla;

	@ApiModelProperty(value = "User session ID at time of the order", example = "iuevrtv578yn457vh", required = true)
	@NotBlank
	@Size(max = 128)
	private String fingerprintClearSale;

	@ApiModelProperty(value = "Order data", required = true)
	@Valid
	@NotNull
	private OrderDTO order;

	@ApiModelProperty(value = "Customer data", required = true)
	@Valid
	@NotNull
	private CustomerDTO customer;

	@ApiModelProperty(value = "Delivery address", required = true)
	@Valid
	private ShippingDTO shipping;

	@ApiModelProperty(value = "List of cards used in the order", required = true)
	@Valid
	@NotEmpty
	private List<PaymentCardDTO> paymentCards;

}
