package br.com.sburble.risk.domain.dto.clearsale;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClearSaleCredentialsDTO implements Serializable {

	private static final long serialVersionUID = -1637764678831535909L;

	private String name;
	private String password;

}
