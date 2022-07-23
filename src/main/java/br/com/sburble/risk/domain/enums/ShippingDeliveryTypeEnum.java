package br.com.sburble.risk.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShippingDeliveryTypeEnum {
	OUTROS("0","Outros"),
	NORMAL("1","Normal"),
	GARANTIDA("2","Garantida"),
	EXPRESSA_BR("3","ExpressaBR"),
	EXPRESSA_SP("4","ExpressaSP"),
	ALTA("5","Alta"),
	ECONOMICA("6","Econômica"),
	AGENDADA("7","Agendada"),
	EXTRA_RAPIDA("8","Extra Rápida"),
	IMPRESSO("9","Impresso"),
	APLICATIVO("10","Aplicativo"),
	CORREIO("11","Correio"),
	MOTOBOY("12","Motoboy"),
	RETIRADA_BILHETERIA("13","Retirada Bilheteria"),
	RETIRADA_LOJA_PARCEIRA("14","Retirada Loja Parceira"),
	CARTAO_CREDITO_INGRESSO("15","Cartão de Crédito Ingresso"),
	RETIRADA_LOJA("16","Retirada Loja"),
	RETIRADA_LOCKERS("17","Retirada via Lockers"),
	RETIRADA_CORREIOS("18","Retirada em Agencia dos Correios"),
	ENTREGA_GARANTIDA_MESMO_DIA("19","Entrega Garantida no mesmo dia da compra"),
	ENTREGA_GARANTIDA_DIA_SEGUINTE("20","Entrega Garantida no dia seguinte da compra"),
	EXPRESSO("21","Retirada em loja - Expresso");
	
	private String code;
	private String description;
}
