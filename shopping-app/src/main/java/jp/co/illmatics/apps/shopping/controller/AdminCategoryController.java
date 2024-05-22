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
import jp.co.illmatics.apps.shopping.service.PageService;
import jp.co.illmatics.apps.shopping.service.admin.error.CategoryErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.url.CategoryUrlService;
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
	PageService pageService;
	
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
		categories = categoriesMapper.findByCondition(name, sortType, sortDirection, displayCount, currentPage);
		
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
		
		int pageCount = categoriesMapper.findByConditionCount(name);
		
		// ページング
		pageService.indexPaging(model, pageCount, displayCount, currentPage);
		
		return "admin/categories/index";
	}
	
	@GetMapping("/admin/product_categories/create")
	public String create() {
		return "admin/categories/create";
	}
	
	@PostMapping("/admin/product_categories")
	public String store(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "orderNo", defaultValue = "") String formOrderNo,
			Model model) {
		
		List<String> errors = new ArrayList<String>();
		
		// 入力された並び順番号が数値であるかの例外処理
		/* 入力された並び順番号が数値であればString型からLong型に変換し変数に格納
		 * 入力された並び順番号が数値でなければ変数にnullを格納
		 * */
		Long orderNo;
		try {
			orderNo = Long.parseLong(formOrderNo);
		} catch (NumberFormatException e) {
			orderNo = null;
		}
		
		Categories category = new Categories(name, orderNo);
		
		// エラーチェック
		errors.addAll(errorCheckService.errorCheck(category, formOrderNo));
		
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("name", name);
			model.addAttribute("orderNo", formOrderNo);
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
			Integer itemCount = categoriesMapper.itemCount(category.get(0).getId());
			model.addAttribute("itemCount", itemCount);
			model.addAttribute("category", category.get(0));
			return "admin/categories/show";
		} else {
			return "error/404";
		}
	}
	
	@GetMapping("/admin/product_categories/{id}/edit")
	public String edit(
			@PathVariable("id") Long id,
			Model model) {
		List<Categories> categories = categoriesMapper.find(new Categories(id));
		
		if (categories.size() > 0) {
			model.addAttribute("category", categories.get(0));
			return "admin/categories/edit";
		} else {
			return "/error/404";
		}
		
	}
	
	@PutMapping("/admin/product_categories/{id}")
	public String update(
			@PathVariable("id") Long id,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "orderNo", defaultValue = "0", required = true) String formOrderNo,
			Model model) {
		
		List<String> errors = new ArrayList<String>();
		
		Long orderNo;
		try {
			orderNo = Long.parseLong(formOrderNo);
		} catch (NumberFormatException e) {
			orderNo = null;
		}
		
		Categories category = new Categories(id, name, orderNo);
		
		// エラーチェック
		errors = errorCheckService.errorCheck(category, formOrderNo);
		
		// 検索用インスタンス(idのみのインスタンス)入れなおし
		category  = new Categories(id);
		
		// 編集データの取得
		List<Categories> categories = categoriesMapper.find(category);
		
		if (categories.size() > 0) {
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
		} else {
			return "/error/404";
		}
	}
	
	@DeleteMapping("/admin/product_categories/{id}")
	public String delete(@PathVariable("id") Long id) {
		// 削除データの取得
		List<Categories> categories = categoriesMapper.find(new Categories(id));
		
		if (categories.size() > 0) {
			categoriesMapper.delete(categories.get(0));
			
			String url = "/admin/product_categories";
			return "redirect:" + url;
		} else {
			return "/error/404";
		}
	}
	
}
