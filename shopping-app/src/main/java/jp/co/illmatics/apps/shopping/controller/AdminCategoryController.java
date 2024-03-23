package jp.co.illmatics.apps.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Controller
public class AdminCategoryController {
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
//	@Autowired
//	private UsersMapper categoriesMapper;
	
	@GetMapping("/admin/categories")
	public String index(Model model) {
		model.addAttribute("categories", categoriesMapper.findAll());
		return "admin/categories/index";
	}
}
