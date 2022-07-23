package br.com.sburble.risk.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhoneTypeEnum {
	NAO_DEFINIDO(0, "Não definido"), RESIDENCIAL(1, "Residencial"), COMERCIAL(2, "Comercial"), RECADOS(3, "Recados"),
	COBRANCA(4, "Cobrança"), TEMPORARIO(5, "Temporário"), CELULAR(6, "Celular");

	private Integer value;
	private String description;
}
