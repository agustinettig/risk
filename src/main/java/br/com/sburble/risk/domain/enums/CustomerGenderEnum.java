package br.com.sburble.risk.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerGenderEnum {

	M("M"), F("F");

	private String value;
}
