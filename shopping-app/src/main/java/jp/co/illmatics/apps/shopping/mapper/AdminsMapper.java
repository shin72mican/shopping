package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Admins;

@Mapper
public interface AdminsMapper {
	
	@SelectProvider(AdminSqlProvider.class)
	List<Admins> find(Admins admins);
	
	@SelectProvider(AdminSqlProvider.class)
	Optional<Admins> findByName(String username);
	
	public class AdminSqlProvider implements ProviderMethodResolver {
		
		public String find(Admins admins) {
			return new SQL() {{
				SELECT("id", "name", "email", "email_verified_at", "password", "image_path", "create_at", "update_at");
				FROM("admin_users");
				if (Objects.nonNull(admins.getId())) {
					 WHERE("id = #{id}");
				}
				if (Objects.nonNull(admins.getName())) {
					 WHERE("name = #{name}");
				}
				if (Objects.nonNull(admins.getEmail())) {
					 WHERE("email = #{email}");
				}
				if (Objects.nonNull(admins.getPassword())) {
					 WHERE("password = #{password}");
				}
				if (Objects.nonNull(admins.getCreateAt())) {
					 WHERE("create_at = #{createAt}");
				}
				if (Objects.nonNull(admins.getUpdateAt())) {
					 WHERE("update_at = #{updateAt}");
				}
			}}.toString();
		}
		
		public String findByName(String name) {
			return new SQL() {{
				SELECT("id", "name", "email", "password", "create_at", "update_at");
				FROM("admin_users");
				WHERE("name = #{name}");
			}}.toString();
		}
	}
}
