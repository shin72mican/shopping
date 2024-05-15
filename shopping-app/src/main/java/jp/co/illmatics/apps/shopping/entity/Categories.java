package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Categories {
	
	public Categories() {
		
	}
	
	public Categories(Long id) {
		this.id = id;
	}
	
	public Categories(String name, Long orderNo) {
		this.name = name;
		this.orderNo = orderNo;
	}
	
	public Categories(Long id, String name, Long orderNo) {
		this.id = id;
		this.name = name;
		this.orderNo = orderNo;
	}
	
	private Long id;
	
	private String name;
	
	private Long orderNo;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
