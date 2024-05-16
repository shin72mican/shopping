package jp.co.illmatics.apps.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	@GetMapping("/errors/403")
	public String error403() {
		return "error/403";
	}
	
}
