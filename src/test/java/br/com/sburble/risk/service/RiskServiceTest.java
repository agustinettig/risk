package br.com.sburble.risk.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sburble.risk.domain.dto.AddressDTO;
import br.com.sburble.risk.domain.dto.CustomerDTO;
import br.com.sburble.risk.domain.dto.OrderDTO;
import br.com.sburble.risk.domain.dto.OrderItemDTO;
import br.com.sburble.risk.domain.dto.PaymentCardDTO;
import br.com.sburble.risk.domain.dto.PhoneDTO;
import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.ShippingDTO;
import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import br.com.sburble.risk.domain.dto.TokenDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleCredentialsDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleOrderDTO;
import br.com.sburble.risk.domain.dto.clearsale.request.ClearSaleAnalyzeRequestDTO;
import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleAnalyzeResponseDTO;
import br.com.sburble.risk.domain.dto.notification.NotifyAnalysisResultDTO;
import br.com.sburble.risk.domain.enums.AddressStateEnum;
import br.com.sburble.risk.domain.enums.ClearsaleStatusResponseEnum;
import br.com.sburble.risk.domain.enums.PhoneTypeEnum;
import br.com.sburble.risk.domain.enums.ShippingDeliveryTypeEnum;
import br.com.sburble.risk.exception.BusinessException;
import br.com.sburble.risk.exception.ClearSaleBadRequestException;
import br.com.sburble.risk.exception.ClearSaleUnauthorizedException;
import br.com.sburble.risk.http.ClearSaleClient;
import br.com.sburble.risk.mapper.ClearSaleAnalyzeMapper;
import br.com.sburble.risk.publisher.RiskExchangePublisher;
import br.com.sburble.risk.publisher.RiskQueuePublisher;
import feign.FeignException;

@EnableRetry
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RiskService.class })
@TestPropertySource("/application-test.properties")
class RiskServiceTest {
	
	private static final String FEIGN_ERROR_BAD_REQUEST_RESPONSE__MESSAGE="The request is invalid.";
	private static final String FEIGN_ERROR_BAD_REQUEST_RESPONSE_BODY__WHEN_CODE_ALREADY_EXISTS="{\"Message\":\"The request is invalid.\",\"ModelState\":{\"existing-orders\":[\"ORDER_EXAMPLE_2_0_1\"]}}";
	private static final String FEIGN_ERROR_BAD_REQUEST_RESPONSE_BODY__WHEN_JSON_SENDED_IS_EMPTY="{\"Message\":\"The request is invalid.\",\"ModelState\":{\"\":[\"Json has no values\"]}}";
	
	@Autowired
	private RiskService riskService;

	@MockBean
	private ClearSaleClient clearSaleClient;
	
	@MockBean
	private ClearSaleAnalyzeMapper clearSaleAnalyzeMapper;
	
	@MockBean
	private RiskQueuePublisher riskQueuePublisher;
	
	@MockBean
	private RiskExchangePublisher riskExchangePublisher;
	
