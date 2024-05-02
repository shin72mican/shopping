package jp.co.illmatics.apps.shopping.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface WishProductsMapper {
	@DeleteProvider(WishProductSqlProvider.class)
	void usersDelete(Users user);
	
	public class WishProductSqlProvider implements ProviderMethodResolver {
		
		public String usersDelete(Users user) {
			return new SQL() {{
				DELETE_FROM("wish_products");
				WHERE("user_id = #{id}");
			}}.toString();
		}
	}
}
