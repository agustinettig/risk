package br.com.sburble.risk.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sburble.risk.configuration.SwaggerConfig;
import br.com.sburble.risk.domain.dto.RiskDTO;
import br.com.sburble.risk.domain.dto.StatusChangeDTO;
import br.com.sburble.risk.domain.dto.clearsale.response.ClearSaleAnalyzeResponseDTO;
import br.com.sburble.risk.service.RiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/risks")
@AllArgsConstructor
@Validated
@Api(tags = SwaggerConfig.RISK)
public class RiskController {

	private final RiskService riskService;

	@ApiOperation(value = "Analyze order risk", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 202, message = "Successful risk analysis request", response = ClearSaleAnalyzeResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad request for risk analysis"),
			@ApiResponse(code = 401, message = "Missing authorization"),
			@ApiResponse(code = 403, message = "User not authorized to request risk analysis"),
			@ApiResponse(code = 404, message = "Data not found with the parameters"),
			@ApiResponse(code = 405, message = "Method not allowed"),
			@ApiResponse(code = 500, message = "System unavailable") })
	@PostMapping("/analysis")
	public ResponseEntity<ClearSaleAnalyzeResponseDTO> riskAnalysis(@RequestBody @Valid RiskDTO risk) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.riskService.analyze(risk));
	}
	
	@ApiOperation(value = "Status change notification", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful status change notification request"),
			@ApiResponse(code = 400, message = "Bad request for status change notification"),
			@ApiResponse(code = 401, message = "Missing authorization"),
			@ApiResponse(code = 403, message = "User not authorized to send status change notification"),
			@ApiResponse(code = 404, message = "Data not found with the parameters"),
			@ApiResponse(code = 405, message = "Method not allowed"),
			@ApiResponse(code = 500, message = "System unavailable") })
	@PostMapping("/status")
	public void statusChangeNotifier(@RequestBody @Valid StatusChangeDTO statusChange) {
		this.riskService.statusChangeNotifier(statusChange);
	}
	
}
