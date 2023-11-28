package jp.co.illmatics.apps.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/admin/login")
	public String loginAsAdmin(Model model) {
		return "/admin/login";
	}

	@GetMapping("/users/login")
	public String loginAsUsers(Model model) {
		return "/users/login";
	}

}