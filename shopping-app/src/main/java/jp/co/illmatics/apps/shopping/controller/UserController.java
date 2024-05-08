package jp.co.illmatics.apps.shopping.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.UserErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.image.UserImageService;
import jp.co.illmatics.apps.shopping.session.UserAccount;

@Controller
public class UserController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserAccount userAccount;
	
	@Autowired
	UsersMapper usersMapper;
	
	@Autowired
	UserErrorCheckService errorCheckService;
	
	@Autowired
	UserImageService imageService;
	
	// 顧客編集ページ
	@GetMapping("/users/edit/{id}")
	public String edit(
			@PathVariable("id") Long id,
			Model model) {
		
		Users user = new Users(id);
		List<Users> users = usersMapper.find(user);
		
		model.addAttribute("user", users.get(0));
		
		return "user/users/edit";
	}
	
	// 顧客情報更新処理
	@PutMapping("/users/{id}")
	public String update(@PathVariable("id") Long id,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "confirm_password", defaultValue = "") String confirmPassword,
			@RequestParam(name = "user_image", defaultValue= "") MultipartFile userImage,
			@RequestParam(name = "delete_check", defaultValue= "false") Boolean deleteCheck,
			Model model) throws IOException {
		Users user = new Users(id, name, email, password);
		List<Users> users = usersMapper.find(user);
		List<String> errors = errorCheckService.editErrorCheck(user, confirmPassword, userImage);
		
		// ログインユーザーであるか比較
		if(Objects.equals(users.get(0).getId(), userAccount.getId())) {
			
			if(!userImage.isEmpty() && errors.size() == 0) {
				// 画像の削除
				imageService.delete(users.get(0));
				
				users.get(0).setImagePath(imageService.saveImage(userImage));
			} else if(deleteCheck) {
				// 画像の削除
				imageService.delete(users.get(0));
				users.get(0).setImagePath("");
			}
			
			if(errors.size() > 0) {
				model.addAttribute("user", user);
				model.addAttribute("password", password);
				model.addAttribute("confirmPassword", confirmPassword);
				model.addAttribute("errors", errors);
				return "user/users/edit";
			} else {
				users.get(0).setName(name);
				users.get(0).setEmail(email);
				// パスワードハッシュ化
				BCryptPasswordEncoder hashPassword = new BCryptPasswordEncoder();
				users.get(0).setPassword(hashPassword.encode(password));
				
				usersMapper.update(users.get(0));
				return "redirect:/home";
			}
		} else {
			return "redirect:/errors/403";
		}
		
		
	}

}
