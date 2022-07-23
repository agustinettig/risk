package br.com.sburble.risk.domain.dto.clearsale.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public class ClearSaleDefaultErrorMessageTemplate {

	@JsonProperty("Message")
	private String message;

	@JsonProperty("ModelState")
	private ClearSaleModelState modelState;
}