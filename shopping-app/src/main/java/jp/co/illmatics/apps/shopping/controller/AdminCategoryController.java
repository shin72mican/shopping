package jp.co.illmatics.apps.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Controller
public class AdminCategoryController {
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
	@GetMapping("/admin/product_categories")
	public String index(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "category_sort", defaultValue = "id") String categorySort,
			@RequestParam(name = "sort_direction", defaultValue = "1") Long sortDirection,
			@RequestParam(name = "display_count", defaultValue = "10") Long displayCount,
			Model model) {
		List<Categories> categories = new ArrayList<Categories>();
		categories = categoriesMapper.findAll();
		model.addAttribute("categories", categories);
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
		
		List<String> errors = new ArrayList<String>();
		
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
			categoriesMapper.incrementSameOrderNo(orderNo);
			// 新規登録
			categoriesMapper.insert(category);
			
			String url = "/admin/product_categories";
			return "redirect:" + url;
		}
		
	}
	
	@GetMapping("/admin/product_categories/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		
		List<Categories> category = categoriesMapper.find(new Categories(id));
		model.addAttribute("category", category.get(0));
		
		return "admin/categories/show";
	}
	
	@GetMapping("/admin/product_categories/{id}/edit")
	public String edit(
			@PathVariable("id") Long id,
			Model model) {
		List<Categories> category = categoriesMapper.find(new Categories(id));
		model.addAttribute("category", category.get(0));
		return "admin/categories/edit";
	}
	
	@PutMapping("/admin/product_categories/{id}")
	public String update(
			@PathVariable("id") Long id,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "orderNo", defaultValue = "") Long orderNo
			) {
		
		// 編集データの取得
		List<Categories> category = categoriesMapper.find(new Categories(id));
		
//		Long count = categoriesMapper.count();
		
		// 名前・並び順番号セッター
		category.get(0).setName(name);
		category.get(0).setOrderNo(orderNo);
		
		// 更新処理
		categoriesMapper.update(category.get(0));
		
		String url = "/admin/product_categories/" + category.get(0).getId();
		return "redirect:" + url;
	}
	
	@DeleteMapping("/admin/product_categories/{id}")
	public String delete(@PathVariable("id") Long id) {
		// 削除データの取得
		List<Categories> category = categoriesMapper.find(new Categories(id));
		
		categoriesMapper.delete(category.get(0));
		
		String url = "/admin/product_categories";
		return "redirect:" + url;
	}
	
	
	
}
