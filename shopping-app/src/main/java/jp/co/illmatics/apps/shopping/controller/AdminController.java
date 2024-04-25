package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;
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
        
        // ページングurl
     	String url = urlService.searchQueryUrl(name, email, authority, sortType, sortDirection, displayCount, currentPage);
     	model.addAttribute("url", url);
		
		return "admin/admin_users/index";
		
	}
}
