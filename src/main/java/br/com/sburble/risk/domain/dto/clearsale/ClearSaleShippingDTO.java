package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.sburble.risk.domain.enums.CustomerGenderEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleShippingDTO implements Serializable {

	private static final long serialVersionUID = 7587971472719773643L;

	private Integer type;
	private String primaryDocument;
	private String name;
	private LocalDate birthDate;
	private String email;
	private CustomerGenderEnum gender;
	private String deliveryType;
	private BigDecimal price;
	private ClearSaleAddressDTO address;
	private List<ClearSalePhoneDTO> phones;
}