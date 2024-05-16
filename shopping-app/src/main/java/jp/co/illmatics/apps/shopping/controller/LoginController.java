package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.AdminLoginErrorCheckService;
import jp.co.illmatics.apps.shopping.session.AdminAccount;

@Controller
public class LoginController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	AdminAccount adminAccount;
	
	@Autowired
	AdminsMapper adminsMapper;
	
	@Autowired
	AdminLoginErrorCheckService adminLoginErrorCheckService;
	
	// ログインページ
	@GetMapping("/admin/login")
	public String adminIndex(Model model) {
		return "admin/login";
	}
	
	// ログイン処理
	@PostMapping("/admin/login")
	public String adminLogin(
			@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="password", defaultValue="") String password,
			Model model) {
		Admins admin = new Admins(email, password);
		List<Admins> admins = adminsMapper.findAutority(admin);
		// エラーチェック
		List<String> errors = adminLoginErrorCheckService.errorCheck(admin, admins);
		
		if(errors.size() > 0) {
			model.addAttribute("errors", errors);
			return "admin/login";
		} else {
			// セッションデータ保持
			// 名前、メールアドレス、権限
			adminAccount.setId(admins.get(0).getId());
			adminAccount.setName(admins.get(0).getName());
			adminAccount.setEmail(admins.get(0).getEmail());
			adminAccount.setAuthority(admins.get(0).getAuthority());
			return "redirect:/admin/home";
		}
	}
	
	// ログアウト
	@PostMapping("/admin/logout")
	public String logout() {
		// セッション削除
		session.invalidate();
		return "redirect:/admin/login";
	}

	@GetMapping("/users/login")
	public String loginAsUsers(Model model) {
		return "/users/login";
	}

}