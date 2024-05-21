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
import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface ProductsMapper {
	@SelectProvider(ProductSqlProvider.class)
	List<Products> find(Products product);
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findAll();
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findByCondition(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage);
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findUser(Products product, Long userId);
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findSearchUser(Products product, Integer currentPage, Long userId, String sort);
	
	@SelectProvider(ProductSqlProvider.class)
	Integer findSearchUserCount(Products product, Long userId);
	
	@SelectProvider(ProductSqlProvider.class)
	List<Products> findUserWishProducts(Users user);
	
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
				ORDER_BY("id");
			}}.toString();
		}
		
		// カテゴリ管理一覧 - 検索データ取得
		public String findByCondition(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, int displayCount, Integer currentPage) {
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
		
		// 顧客商品詳細データ
		public String findUser(Products product, Long userId) {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "w.product_id AS wish_product_id", "w.user_id AS wish_user_id", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				LEFT_OUTER_JOIN("wish_products w ON p.id = w.product_id AND w.user_id = #{userId}");
				WHERE("p.id = #{product.id}");
			}}.toString();
		}
		
		// 顧客商品一覧データ
		public String findSearchUser(Products product, Integer currentPage, Long userId, String sort) {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "w.product_id AS wish_product_id", "w.user_id AS wish_user_id", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				LEFT_OUTER_JOIN("wish_products w ON p.id = w.product_id AND w.user_id = #{userId}");
				OFFSET(15 * (currentPage - 1) + "ROWS FETCH FIRST " +  15  + " ROWS ONLY");
				if(!product.getName().equals("")) {
					WHERE("p.name LIKE '%" + product.getName() + "%'");
				}
				if(product.getProductCategoryId() > 0) {
					WHERE("p.product_category_id = " + product.getProductCategoryId());
				}
				if(sort.equals("wish")) {
					ORDER_BY("w.user_id");
				} else if(sort.equals("price_max")) {
					ORDER_BY("p.price DESC");
				} else if(sort.equals("price_min")) {
					ORDER_BY("p.price");
				} else if(sort.equals("release_date")) {
					ORDER_BY("p.update_at DESC");
				}
			}}.toString();
		}
		
		// 顧客商品一覧データ
		public String findSearchUserCount(Products product, Long userId) {
			return new SQL() {{
				SELECT("COUNT(*)");
				FROM("products p");
				LEFT_OUTER_JOIN("wish_products w ON p.id = w.product_id AND w.user_id = #{userId}");
				if(!product.getName().equals("")) {
					WHERE("p.name LIKE '%" + product.getName() + "%'");
				}
				if(product.getProductCategoryId() > 0) {
					WHERE("p.product_category_id = " + product.getProductCategoryId());
				}
			}}.toString();
		}
		
		// ユーザーお気に入り商品データ
		public String findUserWishProducts(Users user) {
			return new SQL() {{
				SELECT("p.id", "p.product_category_id", "w.product_id AS wish_product_id", "w.user_id AS wish_user_id", "p.name", "p.price", "p.description", "p.image_path", "p.create_at", "p.update_at");
				FROM("products p");
				LEFT_OUTER_JOIN("wish_products w ON p.id = w.product_id AND w.user_id = #{id}");
				WHERE("w.user_id = #{id}");
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




