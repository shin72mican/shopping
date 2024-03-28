package jp.co.illmatics.apps.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Controller
public class AdminCategoryController {
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
//	@Autowired
//	private UsersMapper categoriesMapper;
	
	@GetMapping("/admin/product_categories")
	public String index(Model model) {
		model.addAttribute("categories", categoriesMapper.findAll());
		return "admin/categories/index";
	}
	
	@GetMapping("/admin/product_categories/create")
	public String create() {
		return "admin/categories/create";
	}
	
	@PostMapping("/admin/product_categories")
	public String store(
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "orderNo", defaultValue = "") Long orderNo,
			Model model) {
		
		List<String> errors = new ArrayList();
		
		if(name.equals("") || name.length() == 0){
			errors.add("名前を入力してください");
		}
			
		if(orderNo == null) {
			errors.add("並び順番号を入力してください");
		}
		
		if(errors.size() > 0) {
			model.addAttribute("errors", errors);
			return "admin/categories/create";
		} else {
			Categories category = new Categories(name, orderNo);
			
			Long count = categoriesMapper.count();
			
			// 並び順番号更新
			categoriesMapper.incrementOrderNo(orderNo, count);
			// 新規登録
			categoriesMapper.insert(category);
			
			return "redirect:/admin/product_categories";
		}
		
	}
}
