package jp.co.illmatics.apps.shopping.values.form.products;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
	ID("id", "並び替え:ID"),
	CATEGORY_NAME("category_name", "並び替え:商品カテゴリー"),
	NAME("name", "並び替え:名称"),
	PRICE("price", "並び替え:価格");
	
	private String value;
	private String viewType;
}
