package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Users {
	
	public Users() {
		
	}
	
	public Users(Long id) {
		this.id = id;
	}
	
	public Users(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public Users(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public Users(Long id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	private Long id;

	private String name;

	private String email;

	private Timestamp emailVerifiedAt;

	private String password;

	private String imagePath;

	private Timestamp createAt;

	private Timestamp updateAt;

}