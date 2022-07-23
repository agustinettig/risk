package br.com.sburble.risk.domain.dto.clearsale.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

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
public class ClearSaleModelState {

	@Builder.Default
	private Map<String, Object> details = new LinkedHashMap<>();

	@JsonAnySetter
	void setDetail(String key, List<Object> value) {
		details.put(key, value);
	}
}
