package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClearSaleCardDTO implements Serializable {
	private static final long serialVersionUID = -775952197733896863L;

	private String bin;
	private String end;
	private String ownerName;
}