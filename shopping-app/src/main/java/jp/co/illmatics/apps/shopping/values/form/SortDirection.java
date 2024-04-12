package jp.co.illmatics.apps.shopping.values.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortDirection {
	ASC("asc", "並び替え方向:昇順"),
	DESC("desc", "並び替え方向:降順");
	
	private String value;
	private String viewDirection;
}
