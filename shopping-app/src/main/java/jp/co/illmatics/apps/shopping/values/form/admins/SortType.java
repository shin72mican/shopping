package jp.co.illmatics.apps.shopping.values.form.admins;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
	ID("id", "並び替え:ID"),
	NAME("name", "並び替え:名称"),
	EMAIL("email", "並び替え:メールアドレス");
	
	private String value;
	private String viewType;
}
