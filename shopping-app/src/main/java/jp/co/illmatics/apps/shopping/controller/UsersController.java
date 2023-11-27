package jp.co.illmatics.apps.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.illmatics.apps.shopping.entity.Users;

@RestController
public class UsersController {

	@GetMapping("/test")
	public List<Users> test() {
		List<Users> users = new ArrayList<>(0);
		Users user = new Users();
		for (Long i = 0L; i < 3; i++) {
			user.setId(i);
			user.setName("test" + i);
			user.setEmail("test" + i + "@test.com");
			users.add(user);
		}
		return users;
	}
}
