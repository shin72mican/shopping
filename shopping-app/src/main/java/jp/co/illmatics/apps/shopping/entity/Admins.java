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
	
	public Admins(String name, String email, String password, String authority) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.authority = authority;
	}
	
	public Admins(Long id, String name, String email, String password, String authority) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.authority = authority;
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
