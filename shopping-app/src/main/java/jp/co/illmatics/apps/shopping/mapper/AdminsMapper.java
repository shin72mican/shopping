package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;
import java.util.Objects;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Admins;

@Mapper
public interface AdminsMapper {
	@SelectProvider(AdminSqlProvider.class)
	List<Admins> findSearch(String name, String email, String authority, String sortType, String sortDirection, Integer displayCount, Integer currentPage);
	
	@SelectProvider(AdminSqlProvider.class)
	List<Admins> findEmail(Admins admin);
	
	@SelectProvider(AdminSqlProvider.class)
	List<Admins> find(Admins admin);
	
	@InsertProvider(AdminSqlProvider.class)
	void insert(Admins admin);
	
	@UpdateProvider(AdminSqlProvider.class)
	void update(Admins admin, Admins sorceAdmin);
	
	public class AdminSqlProvider implements ProviderMethodResolver {
		// メールアドレス検索
		public String findEmail(Admins admin) {
			return new SQL() {{
				SELECT("id", "name", "email", "password", "is_owner", 
						"CASE is_owner WHEN 1 THEN 'owner' WHEN 0 THEN 'general' ELSE 'no_data' END AS authority",
						"create_at", "update_at");
				FROM("admin_users");
				WHERE("email = #{email}");
			}}.toString();
		}
		
		// データ検索
		public String findSearch(String name, String email, String authority, String sortType, String sortDirection, Integer displayCount, Integer currentPage) {
			return new SQL() {{
				SELECT("id", "name", "email", "password", "is_owner", 
						"CASE is_owner WHEN 1 THEN 'オーナー' WHEN 0 THEN '一般' ELSE 'no_data' END AS authority",
						"create_at", "update_at");
				FROM("admin_users");
				if (!name.equals("")) {
					WHERE("name LIKE '%" + name + "%'");
				}
				if (authority.equals("owner")) {
					WHERE("is_owner = 1");
				} else if (authority.equals("general")) {
					WHERE("is_owner = 0");
				}
				if (sortDirection.equals("asc")) {
					ORDER_BY(sortType);
				} else if (sortDirection.equals("desc")) {
					ORDER_BY(sortType + " DESC");
				}
				OFFSET(displayCount * (currentPage - 1) + "ROWS FETCH FIRST " +  displayCount  + " ROWS ONLY");
			}}.toString();
		}
		
		public String find(Admins admin) {
			return new SQL() {{
				SELECT("id", "name", "email", "password", "is_owner", 
						"CASE is_owner WHEN 1 THEN 'オーナー' WHEN 0 THEN '一般' ELSE 'no_data' END AS authority",
						"create_at", "update_at");
				FROM("admin_users");
				if(Objects.nonNull(admin.getId())) {
					WHERE("id = #{id}");
				}
			}}.toString();
		}
		
		public String insert(Admins admin) {
			return new SQL() {{
				INSERT_INTO("admin_users");
				VALUES("name", "#{name}");
				VALUES("email", "#{email}");
				VALUES("password", "#{password}");
				VALUES("is_owner", "#{isOwner}");
				VALUES("create_at", "CURRENT_TIMESTAMP");
				VALUES("update_at", "CURRENT_TIMESTAMP");
			}}.toString();
		}
		
		public String update(Admins admin, Admins sorceAdmin) {
			return new SQL() {{
				UPDATE("admin_users");
				SET("name = #{admin.name}");
				if (!admin.getName().equals(sorceAdmin.getName())) {
					SET("email = #{admin.email}");
				}
				if (StringUtils.hasLength(admin.getPassword())) {
					SET("password = #{admin.password}");
				}
				SET("is_owner = #{admin.isOwner}");
				SET("update_at = CURRENT_TIMESTAMP");
				WHERE("id = #{admin.id}");
			}}.toString();
		}
		
	}
	
}
