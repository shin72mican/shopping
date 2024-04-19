package jp.co.illmatics.apps.shopping.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.UserErrorCheckService;
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
	
	@Autowired
	UserErrorCheckService errorCheckService;
	
	private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
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
		
		int totalPage = Integer.valueOf(usersMapper.countAll(name, email).toString()) / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		
		return "admin/users/index";
	}
	
	@GetMapping("/admin/users/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		Users user = new Users(id);
		List<Users> users = usersMapper.find(user);
		model.addAttribute("user", users.get(0));
		
		return "/admin/users/show";
	}
	
	// 新規登録ページ
	@GetMapping("/admin/users/create")
	public String create() {
		return "/admin/users/create";
	}
	
	@PostMapping("/admin/users")
	public String store(
			@RequestParam(name="name", defaultValue="") String name,
			@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="password", defaultValue="") String password,
			@RequestParam(name="confirm_password", defaultValue="") String confirmPassword,
			@RequestParam(name = "user_image", defaultValue="") MultipartFile userImage,
			Model model) throws IOException {
		
		Users user = new Users(name, email, password);
		
		List<String> errors = errorCheckService.errorCheck(user, confirmPassword);
		
		if(!userImage.isEmpty() && errors.size() == 0) {
			// 一意な画像ファイル名の作成
			// ファイル名取得
			String originalFileName = userImage.getOriginalFilename();
			// ファイル拡張子取得
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			// 一意な文字列取得
			UUID uuid = UUID.randomUUID();
			// 新しいファイル名
			String fileName = uuid.toString() + extension;
			// 保存先
			Path filePath=Paths.get("static/users/" + fileName);
			// 保存
			Files.copy(userImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			user.setImagePath("/users/" + fileName);
		} else {
			user.setImagePath("");
		}
		
		if(errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("name", name);
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("confirmPassword", confirmPassword);
			return "/admin/users/create";
		} else {
			user.setPassword(passwordEncoder.encode("password"));
			usersMapper.insert(user);
			return "redirect:/admin/users";
		}
	}
}
