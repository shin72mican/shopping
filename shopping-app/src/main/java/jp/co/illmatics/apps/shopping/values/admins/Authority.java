package jp.co.illmatics.apps.shopping.values.admins;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
	GENERAL(0, "general"),
	OWNER(1, "owner");
	
	private int value;
	private String viweValue;
	
	// valueから逆引き
	public static Authority getByValue(int value) {
		return Arrays.stream(Authority.values())
				.filter(data -> data.getValue() == value)
				.findFirst()
				.orElse(null);
	}
	
	// viewValueから逆引き
	public static Authority getByViewValue(String viewValue) {
		return Arrays.stream(Authority.values())
				.filter(data -> data.getViweValue().equals(viewValue))
				.findFirst()
				.orElse(null);
	}
}
