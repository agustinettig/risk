package br.com.sburble.risk.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleOrderDTO;
import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleAnalyzeResponseDTO;
import br.com.sburble.risk.service.RiskService;

@WebMvcTest(controllers = {RiskController.class}, excludeAutoConfiguration = {MockMvcSecurityAutoConfiguration.class })
class RiskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RiskService riskService;

	private static final String BASE_URL = "/v1/risks";

	private static final String VALID_REQUEST_BODY = "{\"origin\":\"APP\",\"observation\":\"obs\",\"customSla\":10,\"fingerprintClearSale\":\"1234\",\"order\":{\"id\":1,\"date\":\"2022-06-26 12:00:00\",\"totalValue\":15,\"items\":[{\"name\":\"candy\",\"unitPrice\":1.99,\"amount\":1}]},\"customer\":{\"primaryDocument\":\"12345678901\",\"name\":\"John Doe\",\"email\":\"johndoe@mail.com\",\"birthDate\":\"1991-01-01\",\"gender\":\"M\",\"billingAddress\":{\"street\":\"Test St.\",\"number\":\"1\",\"additionalInformation\":\"\",\"county\":\"County name\",\"city\":\"City name\",\"state\":\"AC\",\"zipCode\":\"12345678\",\"reference\":\"\"},\"phones\":[{\"type\":\"COMERCIAL\",\"ddd\":11,\"number\":912345678,\"extension\":\"\"}]},\"shipping\":{\"deliveryType\":\"21\",\"price\":2.99,\"address\":{\"street\":\"Test St.\",\"number\":\"1\",\"additionalInformation\":\"\",\"county\":\"County name\",\"city\":\"City name\",\"state\":\"AC\",\"zipCode\":\"12345678\",\"reference\":\"\"}},\"paymentCards\":[{\"bin\":\"123456\",\"end\":\"0987\",\"ownerName\":\"John Doe\"}]}";

	private static final String VALID_REQUEST_STATUSCHANGE_BODY = "{\"code\":\"123\",\"date\":\"2022-06-26T12:00:00.0000000-03:00\",\"type\":\"status\"}";
	
	@Test
	void post_whenValid_expectSuccess() throws Exception {
		when(this.riskService.analyze(any(RiskDTO.class))).thenReturn(getClearSaleAnalyzeResponse());
		mockMvc.perform(post(BASE_URL.concat("/analysis"))
					.content(VALID_REQUEST_BODY)	
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(asJsonString(getClearSaleAnalyzeResponse())));
	}
	
	@Test
	void post_whenValid_statusChangeNotifier_expectSuccess() throws Exception {
		mockMvc.perform(post(BASE_URL.concat("/status")).content(VALID_REQUEST_STATUSCHANGE_BODY)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ClearSaleAnalyzeResponseDTO getClearSaleAnalyzeResponse() {
		return ClearSaleAnalyzeResponseDTO.builder()
				.packageID("123456")
				.orders(List.of(ClearSaleOrderDTO.builder()
						.code("1")
						.status("NVO")
						.score(BigDecimal.ONE)
						.build()))
				.build();
	}

}
