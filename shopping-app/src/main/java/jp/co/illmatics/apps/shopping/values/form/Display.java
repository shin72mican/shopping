package jp.co.illmatics.apps.shopping.values.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Display {
	FIRST(10, "表示:10件"),
	SECOND(20, "表示:20件"),
	THIRD(50, "表示:50件"),
	FOURTH(100, "表示:100件");
	
	private final int value;
	private final String viewCount;
	
}
