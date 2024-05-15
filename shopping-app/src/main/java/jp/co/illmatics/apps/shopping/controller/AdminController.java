package jp.co.illmatics.apps.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;
import jp.co.illmatics.apps.shopping.service.PageService;
import jp.co.illmatics.apps.shopping.service.admin.error.AdminErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.url.AdminUrlService;
import jp.co.illmatics.apps.shopping.session.AdminAccount;
import jp.co.illmatics.apps.shopping.values.admins.Authority;
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
	
	@Autowired
	PageService pageService;
	
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
		List<Admins> admins = adminsMapper.findByCondition(name, email, authority, sortType, sortDirection, displayCount, currentPage);
		
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
		
		int pageCount = adminsMapper.findByConditionCount(name, email, authority, sortType);
		
		// ページング
		pageService.indexPaging(model, pageCount, displayCount, currentPage);
        
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
		
		// 権限の判定
		/* オーナーではない
		 * または
		 * 一般ユーザー自身のページではない場合
		 * 403エラー表示
		 * */
		if(adminAccount.getAuthority().equals("owner") ||
				!(adminAccount.getAuthority().equals("general") && Objects.equals(admin.getId(), adminAccount.getId()))) {
			return "error/403";
		}
		
		List<Admins> admins = adminsMapper.find(admin);
		
		if(admins.size() > 0) {
			model.addAttribute("admin", admins.get(0));
			return "admin/admin_users/show";
		} else {
			return "error/404";
		}
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
		Admins admin = new Admins(name, email, password, authority);
		List<String> errors = errorCheckService.createErrorCheck(admin, confirmPassword);
		
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
	
	// 編集ページ
	@GetMapping("/admin/admin_users/{id}/edit")
	public String edit(
			@PathVariable("id") Long id,
			Model model) {
		Admins admin = new Admins(id);
		
		// 権限の判定
		/* オーナーではない
		 * または
		 * 一般ユーザー自身のページではない場合
		 * 403エラー表示
		 * */
		if(adminAccount.getAuthority().equals("owner") ||
				!(adminAccount.getAuthority().equals("general") && Objects.equals(admin.getId(), adminAccount.getId()))) {
			return "error/403";
		}
		
		List<Admins> admins = adminsMapper.find(admin);
		
		if(admins.size() > 0) {
			model.addAttribute("admin", admins.get(0));
			// Authority enumから権限の取得
			Authority authority = Authority.getByValue(admins.get(0).getIsOwner()); 
			model.addAttribute("authority", authority.getViweValue());
			return "admin/admin_users/edit";
			
		} else {
			return "error/404";
		}
	}
	
	// 更新処理
	@PutMapping("/admin/admin_users/{id}")
	public String update(
			@PathVariable("id") Long id,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirm_password", defaultValue = "") String confirmPassword,
			@RequestParam(name = "authority", defaultValue = "") String authority,
			Model model) {
		Admins admin = new Admins(id, name, email, password, authority);
		
		// 権限の判定
		/* オーナーではない
		 * または
		 * 一般ユーザー自身のページではない場合
		 * 403エラー表示
		 * */
		if(adminAccount.getAuthority().equals("owner") ||
				!(adminAccount.getAuthority().equals("general") && Objects.equals(admin.getId(), adminAccount.getId()))) {
			return "error/403";
		}
		
		List<Admins> admins = adminsMapper.find(admin);
		List<String> errors = errorCheckService.editErrorCheck(admin, confirmPassword);
		
		if(admins.size() > 0) {
			if(errors.size() > 0) {
				model.addAttribute("errors", errors);
				model.addAttribute("admin", admin);
				model.addAttribute("password", password);
				model.addAttribute("confirmPassword", confirmPassword);
				model.addAttribute("authority", authority);
				return "admin/admin_users/edit";
			} else {
				// パスワードハッシュ化
				BCryptPasswordEncoder hashPassword = new BCryptPasswordEncoder();
				admin.setPassword(hashPassword.encode(password));
				
				// Authority enumから権限の取得
				Authority formAuthority = Authority.getByViewValue(admin.getAuthority());
				admin.setIsOwner(formAuthority.getValue());
							
				adminsMapper.update(admin, admins.get(0));
				return "redirect:/admin/admin_users/" + admin.getId();
			}
		} else {
			return "error/404";
		}
	}
	
	// 管理者削除処理
	@DeleteMapping("/admin/admin_users/{id}")
	public String delete(
			@PathVariable("id") Long id,
			Model model) {
		
		Admins admin = new Admins(id);
		List<Admins> admins = adminsMapper.find(admin);
		
		if (admins.size() > 0) {
			List<String> errors = new ArrayList<String>();
			
			if(adminAccount.getId() == id) {
				errors.add("ログインアカウントは削除することができません");
				model.addAttribute("admin", admins.get(0));
				model.addAttribute("errors", errors);
				return "admin/admin_users/show";
			} else {
				// 削除処理
				adminsMapper.delete(admin);
				
				return "redirect:/admin/admin_users";
			}
		} else {
			return "error/404";
		}
		
	}
	
}
