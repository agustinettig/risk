package br.com.sburble.risk.domain.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import br.com.sburble.risk.util.Constants;
import br.com.sburble.risk.util.ZonedDateTimeDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusChangeDTO implements Serializable {

	private static final long serialVersionUID = 1701003503973907029L;

	@ApiModelProperty(value = "Order code", example = "\"10000036495\"", required = true)
	@NotBlank
	@Size(min = 1, max = 50)
	private String code;
	
	@ApiModelProperty(value = "Order updated date", example = "2022-06-26T12:00:00.0000000-03:00", required = true)
	@NotNull
	@JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	@JsonSerialize(using = ZonedDateTimeSerializer.class)
	private ZonedDateTime date;
	
	@ApiModelProperty(value = "Order updated type", example = "AMA", required = true)
	@NotNull
	private String type;

}
