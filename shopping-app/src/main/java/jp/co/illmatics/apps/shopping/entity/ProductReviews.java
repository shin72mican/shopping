package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductReviews {
	public ProductReviews() {
		
	}
	
	public ProductReviews(Long id, Long productId) {
		this.id = id;
		this.productId = productId;
	}
	
	public ProductReviews(Long productId) {
		this.productId = productId;
	}
	
	public ProductReviews(Long productId, Long userId, String title, String body, int rank) {
		this.productId = productId;
		this.userId = userId;
		this.title = title;
		this.body = body;
		this.rank = rank;
	}
	
	public ProductReviews(Long id, Long productId, Long userId, String title, String body, int rank) {
		this.id = id;
		this.productId = productId;
		this.userId = userId;
		this.title = title;
		this.body = body;
		this.rank = rank;
	}
	
	private Long id;
	
	private String userName;
	
	private String userImagePath;
	
	private Long productId;
	
	private Long userId;
	
	private String title;
	
	private String body;
	
	private int rank;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
