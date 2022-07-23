package br.com.sburble.risk.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import br.com.sburble.risk.service.RiskService;

@ExtendWith(MockitoExtension.class)
public class RiskQueueConsumerTest {

    @InjectMocks
    private RiskQueueConsumer riskQueueConsumer;

    @Mock
    private RiskService riskService;

    @Test
    void publishQueue_whenValid_expectedSuccess() {
        doNothing().when(this.riskService).processMessage(any(StatusChangeDTO.class));
        this.riskQueueConsumer.receiveStatusChangeMessage(getStatusChange());
        verify(riskService).processMessage(any(StatusChangeDTO.class));
    }

    private StatusChangeDTO getStatusChange() {
        return StatusChangeDTO.builder()
                .code("123")
                .date(ZonedDateTime.now())
                .type("status")
                .build();
    }

}
