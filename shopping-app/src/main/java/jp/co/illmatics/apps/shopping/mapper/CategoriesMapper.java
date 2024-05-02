package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;
import java.util.Objects;

import org.apache.ibatis.annotations.DeleteProvider;
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
	List<Categories> findSearch(String name, String sortType, String sortDirection, Integer displayCount, Integer currentPage);
	
	@SelectProvider(CategorySqlProvider.class)
	List<Categories> findLatest();
	
	@SelectProvider(CategorySqlProvider.class)
	Integer itemCount(Long orderNo);
	
	@InsertProvider(CategorySqlProvider.class)
	void insert(Categories categories);
	
	@UpdateProvider(CategorySqlProvider.class)
	void update(Categories categoreis);
	
	@DeleteProvider(CategorySqlProvider.class)
	void delete(Categories categories);
	
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
			}}.toString();
		}
		
		// 検索データ取得
		public String findSearch(String name, String sortType, String sortDirection, Integer displayCount, Integer currentPage) {
			return new SQL() {{
				SELECT("id", "name", "order_no", "create_at", "update_at");
				FROM("product_categories");
				if(!name.equals("")) {
					WHERE("name LIKE '%" + name + "%'");
				}
				if (sortDirection.equals("asc")) {
					ORDER_BY(sortType);
				} else if (sortDirection.equals("desc")) {
					ORDER_BY(sortType + " DESC");
				}
				OFFSET(displayCount * (currentPage - 1) + "ROWS FETCH FIRST " +  displayCount  + " ROWS ONLY");
			}}.toString();
		}
		
		// 最近投稿
		public String findLatest() {
			return new SQL() {{
				SELECT("id", "name", "order_no", "create_at", "update_at");
				FROM("product_categories");
				WHERE("ROWNUM <= 10");
				ORDER_BY("id DESC");
			}}.toString();
		}
		
		// 商品個数
		public String itemCount(Long orderNo) {
			return new SQL() {{
				SELECT("COUNT (*)");
				FROM("products");
				WHERE("product_category_id = " + orderNo);
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
		
		// 削除
		public String delete(Categories categories) {
			return new SQL() {{
				DELETE_FROM("product_categories");
				WHERE("id = #{id}");
			}}.toString();
		}
		
	}
}







