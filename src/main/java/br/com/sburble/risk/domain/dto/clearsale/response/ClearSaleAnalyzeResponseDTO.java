package br.com.sburble.risk.domain.dto.clearsale.response;

import java.io.Serializable;
import java.util.List;

import br.com.sburble.risk.domain.dto.clearsale.ClearSaleOrderDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleAnalyzeResponseDTO implements Serializable {

	private static final long serialVersionUID = -1212967831073739304L;
	
	private String packageID;

	private List<ClearSaleOrderDTO> orders;
	
}