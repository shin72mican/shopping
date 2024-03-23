package jp.co.illmatics.apps.shopping.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/admin/login")
	public String loginAsAdmin(Model model) {
		BCryptPasswordEncoder password = new BCryptPasswordEncoder();
		model.addAttribute("pass", password.encode("general"));
		return "/admin/login";
	}

	@GetMapping("/users/login")
	public String loginAsUsers(Model model) {
		return "/users/login";
	}
	
	@GetMapping("/")
	public String home(Model model) {
		BCryptPasswordEncoder password = new BCryptPasswordEncoder();
		model.addAttribute("pass", password.encode("general"));
		return "/admin/login";
	}

}