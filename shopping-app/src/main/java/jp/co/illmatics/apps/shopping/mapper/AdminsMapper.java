package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Admins;

@Mapper
public interface AdminsMapper {
	@SelectProvider(AdminSqlProvider.class)
	List<Admins> findEmail(Admins admin);
	
	public class AdminSqlProvider implements ProviderMethodResolver {
		// メールアドレス検索
		public String findEmail(Admins admin) {
			return new SQL() {{
				SELECT("id", "name", "email", "password", "is_owner", "create_at", "update_at");
				FROM("admin_users");
				WHERE("email = #{email}");
			}}.toString();
		}
	}
}
