package br.com.sburble.risk.exception;

import org.springframework.http.HttpStatus;

import br.com.sburble.risk.util.Constants;

public class ClearSaleAccessException extends BusinessException {

	private static final long serialVersionUID = -5775042962725057223L;
	
	public ClearSaleAccessException(String description) {
		super(HttpStatus.BAD_GATEWAY,null,Constants.BAD_GATEWAY_CLEARSALE,description);
	}

}
