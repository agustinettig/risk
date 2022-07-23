package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSalePaymentDTO implements Serializable {

	private static final long serialVersionUID = 8763802569968270019L;

	private Integer type;
	private ClearSaleCardDTO card;
}