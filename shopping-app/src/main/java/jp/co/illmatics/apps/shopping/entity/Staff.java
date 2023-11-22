package jp.co.illmatics.apps.shopping.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Staff {

	private long id;

	private String name;

	private String email;

	private Timestamp emailVerifiedAt;

	private String password;

	private String imagePpath;

	private Timestamp createAt;

	private Timestamp updateAt;

}