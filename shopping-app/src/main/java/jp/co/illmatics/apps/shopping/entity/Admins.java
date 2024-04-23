package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Admins {
	
	public Admins(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	private Long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private Long isOwner;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
