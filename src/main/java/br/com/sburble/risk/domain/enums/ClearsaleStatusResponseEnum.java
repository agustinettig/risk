package br.com.sburble.risk.domain.enums;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClearsaleStatusResponseEnum {

	APA("APA","Aprovação Automática","Pedido foi aprovado automaticamente segundo parâmetros definidos na regra de aprovação automática"), 
	APM("APM","Aprovação Manual","Pedido aprovado manualmente por tomada de decisão de um analista"), 
	APP("APP","Aprovação Por Política","Pedido aprovado automaticamente por política estabelecida pelo cliente ou Clearsale"), 
	
	AMA("AMA","Análise manual","Pedido está em fila para análise"), 
	NVO("NVO","Novo","Pedido importado e não classificado Score pela analisadora (processo que roda o Score de cada pedido)"),
	
	RPM("RPM","Reprovado Sem Suspeita","Pedido Reprovado sem Suspeita por falta de contato com o cliente dentro do período acordado e/ou políticas restritivas de CPF (Irregular, SUS ou Cancelados)"), 
	SUS("SUS","Suspensão Manual","Pedido Suspenso por suspeita de fraude baseado no contato com o “cliente” ou ainda na base ClearSale"), 
	CAN("CAN","Cancelado pelo Cliente","Cancelado por solicitação do cliente ou duplicidade do pedido"), 
	FRD("FRD","Fraude Confirmada","Pedido imputado como Fraude Confirmada por contato com a administradora de cartão e/ou contato com titular do cartão ou CPF do cadastro que desconhecem a compra"), 
	RPA("RPA","Reprovação Automática","Pedido Reprovado Automaticamente por algum tipo de Regra de Negócio que necessite aplicá-la"), 
	RPP("RPP","Reprovação Por Política","Pedido reprovado automaticamente por política estabelecida pelo cliente ou Clearsale");
	
	private String value;
	private String shortDescription;
	private String longDescription;
	
	public static List<String> statusApprovedList(){
		return Arrays.asList(APA.getValue(), APM.getValue(), APP.getValue());
	}
	
	public static List<String> statusInAnalyzeList(){
		return Arrays.asList(AMA.getValue(), NVO.getValue());
	}
	
	public static List<String> statusRejectedList(){
		return Arrays.asList(RPM.getValue(), SUS.getValue(), CAN.getValue(), FRD.getValue(), RPA.getValue(), RPP.getValue());
	}
	
	public ClearsaleStatusResponseSimplifiedEnum getStatusSimplified() {
		if(statusApprovedList().contains(this.value)) {
			return ClearsaleStatusResponseSimplifiedEnum.APPROVED;
		}
		
		if(statusInAnalyzeList().contains(this.value)) {
			return ClearsaleStatusResponseSimplifiedEnum.ANALYSING;
		}
		
		return ClearsaleStatusResponseSimplifiedEnum.REPROVED;
	}
}
