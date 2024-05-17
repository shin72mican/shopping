package jp.co.illmatics.apps.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.WishProducts;
import jp.co.illmatics.apps.shopping.mapper.WishProductsMapper;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Controller
public class UserWishProductController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	WishProductsMapper wishProductsMapper;
	
	@PostMapping("/wish_products/{product_id}")
	public String store(
			HttpServletRequest request,
			@PathVariable("product_id") Long productId) {
		String referer = request.getHeader("Referer");
		
		WishProducts wishProduct = new WishProducts(productId, userAccount.getId());
		
		wishProductsMapper.insert(wishProduct);
		
		return "redirect:" + referer;
	}
	
	@DeleteMapping("/wish_products/{product_id}")
	public String delete(
			HttpServletRequest request,
			@PathVariable("product_id") Long productId) {
		String referer = request.getHeader("Referer");
		
		WishProducts wishProduct = new WishProducts(productId, userAccount.getId());
		
		wishProductsMapper.delete(wishProduct);
		
		return "redirect:" + referer;
	}
}
