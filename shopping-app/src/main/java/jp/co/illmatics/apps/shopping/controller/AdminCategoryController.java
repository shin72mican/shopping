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

import jakarta.servlet.http.HttpServletRequest;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Controller
public class AdminCategoryController {
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
	@GetMapping("/admin/product_categories")
	public String index(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "sort_type", defaultValue = "id") String sortType,
			@RequestParam(name = "sort_direction", defaultValue = "asc") String sortDirection,
			@RequestParam(name = "display_count", defaultValue = "10") Integer displayCount,
			@RequestParam(name = "page", defaultValue="1") Integer currentPage,
			HttpServletRequest request,
			Model model) {
		final Integer showPage = 3;
		
		List<Categories> categories = new ArrayList<Categories>();
		categories = categoriesMapper.findAll(name, sortType, sortDirection, displayCount, currentPage);
		
		String url = request.getRequestURL().toString() + "?name=" + name + "&sort_type=" + sortType + "&sort_direction=" + sortDirection + "&display_count=" + displayCount;
		
		model.addAttribute("name", name);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("displayCount", displayCount);
		
		model.addAttribute("url", url);
		model.addAttribute("categories", categories);
		
		int totalPage = categories.size() / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % showPage;
		int endPage = (currentPage + showPage - 1 > totalPage) ? totalPage : (currentPage + showPage -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		
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
		
		// エラーチェック
		if (name.equals("") || name.length() == 0){
			errors.add("名前を入力してください");
		}
			
		if (orderNo == null) {
			errors.add("並び順番号を入力してください");
		} else if(orderNo <= 0) {
			errors.add("1以上の並び順番号を入力してください");
		}
		
		
		Categories category = new Categories(name, orderNo);
		List<Categories> checkCategories = categoriesMapper.find(category);
		if (checkCategories.size() > 0 && orderNo != null && orderNo >= 0) {
			errors.add("指定された並び順番号は既に存在します");
		}
		
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("name", name);
			model.addAttribute("orderNo", orderNo);
			return "admin/categories/create";
		} else {
			// 新規登録
			categoriesMapper.insert(category);
			
			List<Categories> categories = categoriesMapper.findLatest();
			
			String url = "/admin/product_categories/" + categories.get(0).getId();
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
			@RequestParam(value = "orderNo", defaultValue = "") Long orderNo,
			Model model) {
		
		List<String> errors = new ArrayList<String>();
		
		// エラーチェック
		if (name.equals("") || name.length() == 0){
			errors.add("名前を入力してください");
		}
			
		if (orderNo == null) {
			errors.add("並び順番号を入力してください");
		} else if(orderNo <= 0) {
			errors.add("1以上の並び順番号を入力してください");
		}
		
//		if(orderNo <= 0) {
//			errors.add("0以下の並び順番号は登録することができません");
//		}
		
		Categories category = new Categories(name, orderNo);
		List<Categories> checkCategories = categoriesMapper.find(category);
		if (checkCategories.size() > 0 && orderNo != null && orderNo >= 0) {
			errors.add("指定された並び順番号は既に存在します");
		}
		
		// 編集データの取得
		List<Categories> categories = categoriesMapper.find(new Categories(id));
		
		// 名前・並び順番号セッター
		categories.get(0).setName(name);
		categories.get(0).setOrderNo(orderNo);
		
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("category", categories.get(0));
			return "admin/categories/edit";
		} else {
			// 更新処理
			categoriesMapper.update(categories.get(0));
			
			String url = "/admin/product_categories/" + categories.get(0).getId();
			return "redirect:" + url;
		}
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
