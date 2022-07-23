package br.com.sburble.risk.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskQueuePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${ms.queue.risk-status}")
    private String riskQueue;

    @Value("${ms.queue.risk-status-dlq}")
    private String riskQueueDlq;

    public void publishQueue(StatusChangeDTO statusChange) {
        try {
            this.rabbitTemplate.convertAndSend(this.riskQueue, statusChange);
            log.info("Message sent to queue successfully, message={}", statusChange);
        } catch (Exception e) {
            log.error("Error sending message to queue, message={}, error={}", statusChange, e.getMessage());
        }
    }
    
    public void publishQueueDlq(StatusChangeDTO statusChange) {
    	try {
    		this.rabbitTemplate.convertAndSend(this.riskQueueDlq, statusChange);
    		log.info("Message sent to DLQ successfully, message={}", statusChange);
    	} catch (Exception e) {
    		log.error("Error sending message to DLQ no rabbit, message={}, error={}", statusChange, e.getMessage());
    	}
    }
    
}
