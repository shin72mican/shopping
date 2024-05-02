package jp.co.illmatics.apps.shopping.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.AdminLoginErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.error.UserLoginErrorCheckService;
import jp.co.illmatics.apps.shopping.session.AdminAccount;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Controller
public class LoginController {
	
	@Autowired
	HttpSession session;
	
	// 管理者セッション
	@Autowired
	AdminAccount adminAccount;
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	AdminsMapper adminsMapper;
	
	@Autowired
	UsersMapper usersMapper;
	
	// 管理者ログインエラーチェック
	@Autowired
	AdminLoginErrorCheckService adminLoginErrorCheckService;
	
	// 顧客ログインエラーチェック
	@Autowired
	UserLoginErrorCheckService userLoginErrorCheckService;
	
	// ログインページ
	@GetMapping("/admin/login")
	public String adminIndex(Model model) {
//		BCryptPasswordEncoder password = new BCryptPasswordEncoder();
//		model.addAttribute("pass", password.encode("pass"));
		return "admin/login";
	}
	
	// ログイン処理
	@PostMapping("/admin/login")
	public String adminLogin(
			@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="password", defaultValue="") String password,
			Model model) {
		Admins admin = new Admins(email, password);
		List<Admins> admins = adminsMapper.findEmail(admin);
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
	
	
	// 顧客ログインページ
	@GetMapping("/login")
	public String userLoginIndex(Model model) {
		return "user/login";
	}
	
	// 顧客ログイン
	@PostMapping("/login")
	public String userLogin(
			@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="password", defaultValue="") String password,
			Model model) {
		Users user = new Users(email, password);
		List<Users> users = usersMapper.findEmail(user);
		
		// エラーチェック
		List<String> errors = userLoginErrorCheckService.loginErrorCheck(user);
		
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			return "user/login";
		} else {
			// セッションデータ保持
			// 名前、メールアドレス
			userAccount.setId(users.get(0).getId());
			userAccount.setName(users.get(0).getName());
			userAccount.setEmail(users.get(0).getEmail());
			return "redirect:/home";
		}
	}
	
	// 顧客新規登録ページ
	@GetMapping("/register")
	public String userSigninIndex(Model model) {
		return "user/signin";
	}
	
	// 新規登録処理
	@PostMapping("/register")
	public String userSignin(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "comfirm_password", defaultValue = "") String confirmPassword,
			@RequestParam(name = "user_image", defaultValue= "") MultipartFile userImage,
			Model model) throws IOException {
		
		Users user = new Users(name, email, password);
		
		List<String> errors = userLoginErrorCheckService.signinErrorCheck(user, confirmPassword, userImage);
		
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("name", name);
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("confirmPassword", confirmPassword);
			return "user/signin";
		} else {
			
			return "redirect:/home";
		}
	}
	
}