package br.com.sburble.risk.util;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sburble.risk.exception.BusinessException;
import br.com.sburble.risk.exception.ClearSaleAccessException;
import br.com.sburble.risk.exception.ClearSaleBadRequestException;
import br.com.sburble.risk.exception.ClearSaleUnauthorizedException;
import feign.FeignException;
import feign.RetryableException;

@ExtendWith(MockitoExtension.class)
class ExceptionUtilsTest {

	private static final String FEIGN_BAD_REQUEST_RESPONSE__MESSAGE = "The request is invalid.";
	private static final String FEIGN_BAD_REQUEST_RESPONSE_BODY__WHEN_CODE_ALREADY_EXISTS = "{\"Message\":\"The request is invalid.\",\"ModelState\":{\"existing-orders\":[\"ORDER_EXAMPLE_2_0_1\"]}}";
	private static final String FEIGN_BAD_REQUEST_RESPONSE_BODY__WHEN_JSON_SENDED_IS_EMPTY = "{\"Message\":\"The request is invalid.\",\"ModelState\":{\"\":[\"Json has no values\"]}}";

	@Test
	void feignToBusiness_whenFeignIsBadRequestOfTypeOrderAlreadyRegistered_thenExpectedBusinessException() {
		var expectedException = new ClearSaleBadRequestException(Constants.BAD_REQUEST_CLEARSALE,
				"Existing orders:[ORDER_EXAMPLE_2_0_1]");

		var response = ExceptionUtils
				.feignToBusinessException(new FeignException.BadRequest(FEIGN_BAD_REQUEST_RESPONSE__MESSAGE,
						FEIGN_BAD_REQUEST_RESPONSE_BODY__WHEN_CODE_ALREADY_EXISTS.getBytes()));

		assertTrue(response.getDescription().contains(expectedException.getDescription()));
		assertTrue(response.getMessage().contains(expectedException.getMessage()));
		assertEquals(expectedException.getHttpStatusCode(), response.getHttpStatusCode());
	}

	@Test
	void feignToBusiness_whenFeignIsBadRequestOfTypeEmptyJson_thenExpectedBusinessException() {
		var expectedException = new ClearSaleBadRequestException(Constants.BAD_REQUEST_CLEARSALE,
				"Json has no values");
		var response = ExceptionUtils
				.feignToBusinessException(new FeignException.BadRequest(FEIGN_BAD_REQUEST_RESPONSE__MESSAGE,
						FEIGN_BAD_REQUEST_RESPONSE_BODY__WHEN_JSON_SENDED_IS_EMPTY.getBytes()));

		assertTrue(response.getDescription().contains(expectedException.getDescription()));
		assertTrue(response.getMessage().contains(expectedException.getMessage()));
		assertEquals(expectedException.getHttpStatusCode(), response.getHttpStatusCode());
	}

	@Test
	void feignToBusiness_whenFeignIsInternalServeError_thenExpectedBusinessException() {
		assertThrows(BusinessException.class, () -> {
			ExceptionUtils.feignToBusinessException(null);
		});
	}
	
	@Test
	void feignToBusiness_whenFeignIsUnauthorized_thenExpectedBusinessException() {
		var expectedException = new ClearSaleUnauthorizedException();
		var response = ExceptionUtils
				.feignToBusinessException(new FeignException.Unauthorized("", null));

		assertEquals(expectedException, response);
	}
	
	@Test
	void feignToBusiness_whenFeignIsNotMapped_thenExpectedBusinessException() {
		var expectedException = new ClearSaleAccessException(Constants.ACCESS_FAILED_CLEARSALE);
		var response = ExceptionUtils
				.feignToBusinessException(new FeignException.InternalServerError("", null));

		assertEquals(expectedException, response);
	}
	
	@Test
	void feignToBusiness_whenRetryableException_thenExpectedClearSaleAccessException() {
		var expectedException = new ClearSaleAccessException(Constants.ACCESS_FAILED_CLEARSALE);
		var response = ExceptionUtils
				.feignToBusinessException(new RetryableException(-1, null, null, null));

		assertEquals(expectedException, response);
	}
}
