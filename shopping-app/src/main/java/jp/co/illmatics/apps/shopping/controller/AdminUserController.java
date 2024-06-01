package jp.co.illmatics.apps.shopping.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.ProductReviewsMapper;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
import jp.co.illmatics.apps.shopping.mapper.WishProductsMapper;
import jp.co.illmatics.apps.shopping.service.PageService;
import jp.co.illmatics.apps.shopping.service.admin.error.UserErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.image.UserImageService;
import jp.co.illmatics.apps.shopping.service.admin.url.UserUrlService;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.users.SortType;

@Controller
public class AdminUserController {
	@Autowired
	UsersMapper usersMapper;
	
	@Autowired
	ProductReviewsMapper productReviewsMapper;
	
	@Autowired
	WishProductsMapper wishProductsMapper;
	
	@Autowired
	UserUrlService urlService;
	
	@Autowired
	UserErrorCheckService errorCheckService;
	
	@Autowired
	UserImageService imageService;
	
	@Autowired
	PageService pageService;
	
	private final PasswordEncoder passwordEncoder;

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
		List<Users> users = usersMapper.findByCondition(name, email, sortType, sortDirection, displayCount, currentPage);
		
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
		
		int pageCount = usersMapper.findSearchCount(name, email, sortType);
		
		// ページング
		pageService.indexPaging(model, pageCount, displayCount, currentPage);
		
		return "admin/users/index";
	}
	
	@GetMapping("/admin/users/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		
		Users user = new Users(id);
		List<Users> users = usersMapper.find(user);
		
		if(users.size() > 0) {
			model.addAttribute("user", users.get(0));
			return "/admin/users/show";
		} else {
			return "/error/404";
		}
		
	}
	
	// 新規登録ページ
	@GetMapping("/admin/users/create")
	public String create() {
		return "/admin/users/create";
	}
	
	// 
	@PostMapping("/admin/users")
	public String store(
			@RequestParam(name="name", defaultValue="") String name,
			@RequestParam(name="email", defaultValue="") String email,
			@RequestParam(name="password", defaultValue="") String password,
			@RequestParam(name="confirm_password", defaultValue="") String confirmPassword,
			@RequestParam(name = "user_image", defaultValue="") MultipartFile userImage,
			Model model) throws IOException {
		
		Users user = new Users(name, email, password);
		
		List<String> errors = errorCheckService.createErrorCheck(user, confirmPassword, userImage);
		
		if(!userImage.isEmpty() && errors.size() == 0) {
			// 画像ファイル保存後、ファイルパスを返す
			user.setImagePath(imageService.saveImage(userImage));
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
			user.setPassword(passwordEncoder.encode(password));
			usersMapper.insert(user);
			List<Users> users = usersMapper.findEmail(user);
			user = users.get(users.size() - 1);
			return "redirect:/admin/users/" + user.getId();
		}
	}
	
	// 編集ページ
	@GetMapping("/admin/users/{id}/edit")
	public String edit(
			@PathVariable("id") Long id,
			Model model) {
		
		Users user = new Users(id);
		List<Users> users = usersMapper.find(user);
		
		if(users.size() > 0) {
			model.addAttribute("user", users.get(0));
			return "/admin/users/edit";
		} else {
			return "/error/404";
		}
	}
	
	// データ更新
	@PutMapping("/admin/users/{id}")
	public String update(
			@PathVariable("id") Long id,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirm_password", defaultValue = "") String confirmPassword,
			@RequestParam(name = "user_image", defaultValue= "") MultipartFile userImage,
			@RequestParam(name = "delete_check", defaultValue= "false") Boolean deleteCheck,
			Model model) throws IOException {
		Users user = new Users(id, name, email, password);
		List<Users> users = usersMapper.find(user);
		if(users.size() > 0) {
			Users indexUser = users.get(0);
			List<String> errors = errorCheckService.editErrorCheck(user, confirmPassword, userImage);
			

			if(!userImage.isEmpty() && errors.size() == 0) {
				// 画像が登録されたとき
				// 画像の削除
				imageService.delete(indexUser);
				
				indexUser.setImagePath(imageService.saveImage(userImage));
			} else if(deleteCheck) {
				// image_deleteのチェックがされているとき
				// 画像の削除
				imageService.delete(indexUser);
				indexUser.setImagePath("");
			} else if(Objects.isNull(indexUser.getImagePath())) {
				indexUser.setImagePath("");
			}
			
			if(errors.size() > 0) {
				model.addAttribute("user", user);
				model.addAttribute("password", password);
				model.addAttribute("confirmPassword", confirmPassword);
				model.addAttribute("errors", errors);
				return "admin/users/edit";
			} else {
				indexUser.setName(name);
				indexUser.setEmail(email);
				// パスワードハッシュ化
				BCryptPasswordEncoder hashPassword = new BCryptPasswordEncoder();
				indexUser.setPassword(hashPassword.encode(password));
				
				usersMapper.update(indexUser);
				return "redirect:/admin/users/" + user.getId();
			}
		} else {
			return "/error/404";
		}
		
		
	}
	
	@DeleteMapping("/admin/users/{id}")
	public String delete(@PathVariable("id") Long id) {
		Users user = new Users(id);
		List<Users> users = usersMapper.find(user);
		
		if(users.size() > 0) {
			Users indexUser = users.get(0);
			// 画像削除
			imageService.delete(indexUser);
			
			// 顧客関連レビュー削除
			productReviewsMapper.usersDelete(indexUser);
			
			// 顧客関連評価削除
			wishProductsMapper.usersDelete(indexUser);
			
			// 顧客情報削除
			usersMapper.delete(indexUser);
			
			return "redirect:/admin/users";
		} else {
			return "/error/404";
		}
		
	}
}
