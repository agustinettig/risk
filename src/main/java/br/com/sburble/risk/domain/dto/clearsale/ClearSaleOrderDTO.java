package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleOrderDTO implements Serializable {
	
	private static final long serialVersionUID = -8049307185457006460L;
	
	private String code;
	private String status;
	private BigDecimal score;

}
