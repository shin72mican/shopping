package jp.co.illmatics.apps.shopping.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/admin/home")
	public String home() {
		return "admin/home";
	}
	
	@GetMapping("/admin/test")
	public String test() {
		return "admin/home";
	}
}
