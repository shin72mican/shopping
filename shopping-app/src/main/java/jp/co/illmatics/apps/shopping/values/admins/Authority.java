package jp.co.illmatics.apps.shopping.values.admins;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
	GENERAL(0, "GENERAL"),
	OWNER(1, "OWNER");
	
	private int value;
	private String viweValue;
}
