package br.com.sburble.risk.http;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.sburble.risk.domain.dto.TokenDTO;
import br.com.sburble.risk.domain.dto.clearsale.ClearSaleCredentialsDTO;
import br.com.sburble.risk.domain.dto.clearsale.request.ClearSaleAnalyzeRequestDTO;
import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleAnalyzeResponseDTO;
import br.com.sburble.risk.domain.dto.notification.NotifyAnalysisResultDTO;
import feign.Headers;

@FeignClient(url = "${clearSale.endPoint}", name = "clearSaleClient")
public interface ClearSaleClient {

    @PostMapping("${clearSale.token.endPoint}")
    Optional<TokenDTO> getToken(@RequestBody ClearSaleCredentialsDTO clearSaleCredentials);

    @PostMapping("${analize.endPoint}")
    @Headers("Content-Type: application/json")
    ClearSaleAnalyzeResponseDTO postAnalysis(@RequestHeader("Authorization") String basicAuth, @RequestBody ClearSaleAnalyzeRequestDTO clearSaleAnalyzeRequest);

    @GetMapping("${status.endPoint}")
    NotifyAnalysisResultDTO getStatusClearSaleOrder(@RequestHeader("Authorization") String basicAuth, @RequestHeader("Accept") String accept, @PathVariable String code);

}
