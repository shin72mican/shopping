package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface UsersMapper {

	@SelectProvider(UserSqlProvider.class)
	List<Users> find(Users users);

	@SelectProvider(UserSqlProvider.class)
	List<Users> findAll();
	
	@SelectProvider(UserSqlProvider.class)
	List<Users> findSearch(String name, String email, String sortType, String sortDirection, Integer displayCount, Integer currentPage);

	@InsertProvider(UserSqlProvider.class)
	void insert(Users users);
	
	@UpdateProvider(UserSqlProvider.class)
	boolean update(Users users);
	
	@DeleteProvider(UserSqlProvider.class)
	void delete(Users users);
	
	@SelectProvider(UserSqlProvider.class)
	Long count(Users users);

	@SelectProvider(UserSqlProvider.class)
	Optional<Users> findByName(String username);

	public class UserSqlProvider implements ProviderMethodResolver {

		public String find(Users users) {
			return new SQL() {{
				SELECT("id", "name", "email", "email_verified_at", "password", "image_path", "create_at", "update_at");
				FROM("users");
				if (Objects.nonNull(users.getId())) {
					 WHERE("id = #{id}");
				}
				if (Objects.nonNull(users.getName())) {
					 WHERE("name = #{name}");
				}
				if (Objects.nonNull(users.getEmail())) {
					 WHERE("email = #{email}");
				}
				if (Objects.nonNull(users.getEmailVerifiedAt())) {
					 WHERE("email_verified_at = #{emailVerifiedAt}");
				}
				if (Objects.nonNull(users.getPassword())) {
					 WHERE("password = #{password}");
				}
				if (Objects.nonNull(users.getImagePath())) {
					 WHERE("image_path = #{imagePath}");
				}
				if (Objects.nonNull(users.getCreateAt())) {
					 WHERE("create_at = #{createAt}");
				}
				if (Objects.nonNull(users.getUpdateAt())) {
					 WHERE("update_at = #{updateAt}");
				}
			}}.toString();
		}

		public String findAll() {
			return new SQL() {{
//				SELECT("id", "name", "email", "email_verified_at", "password", "image_path", "create_at", "update_at");
				SELECT("id", "name", "email");
				FROM("users");

			}}.toString();
		}
		
		public String findSearch(String name, String email, String sortType, String sortDirection, Integer displayCount, Integer currentPage) {
			return new SQL() {{
				SELECT("id", "name", "email", "create_at", "update_at");
				FROM("users");
				if(!name.equals("")) {
					WHERE("name LIKE '%" + name + "%'");
				}
				if(!email.equals("")) {
					WHERE("email LIKE '%" + email + "%'");
				}
				if (sortDirection.equals("asc")) {
					ORDER_BY(sortType);
				} else if (sortDirection.equals("desc")) {
					ORDER_BY(sortType + " DESC");
				}
				OFFSET(displayCount * (currentPage - 1) + "ROWS FETCH FIRST " +  displayCount  + " ROWS ONLY");
			}}.toString();
		}

		public String insert(Users users) {
			return new SQL() {{
				INSERT_INTO("users");
				VALUES("name", "#{name}");
				VALUES("email", "#{email}");
				VALUES("email_verified_at", "#{emailVerifiedAt}");
				VALUES("password", "#{password}");
				VALUES("image_path", "#{imagePath}");
				VALUES("create_at", "#{createAt}");
				VALUES("update_at", "#{updateAt}");
			}}.toString();
		}

		public String update(Users users) {
			return new SQL() {{
				UPDATE("users");
				SET("name", "#{name}");
				SET("email", "#{email}");
				SET("email_verified_at", "#{emailVerifiedAt}");
				SET("password", "#{password}");
				SET("image_path", "#{imagePath}");
				SET("create_at", "#{createAt}");
				SET("update_at", "#{updateAt}");
				WHERE("id = #{id}");
			}}.toString();
		}

		public String delete(Users users) {
			return new SQL() {{
				DELETE_FROM("users");
				WHERE("id = #{id}");
			}}.toString();
		}

		public String count(Users users) {
			return new SQL() {{
				SELECT("COUNT(*)");
				FROM("users");
				if (Objects.nonNull(users.getId())) {
					 WHERE("id = #{id}");
				}
				if (Objects.nonNull(users.getName())) {
					 WHERE("name = #{name}");
				}
				if (Objects.nonNull(users.getEmail())) {
					 WHERE("email = #{email}");
				}
				if (Objects.nonNull(users.getEmailVerifiedAt())) {
					 WHERE("email_verified_at = #{emailVerifiedAt}");
				}
				if (Objects.nonNull(users.getPassword())) {
					 WHERE("password = #{password}");
				}
				if (Objects.nonNull(users.getImagePath())) {
					 WHERE("image_path = #{imagePath}");
				}
				if (Objects.nonNull(users.getCreateAt())) {
					 WHERE("create_at = #{createAt}");
				}
				if (Objects.nonNull(users.getUpdateAt())) {
					 WHERE("update_at = #{updateAt}");
				}
			}}.toString();
		}

		public String findByName(String name) {
			return new SQL() {{
				SELECT("id", "name", "email", "email_verified_at", "password", "image_path", "create_at", "update_at");
				FROM("users");
				WHERE("name = #{name}");
			}}.toString();
		}

	}
}