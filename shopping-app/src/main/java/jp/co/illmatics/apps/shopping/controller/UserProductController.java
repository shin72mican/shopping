package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;

@Controller
public class UserProductController {
	@Autowired
	CategoriesMapper categoriesMapper;
	
	@Autowired
	ProductsMapper productsMapper;
	
	@GetMapping("/products")
	public String index(
			Model model) {
		List<Categories> categories = categoriesMapper.findAll();
		List<Products> products = productsMapper.findAll();
		
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		
		return "user/products/index";
	}
}
