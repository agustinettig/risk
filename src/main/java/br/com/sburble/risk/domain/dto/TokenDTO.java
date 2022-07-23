package br.com.sburble.risk.domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.sburble.risk.util.Constants;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenDTO implements Serializable {
	
	private static final long serialVersionUID = 7805252967226903183L;

	@JsonProperty("Token")
	private String token;
	
	@JsonProperty("ExpirationDate")
	private String expirationDate;
	
	public String getBearerToken() {
		return Constants.BEARER + this.token;
	}

}
