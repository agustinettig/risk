package br.com.sburble.risk.exception;

import org.springframework.http.HttpStatus;

import br.com.sburble.risk.util.Constants;

public class ClearSaleBadRequestException extends BusinessException {

	private static final long serialVersionUID = -9217411743882970391L;

	public ClearSaleBadRequestException(String description) {
		super(HttpStatus.BAD_REQUEST,null,Constants.BAD_REQUEST_CLEARSALE,description);
	}

	public ClearSaleBadRequestException(String message, String description) {
		super(HttpStatus.BAD_REQUEST,null,message,description);
	}
}
