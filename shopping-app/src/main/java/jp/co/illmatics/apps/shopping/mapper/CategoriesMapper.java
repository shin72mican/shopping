package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Categories;

@Mapper
public interface CategoriesMapper {
	@SelectProvider(CategorySqlProvider.class)
	List<Categories> findAll();
	
	public class CategorySqlProvider implements ProviderMethodResolver {
		public String findAll() {
			return new SQL() {{
				SELECT("id", "name");
//				SELECT("id", "name", "order_no", "create_at", "update_at");
				FROM("product_categories");
			}}.toString();
		}
	}
}
