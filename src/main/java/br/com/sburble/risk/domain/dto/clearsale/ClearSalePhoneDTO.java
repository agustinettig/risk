package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSalePhoneDTO implements Serializable {

	private static final long serialVersionUID = 1784283375058234663L;

	private Integer type;
	private Integer number;
	private Integer ddd;
	private String extension;
}