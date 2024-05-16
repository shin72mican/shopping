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

import jp.co.illmatics.apps.shopping.entity.ProductReviews;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.entity.Users;

@Mapper
public interface ProductReviewsMapper {
	// 商品レビュー保存
	@InsertProvider(ProductReviewSqlProvider.class)
	void insert(ProductReviews productReview);
	
	// 商品レビュー取得
	@SelectProvider(ProductReviewSqlProvider.class)
	List<ProductReviews> findProductReviewFromProduct(Products product);
	
	// 商品レビュー取得
	@SelectProvider(ProductReviewSqlProvider.class)
	List<ProductReviews> findProductReview(ProductReviews productreview);
	
	// 商品レビュー更新
	@UpdateProvider(ProductReviewSqlProvider.class)
	void update(ProductReviews productreview);
	
	@DeleteProvider(ProductReviewSqlProvider.class)
	void usersDelete(Users user);
	
	@DeleteProvider(ProductReviewSqlProvider.class)
	void productsDelete(Products product);
	
	public class ProductReviewSqlProvider implements ProviderMethodResolver {
		
		// 商品レビュー保存
		public String insert(ProductReviews productReview) {
			return new SQL() {{
				INSERT_INTO("product_reviews");
				VALUES("product_id", "#{productId}");
				VALUES("user_id", "#{userId}");
				VALUES("title", "#{title}");
				VALUES("body", "#{body}");
				VALUES("rank", "#{rank}");
				VALUES("create_at", "CURRENT_TIMESTAMP");
				VALUES("update_at", "CURRENT_TIMESTAMP");
			}}.toString();
		}
		
		// 商品レビュー取得
		public String findProductReviewFromProduct(Products product) {
			return new SQL() {{
				SELECT("pr.id, pr.product_id, pr.user_id, u.name AS user_name, u.image_path AS user_image_path, pr.title, pr.body, pr.rank");
				FROM("product_reviews pr");
				INNER_JOIN("users u ON pr.user_id = u.id");
				WHERE("pr.product_id = #{id}");
			}}.toString();
		}
		
		public String findProductReview(ProductReviews productReview) {
			return new SQL() {{
				SELECT("pr.id, pr.product_id, pr.user_id, u.name AS user_name, u.image_path AS user_image_path, pr.title, pr.body, pr.rank");
				FROM("product_reviews pr");
				INNER_JOIN("users u ON pr.user_id = u.id");
				if (Objects.nonNull(productReview.getId())) {
					WHERE("pr.id = #{id}");
				}
				if (Objects.nonNull(productReview.getProductId())) {
					WHERE("pr.product_id = #{productId}");
				}
			}}.toString();
		}
		
		// 商品レビュー更新
		public String update(ProductReviews productReview) {
			return new SQL() {{
				UPDATE("product_reviews");
				SET("title = #{title}");
				SET("body = #{body}");
				SET("rank = #{rank}");
				WHERE("id = #{id}");
			}}.toString();
		}
		
		// 顧客関連のレビューを削除
		public String usersDelete(Users user) {
			return new SQL() {{
				DELETE_FROM("product_reviews");
				WHERE("user_id = #{id}");
			}}.toString();
		}
		
		// 削除商品関連のレビューを削除
		public String productsDelete(Products product) {
			return new SQL() {{
				DELETE_FROM("product_reviews");
				WHERE("product_id = #{id}");
			}}.toString();
		}
	}
}

