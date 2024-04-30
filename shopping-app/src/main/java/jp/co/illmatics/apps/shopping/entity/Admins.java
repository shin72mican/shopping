package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Admins {
	
	public Admins() {
		
	}
	
	public Admins(Long id) {
		this.id = id;
	}
	
	public Admins(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public Admins(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	private Long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private int isOwner;
	
	private String authority;
	
	private Timestamp createAt;
	
	private Timestamp updateAt;
}
