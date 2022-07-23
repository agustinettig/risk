package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 8078931715194841365L;

	@ApiModelProperty(value = "Order id", example = "\"10000036495\"", required = true)
	@NotBlank
	@Size(min = 1, max = 50)
	private String id;

	@ApiModelProperty(value = "Order date", example = "2022-06-06 12:00:00", required = true)
	@NotNull
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "${default.date.locale}", timezone = "${default.date.timeZone}")
	private LocalDateTime date;

	@ApiModelProperty(value = "Total order value (composed of the total value of the items + shipping value + possible interest value of the purchase)", example = "27.00", required = true)
	@Digits(integer = 20, fraction = 4)
	@DecimalMin(value = "0.0", inclusive = true)
	@NotNull
	private BigDecimal totalValue;

	@ApiModelProperty(value = "Order items", required = true)
	@Valid
	@NotEmpty
	private List<OrderItemDTO> items;

}
