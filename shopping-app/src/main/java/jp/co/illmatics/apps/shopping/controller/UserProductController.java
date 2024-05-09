package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.ProductReviews;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductReviewsMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.service.user.url.UserProductUrlService;
import jp.co.illmatics.apps.shopping.session.UserAccount;
import jp.co.illmatics.apps.shopping.values.Page;

@Controller
public class UserProductController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	CategoriesMapper categoriesMapper;
	
	@Autowired
	ProductsMapper productsMapper;
	
	@Autowired
	ProductReviewsMapper productReviewsMapper;
	
	@Autowired
	UserProductUrlService urlService;
	
	@GetMapping("/products")
	public String index(
			@RequestParam(name = "category_id", defaultValue = "0") Long categoryId,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "page", defaultValue = "1") Integer currentPage,
			Model model) {
		
		Products product = new Products(categoryId, name);
		
		List<Categories> categories = categoriesMapper.findAll();
		List<Products> products = productsMapper.findSearchUser(product, currentPage, userAccount.getId());
		
		model.addAttribute("categories", categories);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("searchName", name);
		
		model.addAttribute("products", products);
		
		String url = urlService.searchUrl(categoryId, name);
		model.addAttribute("url", url);
		
		int totalPage = (productsMapper.findSearchUserCount(product, userAccount.getId()) - 1) / 15 + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
		
		return "user/products/index";
	}
	
	@GetMapping("/products/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		List<Categories> categories = categoriesMapper.findAll();
		
		Products product = new Products(id);
		// 商品情報取得
		List<Products> products = productsMapper.findUser(product, userAccount.getId());
		// 商品レビュー取得
		List<ProductReviews> productReviews = productReviewsMapper.findProductReviewFromProduct(product);
		
		model.addAttribute("categories", categories);
		model.addAttribute("product", products.get(0));
		model.addAttribute("productReviews", productReviews);
		
		return "/user/products/show";
	}
}
