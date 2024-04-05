package jp.co.illmatics.apps.shopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import jp.co.illmatics.apps.shopping.entity.Products;

@Mapper
public interface ProductsMapper {
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findAll();
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findSearch(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage);
	
	public class ProductSqlProvider implements ProviderMethodResolver {
		// データ取得
		public String findAll() {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "c.name AS category_name", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				INNER_JOIN("product_categories c ON p.product_category_id = c.id");
			}}.toString();
		}
		
		// 検索データ取得
		public String findSearch(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage) {
			System.out.println(
					new SQL() {{
						SELECT("p.id", "p.product_category_id", "c.name AS category_name", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
						FROM("products p");
						if(!name.equals("")) {
							WHERE("p.name LIKE '%" + name + "%'");
						}
						if(standard.equals("over") && price != null) {
							WHERE("p.price >= " + price);
						} else if(standard.equals("less") && price != null) {
							WHERE("p.price <= " + price);
						} else {
							WHERE("p.price >= 0");
						}
						INNER_JOIN("product_categories c ON p.product_category_id = c.id");
						if (sortDirection.equals("asc")) {
							ORDER_BY(sortType);
						} else if (sortDirection.equals("desc")) {
							ORDER_BY(sortType + " DESC");
						}
						OFFSET(displayCount * (currentPage - 1) + "ROWS FETCH FIRST " +  displayCount  + " ROWS ONLY");
					}}.toString()
					);
			
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "c.name AS category_name", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				if(!name.equals("")) {
					WHERE("p.name LIKE '%" + name + "%'");
				}
				if(standard.equals("over") && price != null) {
					WHERE("p.price >= " + price);
				} else if(standard.equals("less") && price != null) {
					WHERE("p.price <= " + price);
				} else {
					WHERE("p.price >= 0");
				}
				INNER_JOIN("product_categories c ON p.product_category_id = c.id");
				if (sortDirection.equals("asc")) {
					ORDER_BY(sortType);
				} else if (sortDirection.equals("desc")) {
					ORDER_BY(sortType + " DESC");
				}
				OFFSET(displayCount * (currentPage - 1) + "ROWS FETCH FIRST " +  displayCount  + " ROWS ONLY");
			}}.toString();
		}
	}
}
