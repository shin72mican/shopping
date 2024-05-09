package jp.co.illmatics.apps.shopping.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.ProductReviews;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductReviewsMapper;
import jp.co.illmatics.apps.shopping.service.user.error.ProductReviewErrorCheckService;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Controller
public class UserProductReviewController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	CategoriesMapper categoriesMapper;
	
	@Autowired
	ProductReviewsMapper productReviewsMapper;
	
	@Autowired
	ProductReviewErrorCheckService errorCheckServise;
	
	@GetMapping("/products/{products_id}/product_reviews/create")
	public String create(
			@PathVariable("products_id") Long productId,
			Model model) {
		
		List<Categories> categories = categoriesMapper.findAll();
		
		ProductReviews productReview = new ProductReviews(productId);
		
		model.addAttribute("categories", categories);
		model.addAttribute("productReview", productReview);
		
		return "user/product_reviews/create";
	}
	
	@PostMapping("/products/{products_id}/product_reviews")
	public String store(
			@PathVariable("products_id") Long productId,
			@RequestParam(name = "title", defaultValue = "") String title,
			@RequestParam(name = "body", defaultValue = "") String body,
			@RequestParam(name = "rank", defaultValue = "1") int rank,
			Model model) {
		
		ProductReviews productReview = new ProductReviews(productId, userAccount.getId(), title, body, rank);
		
		List<String> errors = errorCheckServise.errorCheck(productReview);
		
		if(errors.size() > 0) {
			List<Categories> categories = categoriesMapper.findAll();
			model.addAttribute("categories", categories);
			
			model.addAttribute("errors", errors);
			model.addAttribute("productReview", productReview);
			return "user/product_reviews/create";
		} else {
			// レビューデータ保存
			productReviewsMapper.insert(productReview);
			return "redirect:/products/" + productId;
		}
	}
	
	@GetMapping("/products/{product_id}/product_reviews/{product_review_id}/edit")
	public String edit(
			@PathVariable("product_id") Long productId,
			@PathVariable("product_review_id") Long id,
			Model model) {
		
		ProductReviews productReview = new ProductReviews(id, productId);
		List<ProductReviews> productReviews = productReviewsMapper.findProductReview(productReview);
		
		if(productReviews.size() > 0 && Objects.equals(productReviews.get(0).getUserId(), userAccount.getId())) {
			System.out.println("tset");
			List<Categories> categories = categoriesMapper.findAll();
			model.addAttribute("categories", categories);
			
			model.addAttribute("productReview", productReviews.get(0));
			
			return "user/product_reviews/edit";
		} else {
			return "redirect:/errors/403";
		}
		
	}
	
	@PatchMapping("/products/{product_id}/product_reviews/{product_review_id}")
	public String update(
			@PathVariable("product_id") Long productId,
			@PathVariable("product_review_id") Long id,
			@RequestParam(name = "title", defaultValue = "") String title,
			@RequestParam(name = "body", defaultValue = "") String body,
			@RequestParam(name = "rank", defaultValue = "1") int rank,
			Model model) {
		
		ProductReviews productReview = new ProductReviews(id, productId, userAccount.getId(), title, body, rank);
		
		List<String> errors = errorCheckServise.errorCheck(productReview);
		
		if(errors.size() > 0) {
			List<Categories> categories = categoriesMapper.findAll();
			model.addAttribute("categories", categories);
			
			model.addAttribute("errors", errors);
			model.addAttribute("productReview", productReview);
			return "user/product_reviews/edit";
		} else {
			// レビューデータ更新
			productReviewsMapper.update(productReview);
			return "redirect:/products/" + productId;
		}
		
	}
	
	

}



