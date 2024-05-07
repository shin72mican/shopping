package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Products {
	
	public Products() {
		
	}
	
	public Products(Long id) {
		this.id = id;
	}
	
	public Products(Long categoryId, String name) {
		this.productCategoryId = categoryId;
		this.name = name;
	}
	
	public Products(Long categoryId, String name, String description) {
		this.productCategoryId = categoryId;
		this.name = name;
		this.description = description;
	}
	
	public Products(Long categoryId, String name, Long price, String description) {
		this.productCategoryId = categoryId;
		this.name = name;
		this.price = price;
		this.description = description;
	}
	
	public Products(Long id, Long categoryId, String name, Long price, String description) {
		this.id = id;
		this.productCategoryId = categoryId;
		this.name = name;
		this.price = price;
		this.description = description;
	}
	
	private Long id;
	
	private Long productCategoryId;
	
	private Long wishProductId;
	
	private Long wishUserId;
	
	private String categoryName;
	
	private String name;
	
	private Long price;
	
	private String description;
	
	private String imagePath;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
	
}
