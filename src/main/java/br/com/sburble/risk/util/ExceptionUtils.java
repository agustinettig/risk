package br.com.sburble.risk.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleDefaultErrorMessageTemplate;
import br.com.sburble.risk.exception.BusinessException;
import br.com.sburble.risk.exception.ClearSaleAccessException;
import br.com.sburble.risk.exception.ClearSaleBadRequestException;
import br.com.sburble.risk.exception.ClearSaleUnauthorizedException;
import feign.FeignException;
import feign.RetryableException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

	private static final String ORDER_ALREADY_EXISTS = "existing-orders";

	public static BusinessException feignToBusinessException(FeignException ex) {
		try {			
			
			if (!(ex instanceof RetryableException)) {
				var status = HttpStatus.valueOf(ex.status());
				
				if (HttpStatus.BAD_REQUEST.equals(status)) {
					return getBadRequestException(ex);
				}
				
				if (HttpStatus.UNAUTHORIZED.equals(status)) {
					return new ClearSaleUnauthorizedException();
				}				
			}
			
			return new ClearSaleAccessException(Constants.ACCESS_FAILED_CLEARSALE);
		} catch (Exception e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error converting exception",
					"Falha ao converter Exceção");
		}
	}

	private static ClearSaleBadRequestException getBadRequestException(FeignException ex) throws IOException {		

		var exceptionMessage = new ObjectMapper().readValue(ex.content(), ClearSaleDefaultErrorMessageTemplate.class);

		List<String> errorMessageList = exceptionMessage.getModelState().getDetails().entrySet().stream()
				.map(entry -> translateMappedErrorMessageKeysToPtBr(entry.getKey()) + ":" + entry.getValue().toString())
				.collect(Collectors.toList());

		return new ClearSaleBadRequestException(String.join(",", errorMessageList));
	}

	private static String translateMappedErrorMessageKeysToPtBr(String key) {
		if (key.equals(ORDER_ALREADY_EXISTS)) {
			return "Existing orders";
		}

		return key;
	}
}
