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

import jp.co.illmatics.apps.shopping.entity.Products;

@Mapper
public interface ProductsMapper {
	@SelectProvider(ProductSqlProvider.class)
	List<Products> find(Products product);
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findAll();
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findSearch(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage);
	
	@SelectProvider(ProductSqlProvider.class)
	int findSearchCount(Long categoryId, String name, Long price, String standard);
	
	@InsertProvider(ProductSqlProvider.class)
	void insert(Products products);
	
	@UpdateProvider(ProductSqlProvider.class)
	void update(Products products);
	
	@DeleteProvider(ProductSqlProvider.class)
	void delete(Products products);
	
	public class ProductSqlProvider implements ProviderMethodResolver {
		// 単一データの取得
		public String find(Products product) {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "c.name AS category_name", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				INNER_JOIN("product_categories c ON p.product_category_id = c.id");
				if(Objects.nonNull(product.getId())) {
					WHERE("p.id = #{id}");
				}
			}}.toString();
		}
		
		// データ取得
		public String findAll() {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "c.name AS category_name", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				INNER_JOIN("product_categories c ON p.product_category_id = c.id");
			}}.toString();
		}
		
		// カテゴリ管理一覧 - 検索データ取得
		public String findSearch(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage) {
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
				if(categoryId > 0) {
					WHERE("p.product_category_id = " + categoryId);
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
		
		// 検索データ個数
		public String findSearchCount(Long categoryId, String name, Long price, String standard) {
			return new SQL() {{
				SELECT("COUNT (*)");
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
				if(categoryId > 0) {
					WHERE("p.product_category_id = " + categoryId);
				}
				INNER_JOIN("product_categories c ON p.product_category_id = c.id");
			}}.toString();
		}
		
		// データの挿入
		public String insert(Products products) {
			return new SQL() {{
				INSERT_INTO("products");
				VALUES("product_category_id", "#{productCategoryId}");
				VALUES("name", "#{name}");
				VALUES("price", "#{price}");
				VALUES("description", "#{description}");
				VALUES("image_path", "#{imagePath}");
				VALUES("create_at", "CURRENT_TIMESTAMP");
				VALUES("update_at", "CURRENT_TIMESTAMP");
			}}.toString();
			
		}
		
		// データの更新
		public String update(Products products) {
			System.out.println(products.getPrice());
			return new SQL() {{
				UPDATE("products");
				SET("product_category_id = #{productCategoryId}");
				SET("name = #{name}");
				SET("price = #{price}");
				SET("description = #{description}");
				SET("image_path = #{imagePath}");
				SET("update_at = CURRENT_TIMESTAMP");
				WHERE("id = #{id}");
			}}.toString();
		}
		
		// 削除
		public String delete(Products products) {
			return new SQL() {{
				DELETE_FROM("products");
				WHERE("id = #{id}");
			}}.toString();
		}
	}
}




