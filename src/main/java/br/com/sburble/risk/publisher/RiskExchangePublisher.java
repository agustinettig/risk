package br.com.sburble.risk.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.sburble.risk.domain.dto.notification.NotifyAnalysisResultDTO;
import br.com.sburble.risk.domain.enums.NotificationEventTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskExchangePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${ms.queue.risk-status}")
    private String riskExchange;

    public void publishExchange(NotifyAnalysisResultDTO resultAnalisys, NotificationEventTypeEnum typeEnum) {
        try {
            this.rabbitTemplate.convertAndSend(this.riskExchange, typeEnum.toString(), resultAnalisys);
            log.info("Message sent to exchange successfully, eventType={}, message={}", typeEnum, resultAnalisys);
        } catch (Exception e) {
            log.error("Error sending message to exchange, eventType={}, message={}, error={}", typeEnum, resultAnalisys, e.getMessage());
        }
    }    
}
