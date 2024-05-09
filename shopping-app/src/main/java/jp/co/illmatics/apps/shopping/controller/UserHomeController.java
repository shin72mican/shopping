package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductReviewsMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Controller
public class UserHomeController {
	
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
	
	@GetMapping("/home")
	public String home(Model model) {
		
		Users user = new Users(userAccount.getId());
		
		List<Categories> categories = categoriesMapper.findAll();
		List<Products> products = productsMapper.findUserWishProducts(user);
		
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		
		return "user/home";
	}
	
}
