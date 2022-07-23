package br.com.sburble.risk.publisher;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.sburble.risk.domain.dto.notification.NotifyAnalysisResultDTO;
import br.com.sburble.risk.domain.enums.ClearsaleStatusResponseEnum;
import br.com.sburble.risk.domain.enums.ClearsaleStatusResponseSimplifiedEnum;
import br.com.sburble.risk.domain.enums.NotificationEventTypeEnum;

@ExtendWith(MockitoExtension.class)
class RiskExchangePublisherTest {

	private static final String EXCHANGE_NAME = "exchange-name";

	@InjectMocks
	private RiskExchangePublisher riskExchangePublisher;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.setField(riskExchangePublisher, "riskExchange", EXCHANGE_NAME);
	}
	
	@Test
	void publishExchange_whenValid_expectSuccess() {
		var notification = getStatusChange();
		this.riskExchangePublisher.publishExchange(notification, NotificationEventTypeEnum.CREATED);
		verify(rabbitTemplate).convertAndSend(EXCHANGE_NAME, NotificationEventTypeEnum.CREATED.toString(), notification);		
	}
	
	@Test
	void publishExchange_whenInvalid_expectErrorToBeCaught() {
		doThrow(new RuntimeException()).when(rabbitTemplate).convertAndSend(any(), any(Object.class));
		assertDoesNotThrow(() ->{
			this.riskExchangePublisher.publishExchange(getStatusChange(), NotificationEventTypeEnum.CREATED);
		});
	}
	
	private NotifyAnalysisResultDTO getStatusChange() {
		return NotifyAnalysisResultDTO.builder()
				.code("123")
				.score(BigDecimal.TEN)
				.status(ClearsaleStatusResponseEnum.AMA)
				.statusSimplified(ClearsaleStatusResponseSimplifiedEnum.APPROVED)
				.build();
	}

}
