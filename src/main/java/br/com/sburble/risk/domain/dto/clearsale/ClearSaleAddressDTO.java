package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleAddressDTO implements Serializable {
	private static final long serialVersionUID = 5850098065784519636L;

	private String street;
	private String number;
	private String additionalInformation;
	private String county;
	private String city;
	private String state;
	private String zipcode;
	private String reference;
}