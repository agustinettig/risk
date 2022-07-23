package br.com.sburble.risk.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import br.com.sburble.risk.domain.dto.TokenDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleCredentialsDTO;
import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleAnalyzeResponseDTO;
import br.com.sburble.risk.domain.enums.NotificationEventTypeEnum;
import br.com.sburble.risk.exception.ClearSaleUnauthorizedException;
import br.com.sburble.risk.http.ClearSaleClient;
import br.com.sburble.risk.mapper.ClearSaleAnalyzeMapper;
import br.com.sburble.risk.publisher.RiskExchangePublisher;
import br.com.sburble.risk.publisher.RiskQueuePublisher;
import br.com.sburble.risk.util.Constants;
import br.com.sburble.risk.util.ExceptionUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskService {

	private final ClearSaleClient clearSaleClient;

	private final ClearSaleAnalyzeMapper clearSaleAnalyzeMapper;

	private final RiskQueuePublisher riskQueuePublisher;

	private final RiskExchangePublisher riskExchangePublisher;
	
	@Value("${credentials.name}")
	private String name;

	@Value("${credentials.password}")
	private String password;

	public ClearSaleAnalyzeResponseDTO analyze(RiskDTO risk) {

		try {
			log.info("Order to be sent for risk analysis, order={}", risk);
			var clearSalePayload = this.clearSaleAnalyzeMapper.mapper(risk);
			log.info("Submitting a risk analysis request on ClearSale, payload={}", clearSalePayload);
			var response = this.clearSaleClient.postAnalysis(getTokenClearSale().getBearerToken(), clearSalePayload );
			log.info("Submission of the request for risk analysis successfull, response={}", response);
			return response;
		} catch (FeignException e) {
			log.error("Analysis request to ClearSale failed, cause={}, error={}", e.getMessage(), e.contentUTF8());
			throw ExceptionUtils.feignToBusinessException(e);
		}
	}
	
	@Retryable(value = Exception.class, maxAttemptsExpression = "${ms.queue.risk-status-retry-attemps}", backoff = @Backoff(delayExpression = "${ms.queue.risk-status-retry-delay}"))
	public void processMessage(StatusChangeDTO statusChange){
		log.info("Searching order status on ClearSale, code={}", statusChange.getCode());
		
		var token = getTokenClearSale();
		var response = this.clearSaleClient.getStatusClearSaleOrder(token.getBearerToken(), Constants.ACCEPT, statusChange.getCode());
		
		log.info("Search successfull, response={}", response);

		this.riskExchangePublisher.publishExchange(response, NotificationEventTypeEnum.CREATED);
	}

	public void statusChangeNotifier(StatusChangeDTO statusChange) {
		log.info("Status change notification received, sending to processing queue, statusChange={}", statusChange);
		this.riskQueuePublisher.publishQueue(statusChange);
	}
	
	private TokenDTO getTokenClearSale(){
		Optional<TokenDTO> token = this.clearSaleClient.getToken(ClearSaleCredentialsDTO.builder().name(name).password(password).build());
        return token.orElseThrow(ClearSaleUnauthorizedException::new);
	}
	
	@Recover
	private void processMessageRecover(Exception e, StatusChangeDTO statusChange) {
		log.info("Sending order status message to DLQ , cause={}, message={}, class={}",e.getCause(), e.getMessage(), e.getClass());
		this.riskQueuePublisher.publishQueueDlq(statusChange);
	}
}
