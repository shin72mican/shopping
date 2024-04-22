package jp.co.illmatics.apps.shopping.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface ProductReviewsMapper {
	@DeleteProvider(ProductReviewSqlProvider.class)
	void usersDelete(Users user);
	
	public class ProductReviewSqlProvider implements ProviderMethodResolver {
		// 顧客関連のレビューを削除
		public String usersDelete(Users user) {
			return new SQL() {{
				DELETE_FROM("product_reviews");
				WHERE("user_id = #{id}");
			}}.toString();
		}
	}
}
