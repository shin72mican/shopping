package jp.co.illmatics.apps.shopping.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishProducts {
	
	public WishProducts() {
		
	}
	
	public WishProducts(Long productId, Long userId) {
		this.productId = productId;
		this.userId = userId;
	}
	
	private Long id;
	
	private Long productId;
	
	private Long userId;
	
}
