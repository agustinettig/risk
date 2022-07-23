package br.com.sburble.risk.mapper;

import static java.util.Objects.isNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.sburble.risk.domain.dto.AddressDTO;
import br.com.sburble.risk.domain.dto.CustomerDTO;
import br.com.sburble.risk.domain.dto.OrderItemDTO;
import br.com.sburble.risk.domain.dto.PaymentCardDTO;
import br.com.sburble.risk.domain.dto.PhoneDTO;
import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.ShippingDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleAddressDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleBillingDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleCardDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleItemDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSalePaymentDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSalePhoneDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleShippingDTO;
import br.com.sburble.risk.domain.dto.clearsale.request.ClearSaleAnalyzeRequestDTO;
import br.com.sburble.risk.util.Constants;

@Component
public class ClearSaleAnalyzeMapper {

	public ClearSaleAnalyzeRequestDTO mapper(RiskDTO risk) {
		return ClearSaleAnalyzeRequestDTO.builder()
				.code(risk.getOrder().getId())
				.sessionID(risk.getFingerprintClearSale())
				.date(risk.getOrder().getDate())
				.email(risk.getCustomer().getEmail())
				.totalValue(risk.getOrder().getTotalValue())
				.observation(risk.getObservation())
				.origin(risk.getOrigin())
				.customSla(risk.getCustomSla())
				.billing(createClearSaleBilling(risk.getCustomer()))
				.shipping(createClearSaleShipping(risk.getCustomer(), risk.getShipping()))
				.items(createClearSaleItem(risk.getOrder().getItems()))
				.payments(createClearSalePayment(risk.getPaymentCards()))
				.build();
	}

	private ClearSaleBillingDTO createClearSaleBilling(CustomerDTO customer) {
		return ClearSaleBillingDTO.builder()
				.type(getCustomerType(customer.getPrimaryDocument()))
				.primaryDocument(customer.getPrimaryDocument())
				.name(customer.getName())
				.birthDate(customer.getBirthDate())
				.email(customer.getEmail())
				.gender(customer.getGender())
				.address(createClearSaleAddress(customer.getBillingAddress()))
				.phones(createClearSalePhones(customer.getPhones()))
				.build();
	}

	private ClearSaleShippingDTO createClearSaleShipping(CustomerDTO customer, ShippingDTO shipping) {
		return ClearSaleShippingDTO.builder()
				.type(getCustomerType(customer.getPrimaryDocument()))
				.primaryDocument(customer.getPrimaryDocument())
				.name(customer.getName())
				.birthDate(customer.getBirthDate())
				.email(customer.getEmail())
				.gender(customer.getGender())
				.phones(createClearSalePhones(customer.getPhones()))
				.deliveryType(getDeliveryType(shipping))
				.price(getPrice(shipping))
				.address(createClearSaleAddress(getAddress(shipping)))
				.build();
	}

	private ClearSaleAddressDTO createClearSaleAddress(AddressDTO address) {
		
		if (isNull(address)){
			return null;
		}
		
		return ClearSaleAddressDTO.builder()
				.street(address.getStreet())
				.number(address.getNumber())
				.additionalInformation(address.getAdditionalInformation())
				.county(address.getCounty())
				.city(address.getCity())
				.state(address.getCity())
				.zipcode(address.getZipCode())
				.reference(address.getReference())
				.build();
	}

	private List<ClearSalePhoneDTO> createClearSalePhones(List<PhoneDTO> phones) {
		return phones.stream()
				.map(phone -> ClearSalePhoneDTO.builder()
						.type(phone.getType().getValue())
						.ddd(phone.getDdd())
						.number(phone.getNumber())
						.extension(phone.getExtension())
						.build())
				.collect(Collectors.toList());
	}

	private List<ClearSaleItemDTO> createClearSaleItem(List<OrderItemDTO> items) {
		return items.stream()
				.map(item -> ClearSaleItemDTO.builder()
						.name(item.getName())
						.value(item.getUnitPrice())
						.amount(item.getAmount()).build())
				.collect(Collectors.toList());
	}

	private List<ClearSalePaymentDTO> createClearSalePayment(List<PaymentCardDTO> paymentCards) {
		return paymentCards.stream()
				.map(payment -> ClearSalePaymentDTO.builder()
						.type(Constants.CARD_TYPE)
						.card(createClearSaleCard(payment))
						.build())
				.collect(Collectors.toList());
	}

	private ClearSaleCardDTO createClearSaleCard(PaymentCardDTO payment) {
		return ClearSaleCardDTO.builder()
				.bin(payment.getBin())
				.end(payment.getEnd())
				.ownerName(payment.getOwnerName())
				.build();
	}

	private Integer getCustomerType(String primaryDocument) {
		return primaryDocument.length() > 11 ? Constants.LEGAL_PERSON : Constants.PHYSICAL_PERSON;
	}
	
	private String getDeliveryType(ShippingDTO shipping) {
		if (isNull(shipping)) {
			return null;
		}
		return shipping.getDeliveryType() != null ? shipping.getDeliveryType().getCode() : null;
	}

	private BigDecimal getPrice(ShippingDTO shipping) {
		if (isNull(shipping)) {
			return null;
		}
		return shipping.getPrice() != null ? shipping.getPrice() : null;
	}

	private AddressDTO getAddress(ShippingDTO shipping) {
		if (isNull(shipping)) {
			return null;
		}
		return shipping.getAddress() != null ? shipping.getAddress() : null;
	}
	
}
