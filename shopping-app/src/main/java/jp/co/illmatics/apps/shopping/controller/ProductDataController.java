package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@RestController
public class ProductDataController {
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	ProductsMapper productsMapper;
	
	@GetMapping("/api/products")
	public List<Products> data(
			@RequestParam(name = "category_id", defaultValue = "0") Long categoryId,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "page", defaultValue = "1") Integer currentPage,
			@RequestParam(name = "sort", defaultValue="wish") String sort,
			Model model) {
		Products product = new Products(categoryId, name);
		List<Products> products = productsMapper.findSearchUser(product, currentPage, userAccount.getId(), sort);
		return products;
	}
}
