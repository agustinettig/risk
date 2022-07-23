package br.com.sburble.risk.domain.dto.clearsale.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.sburble.risk.domain.dto.clearsale.ClearSaleBillingDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleItemDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSalePaymentDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleShippingDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleAnalyzeRequestDTO implements Serializable {

	private static final long serialVersionUID = 3222360124975317724L;

	private String code;
	private String sessionID;
	private LocalDateTime date;
	private String email;
	private BigDecimal totalValue;
	private String observation;
	private String origin;
	private Integer customSla;
	private ClearSaleBillingDTO billing;
	private ClearSaleShippingDTO shipping;
	private List<ClearSaleItemDTO> items;
	private List<ClearSalePaymentDTO> payments;

}
