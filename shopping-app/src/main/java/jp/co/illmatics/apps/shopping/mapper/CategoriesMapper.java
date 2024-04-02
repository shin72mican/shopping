package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;
import java.util.Objects;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Categories;

@Mapper
public interface CategoriesMapper {
	@SelectProvider(CategorySqlProvider.class)
	List<Categories> find(Categories categories);
	
	@SelectProvider(CategorySqlProvider.class)
	List<Categories> findAll();
	
	@SelectProvider(CategorySqlProvider.class)
	Long count();
	
	@UpdateProvider(CategorySqlProvider.class)
	void incrementOrderNo(Long orderNo, Long count);
	
	@UpdateProvider(CategorySqlProvider.class)
	void incrementSameOrderNo(Long orderNo);
	
	@InsertProvider(CategorySqlProvider.class)
	void insert(Categories categories);
	
	@UpdateProvider(CategorySqlProvider.class)
	void update(Categories categoreis);
	
	public class CategorySqlProvider implements ProviderMethodResolver {
		// 一データ取得
		public String find(Categories categories) {
			return new SQL() {{
				SELECT("id", "name", "order_no", "create_at", "update_at");
				FROM("product_categories");
				if (Objects.nonNull(categories.getId())) {
					WHERE("id = #{id}");
				}
				if (Objects.nonNull(categories.getName())) {
					WHERE("name = #{name}");
				}
				if (Objects.nonNull(categories.getOrderNo())) {
					WHERE("order_no = #{orderNo}");
				}
				if (Objects.nonNull(categories.getCreateAt())) {
					WHERE("createAt = #{createAt}");
				}
				if (Objects.nonNull(categories.getUpdateAt())) {
					WHERE("update_at = #{updateAt}");
				}
			}}.toString();
		}
		
		// 全データ取得
		public String findAll() {
			return new SQL() {{
				SELECT("id", "name", "order_no", "create_at", "update_at");
				FROM("product_categories");
				ORDER_BY("id");
			}}.toString();
		}
		
		public String count() {
			return new SQL() {{
				SELECT("count(*)");
				FROM("product_categories");
			}}.toString();
		}
		
		// order_no並び順に更新
		// order_noと一致するデータ以降 1 加算
		public String incrementOrderNo(Long orderNo, Long count) {
			return new SQL() {{
				UPDATE("product_categories");
				SET("order_no = order_no + 1");
				WHERE("order_no >= #{orderNo}");
				WHERE("order_no <= #{count}");
			}}.toString();
		}
		
		public String incrementSameOrderNo(Long orderNo) {
			return new SQL() {{
				UPDATE("product_categories");
				SET("order_no = order_no + 1");
				WHERE("order_no = #{orderNo}");
			}}.toString();
		}
		
		// 新規登録
		public String insert(Categories categories) {
			return new SQL() {{
				INSERT_INTO("product_categories");
				VALUES("name", "#{name}");
				VALUES("order_no", "#{orderNo}");
				VALUES("create_at", "CURRENT_TIMESTAMP");
				VALUES("update_at", "CURRENT_TIMESTAMP");
			}}.toString();
		}
		
		// 更新
		public String update(Categories categories) {
			return new SQL() {{
				UPDATE("product_categories");
				SET("name = #{name}");
				SET("order_no = #{orderNo}");
				SET("create_at = #{createAt}");
				SET("update_at = CURRENT_TIMESTAMP");
				WHERE("id = #{id}");
			}}.toString();
		}
		
	}
}







