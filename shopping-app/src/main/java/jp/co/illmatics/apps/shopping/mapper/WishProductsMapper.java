package jp.co.illmatics.apps.shopping.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface WishProductsMapper {
	@DeleteProvider(WishProductSqlProvider.class)
	void usersDelete(Users user);
	
	@DeleteProvider(WishProductSqlProvider.class)
	void productsDelete(Products product);
	
	public class WishProductSqlProvider implements ProviderMethodResolver {
		// 削除顧客関連のレビュー削除
		public String usersDelete(Users user) {
			return new SQL() {{
				DELETE_FROM("wish_products");
				WHERE("user_id = #{id}");
			}}.toString();
		}
		
		// 削除商品関連のレビューを削除
		public String productsDelete(Products product) {
			return new SQL() {{
				DELETE_FROM("wish_products");
				WHERE("product_id = #{id}");
			}}.toString();
		}
	}
}
