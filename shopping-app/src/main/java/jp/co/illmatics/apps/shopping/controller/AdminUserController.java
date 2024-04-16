package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
import jp.co.illmatics.apps.shopping.service.admin.url.UserUrlService;
import jp.co.illmatics.apps.shopping.values.Page;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.users.SortType;

@Controller
public class AdminUserController {
	@Autowired
	UsersMapper usersMapper;
	
	@Autowired
	UserUrlService urlService;
	
	@GetMapping("/admin/users")
	public String index(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "sort_type", defaultValue = "id") String sortType,
			@RequestParam(name = "sort_direction", defaultValue = "asc") String sortDirection,
			@RequestParam(name = "display_count", defaultValue = "10") Integer displayCount,
			@RequestParam(name = "page", defaultValue="1") Integer currentPage,
			Model model) {
		List<Users> users = usersMapper.findSearch(name, email, sortType, sortDirection, displayCount, currentPage);
		
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("displayCount", displayCount);
		
		// 検索
		model.addAttribute("typeList", SortType.values());
		model.addAttribute("sortList", SortDirection.values());
		model.addAttribute("countList", Display.values());
		
		model.addAttribute("users", users);
		
		String url = urlService.searchUrl(name, email, sortType, sortDirection, displayCount);
		model.addAttribute("url", url);
		
		int totalPage = users.size() / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		
		return "admin/users/index";
	}
}
