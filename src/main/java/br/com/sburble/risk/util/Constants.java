package br.com.sburble.risk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

	public static final String UNAUTHORIZED = "Internal authentication failed";
	public static final String UNAUTHORIZED_TOKEN_CLEARSALE = "Authentication failed on ClearSale";
	public static final String BAD_REQUEST_CLEARSALE = "Failed to submit for analysis on ClearSale";
	public static final String BAD_GATEWAY_CLEARSALE ="Unable to access ClearSale";
	public static final String ACCESS_FAILED_CLEARSALE ="Unable to access ClearSale";
	public static final String BEARER = "Bearer ";
	public static final String LOG_ERROR_DEFAULT = "method={} event={} httpCode={} message={} description={}";
	public static final String EXCEPTION = "exception";
	public static final int CPF_LENGTH = 11;
	public static final int CNPJ_LENGTH = 14;
	public static final int CUSTOMER_PHONE_NUMBER_LENGTH = 8;
	public static final int CUSTOMER_PHONE_NUMBER_LENGTH_WITH_NINE_IN_FRONT = 9;
	public static final int PHYSICAL_PERSON = 1;
	public static final int LEGAL_PERSON = 2;
	public static final Integer CARD_TYPE = 1;
	public static final String ACCEPT = "application/json";
	public static final String ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";	
}
