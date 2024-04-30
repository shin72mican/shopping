package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.AdminErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.url.AdminUrlService;
import jp.co.illmatics.apps.shopping.session.AdminAccount;
import jp.co.illmatics.apps.shopping.values.Page;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.admins.SortType;

@Controller
public class AdminController {
	
	@Autowired
	AdminAccount adminAccount;
	
	@Autowired
	AdminsMapper adminsMapper;
	
	@Autowired
	AdminUrlService urlService;
	
	@Autowired
	AdminErrorCheckService errorCheckService;
	
	// 一覧ページ
	@GetMapping("/admin/admin_users")
	public String index(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "authority", defaultValue = "all") String authority,
			@RequestParam(name = "sort_type", defaultValue = "id") String sortType,
			@RequestParam(name = "sort_direction", defaultValue = "asc") String sortDirection,
			@RequestParam(name = "display_count", defaultValue = "10") Integer displayCount,
			@RequestParam(name = "page", defaultValue = "1") Integer currentPage,
			Model model) {
		
		// 検索データ取得
		List<Admins> admins = adminsMapper.findSearch(name, email, authority, sortType, sortDirection, displayCount, currentPage);
		
		// フロントへフォームデータ
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		model.addAttribute("authority", authority);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("displayCount", displayCount);
		
		// フロントへ検索データ
		model.addAttribute("typeList", SortType.values());
		model.addAttribute("sortList", SortDirection.values());
		model.addAttribute("countList", Display.values());
		
		// フロントへデータベースデータ
		model.addAttribute("admins", admins);
		
		// ページング
		int totalPage = admins.size() / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        // ページングurl取得
     	String url = urlService.searchQueryUrl(name, email, authority, sortType, sortDirection, displayCount, currentPage);
     	model.addAttribute("url", url);
		
		return "admin/admin_users/index";
		
	}
	
	// 詳細ページ
	@GetMapping("/admin/admin_users/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		Admins admin = new Admins(id);
		List<Admins> admins = adminsMapper.find(admin);
		
		model.addAttribute("admin", admins.get(0));
		return "admin/admin_users/show";
	}
	
	// 新規登録ページ
	@GetMapping("/admin/admin_users/create")
	public String create(Model model) {
		// radio button 初期値
		model.addAttribute("authority", "general");
		return "admin/admin_users/create";
	}
	
	// 新規データ保存処理
	@PostMapping("/admin/admin_users")
	public String store(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirm_password", defaultValue = "") String confirmPassword,
			@RequestParam(name = "authority", defaultValue = "general") String authority,
			Model model) {
		Admins admin = new Admins(name, email, password);
		List<String> errors = errorCheckService.errorCheck(admin, confirmPassword, authority);
		
		if (errors.size() > 0) {
			model.addAttribute("name", name);
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("confirmPassword", confirmPassword);
			model.addAttribute("authority", authority);
			model.addAttribute("errors", errors);
			return "admin/admin_users/create";
		} else {
			// パスワードハッシュ化
			BCryptPasswordEncoder hashPassword = new BCryptPasswordEncoder();
			admin.setPassword(hashPassword.encode(password));
			
			// 保存データ権限設定
			if (authority.equals("general")) {
				admin.setIsOwner(0);
			} else if(authority.equals("owner")) {
				admin.setIsOwner(1);
			}
			adminsMapper.insert(admin);
			return "redirect:/admin/admin_users";
		}
	}
}
