package br.com.sburble.risk.mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sburble.risk.domain.dto.AddressDTO;
import br.com.sburble.risk.domain.dto.CustomerDTO;
import br.com.sburble.risk.domain.dto.OrderDTO;
import br.com.sburble.risk.domain.dto.OrderItemDTO;
import br.com.sburble.risk.domain.dto.PaymentCardDTO;
import br.com.sburble.risk.domain.dto.PhoneDTO;
import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.ShippingDTO;
import br.com.sburble.risk.domain.enums.AddressStateEnum;
import br.com.sburble.risk.domain.enums.CustomerGenderEnum;
import br.com.sburble.risk.domain.enums.PhoneTypeEnum;
import br.com.sburble.risk.domain.enums.ShippingDeliveryTypeEnum;

@ExtendWith(MockitoExtension.class)
class ClearSaleAnalyzeMapperTest {
	
	@InjectMocks
	private ClearSaleAnalyzeMapper clearSaleAnalyzeMapper;
	
	@Test
	void clearSaleMapper_whenAllDataValid_expectedSucess() throws Exception {
		var expectedResult = this.clearSaleAnalyzeMapper.mapper(createRiskAllData());
		assertNotNull(expectedResult);
		assertNotNull(expectedResult.getCode());
		assertNotNull(expectedResult.getSessionID());
		assertNotNull(expectedResult.getDate());
		assertNotNull(expectedResult.getEmail());
		assertNotNull(expectedResult.getTotalValue());
		assertNotNull(expectedResult.getObservation());
		assertNotNull(expectedResult.getOrigin());
		assertNotNull(expectedResult.getCustomSla());
		
		assertNotNull(expectedResult.getBilling().getName());
		assertNotNull(expectedResult.getShipping().getEmail());
		assertTrue(expectedResult.getItems().size() > 0);
		assertTrue(expectedResult.getPayments().size() > 0);
		
	}
	
	@Test
	void clearSaleMapper_whenAllMandatoryDataValid_expectedSucess() throws Exception {
		var expectedResult = this.clearSaleAnalyzeMapper.mapper(createRiskMandatoryData());
		assertNotNull(expectedResult);
		assertNotNull(expectedResult.getCode());
		assertNotNull(expectedResult.getItems());
		assertNotNull(expectedResult.getEmail());
		
		assertNotNull(expectedResult.getBilling());
		assertNotNull(expectedResult.getBilling().getEmail());
		assertNotNull(expectedResult.getBilling().getType());
		assertNotNull(expectedResult.getBilling().getPrimaryDocument());
		assertNotNull(expectedResult.getBilling().getName());
		assertNotNull(expectedResult.getBilling().getPhones());

		assertNotNull(expectedResult.getShipping());
		assertNotNull(expectedResult.getShipping().getType());
		assertNotNull(expectedResult.getShipping().getPrimaryDocument());
		assertNotNull(expectedResult.getShipping().getName());
		assertNotNull(expectedResult.getShipping().getPhones());

	}
	
	@Test
	void clearSaleMapper_whenShippingEmpty_expectedSucess() throws Exception {
		var expectedResult = this.clearSaleAnalyzeMapper.mapper(createRiskValidButShippingEmpty());
		assertNull(expectedResult.getShipping().getDeliveryType());
		assertNull(expectedResult.getShipping().getPrice());
		assertNull(expectedResult.getShipping().getAddress());
		
	}

	private RiskDTO createRiskAllData() {
		return RiskDTO.builder()
		.origin("APP")
		.observation("obs")
		.customSla(40)
		.fingerprintClearSale("123456")
		.order(createValidDataOrder())
		.customer(createCustomerValid())
		.shipping(createShippingValid())
		.paymentCards(createPaymentCardsValid())
		.build();
	}
	
	private RiskDTO createRiskMandatoryData() {
		return RiskDTO.builder()
				.order(OrderDTO.builder().id("1").items(createOrderItemValid()).build())
				.customer(CustomerDTO.builder()
						.name("John Doe")
						.email("johndoe@mail.com")
						.primaryDocument("12345678900000")
						.phones(createPhonesValid()).build())
				.paymentCards(createPaymentCardsValid())
				.build();
	}
	
	private RiskDTO createRiskValidButShippingEmpty() {
		return RiskDTO.builder()
				.order(createValidDataOrder())
				.customer(createCustomerValid())
				.paymentCards(createPaymentCardsValid())
				.shipping(ShippingDTO.builder().build())
				.build();
	}

	private OrderDTO createValidDataOrder() {
		return OrderDTO.builder()
				.id("1")
				.date(LocalDateTime.now())
				.totalValue(BigDecimal.TEN)
				.items(createOrderItemValid())
				.build();
	}

	private CustomerDTO createCustomerValid() {
		return CustomerDTO.builder()
				.primaryDocument("12345678900")
				.name("John Doe")
				.email("johndoe@mail.com")
				.birthDate(LocalDate.now())
				.gender(CustomerGenderEnum.M)
				.billingAddress(createAddressValid())
				.phones(createPhonesValid())
				.build();
	}

	private ShippingDTO createShippingValid() {
		return ShippingDTO.builder()
				.deliveryType(ShippingDeliveryTypeEnum.ECONOMICA)
				.price(BigDecimal.TEN)
				.address(createAddressValid())
				.build();
	}

	private List<PaymentCardDTO> createPaymentCardsValid() {
		return List.of(PaymentCardDTO.builder()
				.bin("123456")
				.end("9876")
				.ownerName("John Doe")
				.build());
	}

	private List<OrderItemDTO> createOrderItemValid() {
		return List.of(OrderItemDTO.builder()
				.name("candy")
				.unitPrice(BigDecimal.TEN)
				.amount(1)
				.build());
	}

	private AddressDTO createAddressValid() {
		return AddressDTO.builder()
				.street("Test St.")
				.number("1")
				.additionalInformation("add")
				.county("County name")
				.city("City name")
				.state(AddressStateEnum.AC)
				.zipCode("12345678")
				.reference("ref")
				.build();
	}

	private List<PhoneDTO> createPhonesValid() {
		return List.of(PhoneDTO.builder()
				.type(PhoneTypeEnum.COMERCIAL)
				.ddd(11)
				.number(12345678)
				.extension("123")
				.build());
	}
	
}
