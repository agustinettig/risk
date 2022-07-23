package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import br.com.sburble.risk.domain.enums.CustomerGenderEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleBillingDTO implements Serializable {

	private static final long serialVersionUID = 1759313569067966422L;

	private Integer type;
	private String primaryDocument;
	private String name;
	private LocalDate birthDate;
	private String email;
	private CustomerGenderEnum gender;
	private ClearSaleAddressDTO address;
	private List<ClearSalePhoneDTO> phones;
}