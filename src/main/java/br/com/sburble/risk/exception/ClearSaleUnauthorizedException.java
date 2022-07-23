package br.com.sburble.risk.exception;

import org.springframework.http.HttpStatus;

import br.com.sburble.risk.util.Constants;

public class ClearSaleUnauthorizedException extends BusinessException {

	private static final long serialVersionUID = -9217411743882970391L;
	
	public ClearSaleUnauthorizedException() {
		super(HttpStatus.BAD_GATEWAY,null,Constants.UNAUTHORIZED,Constants.UNAUTHORIZED_TOKEN_CLEARSALE);
	}

}
