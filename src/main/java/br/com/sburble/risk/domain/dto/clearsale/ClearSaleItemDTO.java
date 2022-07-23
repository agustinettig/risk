package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleItemDTO implements Serializable {

	private static final long serialVersionUID = 7212316388504477754L;

	private String name;
	private BigDecimal value;
	private Integer amount;
}