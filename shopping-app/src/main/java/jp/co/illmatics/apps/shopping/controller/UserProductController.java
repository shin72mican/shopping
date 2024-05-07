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
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.session.UserAccount;

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
		model.addAttribute("products", products);
		
		return "user/products/index";
	}
	
	@GetMapping("/products/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		Products product = new Products(id);
		List<Products> products = productsMapper.findUser(product, userAccount.getId());
		
		model.addAttribute("product", products.get(0));
		
		return "/user/products/show";
	}
}
