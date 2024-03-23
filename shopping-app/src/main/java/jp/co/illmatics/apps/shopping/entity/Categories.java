package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Categories {
	
	private Long id;
	
	private String name;
	
	private Long orderNo;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
