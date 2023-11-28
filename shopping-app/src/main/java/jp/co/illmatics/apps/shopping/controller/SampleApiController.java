package jp.co.illmatics.apps.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;

@RestController
public class SampleApiController {

	@Autowired
	UsersMapper usersMapper;

	@GetMapping("/api/sample")
	public List<Users> sample() {
		List<Users> users = usersMapper.findAll();
		return users;
	}
}