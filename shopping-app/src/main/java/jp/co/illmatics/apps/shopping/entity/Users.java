package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Users {

	private Long id;

	private String name;

	private String email;

	private Timestamp emailVerifiedAt;

	private String password;

	private String imagePath;

	private Timestamp createAt;

	private Timestamp updateAt;

}