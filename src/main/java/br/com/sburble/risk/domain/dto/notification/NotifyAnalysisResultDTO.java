package br.com.sburble.risk.domain.dto.notification;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.sburble.risk.domain.enums.ClearsaleStatusResponseEnum;
import br.com.sburble.risk.domain.enums.ClearsaleStatusResponseSimplifiedEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotifyAnalysisResultDTO implements Serializable {
	
	private static final long serialVersionUID = -6157164243534120473L;

	@ApiModelProperty(value = "Order code", example = "542365735", required = true)
	@NotBlank
	@Size(min = 1, max = 50)
	private String code;
	
	@ApiModelProperty(value = "Order analysis status", example = "AMA", required = true)
	@NotNull
	private ClearsaleStatusResponseEnum status;
	
	@ApiModelProperty(value = "Simplified status of the order analysis", example = "APPROVED", required = true)
	private ClearsaleStatusResponseSimplifiedEnum statusSimplified;
	
	@ApiModelProperty(value = "Order score", example = "99", required = true)
	@NotNull
	private BigDecimal score;
	
}
