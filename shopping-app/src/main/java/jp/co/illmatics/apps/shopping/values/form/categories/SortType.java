package jp.co.illmatics.apps.shopping.values.form.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
	ID("id", "並び替え:ID"),
	NAME("name", "並び替え:名称"),
	ORDER_NO("order_no", "並び替え:並び順番号");
	
	private String value;
	private String viewType;
}
