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
import jp.co.illmatics.apps.shopping.service.admin.error.CategoryErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.url.CategoryUrlService;
import jp.co.illmatics.apps.shopping.values.Page;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.categories.SortType;

@Controller
public class AdminCategoryController {
	@Autowired
	CategoryUrlService urlService;
	
	@Autowired
	CategoryErrorCheckService errorCheckService;
	
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
		List<Categories> categories = new ArrayList<Categories>();
		categories = categoriesMapper.findAll(name, sortType, sortDirection, displayCount, currentPage);
		
		String url = urlService.searchUrl(name, sortType, sortDirection, displayCount);
		
		model.addAttribute("name", name);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("displayCount", displayCount);
		
		// 検索
		model.addAttribute("typeList", SortType.values());
		model.addAttribute("sortList", SortDirection.values());
		model.addAttribute("countList", Display.values());
		
		model.addAttribute("url", url);
		model.addAttribute("categories", categories);
		
		int totalPage = categories.size() / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
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
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "orderNo", defaultValue = "") Long orderNo,
			Model model) {
		
		List<String> errors = new ArrayList<String>();
		
		// エラーチェック
		errors = errorCheckService.errorCheck(name, orderNo);
		
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
			
			String url = urlService.idUrl(categories.get(0).getId());
			return "redirect:" + url;
		}
		
	}
	
	@GetMapping("/admin/product_categories/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		List<Categories> category = categoriesMapper.find(new Categories(id));
		
		if (category.size() > 0) {
			Integer itemCount = categoriesMapper.itemCount(category.get(0).getOrderNo());
			model.addAttribute("itemCount", itemCount);
			model.addAttribute("category", category.get(0));
			return "admin/categories/show";
		} else {
			return "error/403";
		}
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
			@RequestParam(value = "orderNo", defaultValue = "0", required = true) Long orderNo,
			Model model) {
		
		List<String> errors = new ArrayList<String>();
		
		// エラーチェック
		errors = errorCheckService.errorCheck(name, orderNo);
		
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
			
			String url = urlService.idUrl(categories.get(0).getId());
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
