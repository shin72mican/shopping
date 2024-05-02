package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishProducts {
	
	public WishProducts() {
		
	}
	
	private Long id;
	
	private Long productId;
	
	private Long userId;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
