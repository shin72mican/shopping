package jp.co.illmatics.apps.shopping.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.entity.WishProducts;

@Mapper
public interface WishProductsMapper {
	
	@InsertProvider(WishProductSqlProvider.class)
	void insert(WishProducts wishProduct);
	
	@DeleteProvider(WishProductSqlProvider.class)
	void delete(WishProducts wishProduct);
	
	@DeleteProvider(WishProductSqlProvider.class)
	void usersDelete(Users user);
	
	@DeleteProvider(WishProductSqlProvider.class)
	void productsDelete(Products product);
	
	public class WishProductSqlProvider implements ProviderMethodResolver {
		// お気に入り登録
		public String insert(WishProducts wishProduct) {
			return new SQL() {{
				INSERT_INTO("wish_products");
				VALUES("product_id", "#{productId}");
				VALUES("user_id", "#{userId}");
			}}.toString();
		}
		
		// お気に入り解除
		public String delete(WishProducts wishProduct) {
			return new SQL() {{
				DELETE_FROM("wish_products");
				WHERE("product_id = #{productId}");
				WHERE("user_id = #{userId}");
			}}.toString();
		}
		
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
