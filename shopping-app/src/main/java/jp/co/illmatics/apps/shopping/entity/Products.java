package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Products {
	
	public Products() {
		
	}
	
	private Long id;
	
	private Long productCategoryId;
	
	private String categoryName;
	
	private String name;
	
	private Long price;
	
	private String description;
	
	private String imagePath;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
	
}
