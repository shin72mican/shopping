package jp.co.illmatics.apps.shopping.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface ProductReviewsMapper {
	@DeleteProvider(ProductReviewSqlProvider.class)
	void usersDelete(Users user);
	
	@DeleteProvider(ProductReviewSqlProvider.class)
	void productsDelete(Products product);
	
	public class ProductReviewSqlProvider implements ProviderMethodResolver {
		// 顧客関連のレビューを削除
		public String usersDelete(Users user) {
			return new SQL() {{
				DELETE_FROM("product_reviews");
				WHERE("user_id = #{id}");
			}}.toString();
		}
		
		// 削除商品関連のレビューを削除
		public String productsDelete(Products product) {
			return new SQL() {{
				DELETE_FROM("product_reviews");
				WHERE("product_id = #{id}");
			}}.toString();
		}
	}
}
