package br.com.sburble.risk.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import br.com.sburble.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiskQueueConsumer {

    private final RiskService riskService;

    @RabbitListener(queues = "${ms.queue.risk-status}")
    public void receiveStatusChangeMessage(StatusChangeDTO statusChange){
    	log.info("Consuming message from status processing queue, message={}", statusChange);
        this.riskService.processMessage(statusChange);
    }

}