	@Test
	void orderAnalize_whenValid_expectedSuccess() throws Exception {
		var expectedResponse = getClearSaleResponse();
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleAnalyzeMapper.mapper(any(RiskDTO.class)))
				.thenReturn(ClearSaleAnalyzeRequestDTO.builder().build());
		when(this.clearSaleClient.postAnalysis(any(String.class), any(ClearSaleAnalyzeRequestDTO.class)))
				.thenReturn(getClearSaleResponse());
		var response = this.riskService.analyze(getRisk());
		assertNotNull(response);
		assertEquals(expectedResponse.getPackageID(), response.getPackageID());
	}

	@Test
	void orderAnalize_whenTokenInvalid_expectedException() throws Exception {
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(Optional.empty());
		var risk = getRisk();
		assertThrows(ClearSaleUnauthorizedException.class, () -> this.riskService.analyze(risk));
	}

	@Test
	void orderAnalize_whenOrderIdIsAlreadyRegisteredInClearSale_expectedClearSaleBadRequestException() throws ClearSaleBadRequestException {
		var expectedRisk = getRisk();
		
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.postAnalysis(any(String.class), any(ClearSaleAnalyzeRequestDTO.class)))
				.thenThrow(new FeignException.BadRequest(FEIGN_ERROR_BAD_REQUEST_RESPONSE__MESSAGE, FEIGN_ERROR_BAD_REQUEST_RESPONSE_BODY__WHEN_CODE_ALREADY_EXISTS.getBytes()));
		when(this.clearSaleAnalyzeMapper.mapper(any(RiskDTO.class)))
				.thenReturn(ClearSaleAnalyzeRequestDTO.builder().build());
		
		assertThrows(ClearSaleBadRequestException.class, () -> {	
			riskService.analyze(expectedRisk);
		});	
	}

	@Test
	void orderAnalize_whenPayloadSendedToAnalyzeIsEmpty_expectedBusinessException() throws BusinessException {
		var expectedRisk = getRisk();
		
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.postAnalysis(any(String.class), any(ClearSaleAnalyzeRequestDTO.class)))
				.thenThrow(new FeignException.BadRequest(FEIGN_ERROR_BAD_REQUEST_RESPONSE__MESSAGE, FEIGN_ERROR_BAD_REQUEST_RESPONSE_BODY__WHEN_JSON_SENDED_IS_EMPTY.getBytes()));
		when(this.clearSaleAnalyzeMapper.mapper(any(RiskDTO.class)))
				.thenReturn(ClearSaleAnalyzeRequestDTO.builder().build());
		
		assertThrows(BusinessException.class, () -> {	
			riskService.analyze(expectedRisk);
		});	
	}
	
	@Test
	void orderAnalize_whenClearSaleResponseIsVoid_thenMethodCannotConvertPayloadToObject_expectedBusinessException() throws BusinessException {
		var expectedRisk = getRisk();
		
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.postAnalysis(any(String.class), any(ClearSaleAnalyzeRequestDTO.class)))
				.thenThrow(new FeignException.InternalServerError("", "".getBytes()));
		when(this.clearSaleAnalyzeMapper.mapper(any(RiskDTO.class)))
				.thenReturn(ClearSaleAnalyzeRequestDTO.builder().build());
		
		assertThrows(BusinessException.class, () -> {
			riskService.analyze(expectedRisk);
		});	
	}
	
	@Test
	void statusChangeNotifier_expectedSuccess(){
		var statusChange = getStatusChange();
		this.riskService.statusChangeNotifier(statusChange);
		verify(riskQueuePublisher).publishQueue(statusChange);
	}
	
	@Test
	void statusChangeNotifier_InvalidArguments_expectedException() {
		doThrow(new RuntimeException()).when(this.riskQueuePublisher).publishQueue(any());
		assertThrows(Exception.class, () -> 
			this.riskService.statusChangeNotifier(null)
			);
	}

	@Test
	void processMessage_whenValid_expectedSuccess(){
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class))).thenReturn(createNotifyAnalysisResults().get(0));
		this.riskService.processMessage(getStatusChange());
		verify(this.clearSaleClient).getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class));
	}

	@Test
	void processMessageRecover_whenValid_expectedSuccess() {
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		doThrow(RuntimeException.class).when(this.clearSaleClient).getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class));
		this.riskService.processMessage(getStatusChange());
		verify(this.clearSaleClient, times(2)).getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class));
		verify(this.riskQueuePublisher, times(1)).publishQueueDlq(any());
	}
		
	@Test
	void publishNotification_whenArgumentsIsValid_expectedSuccess(){
		var status = getStatusChange();
		
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class))).thenReturn(createNotifyAnalysisResults().get(0));
		
		this.riskService.processMessage(status);
		verify(this.riskExchangePublisher).publishExchange(any(),any());
	}
	
	@Test
	void publishNotification_whenArgumentsIsInvalid_expectedException() {
		when(this.clearSaleClient.getToken(any(ClearSaleCredentialsDTO.class))).thenReturn(getToken());
		when(this.clearSaleClient.getStatusClearSaleOrder(any(String.class), any(String.class), any(String.class))).thenReturn(createNotifyAnalysisResults().get(0));
		doThrow(RuntimeException.class).when(this.riskExchangePublisher).publishExchange(any(), any());
		
		assertDoesNotThrow(()->
			this.riskService.processMessage(getStatusChange())
		);
		
		verify(this.riskExchangePublisher,times(2)).publishExchange(any(),any());
		verify(this.riskQueuePublisher).publishQueueDlq(any());
	}
	
	private StatusChangeDTO getStatusChange() {
		return StatusChangeDTO.builder()
				.code("1")
				.date(ZonedDateTime.now())
				.type("status")
				.build();
	}
	
	private ClearSaleAnalyzeResponseDTO getClearSaleResponse() {
		return ClearSaleAnalyzeResponseDTO.builder().packageID("1").orders(createClearSaleOrders()).build();
	}

	private RiskDTO getRisk() {
		return RiskDTO.builder()
					.origin("APP")
					.observation("obs")
					.customSla(40)
					.fingerprintClearSale("123")
					.order(createOrder())
					.customer(createCustomer())
					.shipping(createShipping())
					.paymentCards(createPaymentCards())
				.build();
	}

	private List<PaymentCardDTO> createPaymentCards() {
		return List.of(PaymentCardDTO.builder()
				.bin("123456")
				.end("9876")
				.ownerName("John Doe")
				.build());
	}

	private ShippingDTO createShipping() {
		return ShippingDTO.builder()
					.deliveryType(ShippingDeliveryTypeEnum.ECONOMICA)
					.address(createAddress())
				.build();
	}

	private OrderDTO createOrder() {
		return OrderDTO.builder()
					.id("1")
					.items(List.of(OrderItemDTO.builder().build()))
				.build();
	}

	private Optional<TokenDTO> getToken() {
		return Optional.of(new TokenDTO("1", "2022-06-26T12:00:00.0000000-03:00"));
	}

	private List<ClearSaleOrderDTO> createClearSaleOrders() {
		return List.of(ClearSaleOrderDTO.builder()
					.code("1")
					.status("NVO")
					.score(BigDecimal.ONE)
				.build());
	}

	private List<NotifyAnalysisResultDTO> createNotifyAnalysisResults() {
		return List.of(NotifyAnalysisResultDTO.builder()
					.code("1")
					.status(ClearsaleStatusResponseEnum.NVO)
					.score(BigDecimal.ONE)
				.build());
	}
	
	private AddressDTO createAddress() {
		return AddressDTO.builder()
					.street("Test St.")
					.number("1")
					.county("County name")
					.city("City name")
					.state(AddressStateEnum.AC)
					.zipCode("12345678")
				.build();
	}

	private CustomerDTO createCustomer() {
		return CustomerDTO.builder()
					.primaryDocument("12345678900")
					.name("John Doe")
					.billingAddress(createAddress())
					.phones(List.of(PhoneDTO.builder()
							.type(PhoneTypeEnum.COMERCIAL)
							.ddd(11)
							.number(912345678)
							.build()))
				.build();
	}

}
