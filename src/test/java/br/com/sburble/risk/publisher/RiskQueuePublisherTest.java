package br.com.sburble.risk.publisher;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.sburble.risk.domain.dto.StatusChangeDTO;

@ExtendWith(MockitoExtension.class)
class RiskQueuePublisherTest {

	private static final String QUEUE_NAME = "queue-name";
	private static final String DLQ_NAME = "dlq-name";

	@InjectMocks
	private RiskQueuePublisher riskQueuePublisher;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@BeforeEach
	void beforeEach() {
		ReflectionTestUtils.setField(riskQueuePublisher, "riskQueue", QUEUE_NAME);
		ReflectionTestUtils.setField(riskQueuePublisher, "riskQueueDlq", DLQ_NAME);
	}
	
	@Test
	void publishQueue_whenValid_expectSuccess() {
		var statusChange = getStatusChange();
		this.riskQueuePublisher.publishQueue(statusChange);
		verify(rabbitTemplate).convertAndSend(QUEUE_NAME, statusChange);		
	}
	
	@Test
	void publishQueue_whenInvalid_expectErrorToBeCaught() {
		doThrow(new RuntimeException()).when(rabbitTemplate).convertAndSend(any(), any(Object.class));
		assertDoesNotThrow(() ->{
			this.riskQueuePublisher.publishQueue(getStatusChange());
		});
	}

	@Test
	void publishQueueDLQ_whenValid_expectSuccess() {
		var statusChange = getStatusChange();
		this.riskQueuePublisher.publishQueueDlq(statusChange);
		verify(rabbitTemplate).convertAndSend(DLQ_NAME, statusChange);
	}
	
	@Test
	void publishQueueDLQ_whenInvalid_expectErrorToBeCaught() {
		doThrow(new RuntimeException()).when(rabbitTemplate).convertAndSend(any(), any(Object.class));
		assertDoesNotThrow(() ->{
			this.riskQueuePublisher.publishQueueDlq(getStatusChange());
		});
	}
	
	private StatusChangeDTO getStatusChange() {
		return StatusChangeDTO.builder()
				.code("123")
				.date(ZonedDateTime.now())
				.type("status")
				.build();
	}

}
