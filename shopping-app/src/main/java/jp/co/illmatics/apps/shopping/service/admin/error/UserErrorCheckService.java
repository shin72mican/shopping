package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;

@Service
public class UserErrorCheckService {
	
	@Autowired
	UsersMapper usersMapper;
	
	// 新規・更新共通エラーチェック
	public List<String> errorCheck(Users user, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		
		if(!StringUtils.hasLength(user.getName())) {
			errors.add("名前を入力してください");
		}
		
		if(user.getName().length() > 255) {
			errors.add("255文字を超える名前を登録することができません");
		}
		
		if(!StringUtils.hasLength(user.getEmail())) {
			errors.add("メールアドレスを入力してください");
		} else {
			if(!user.getEmail().matches("^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$")) {
				errors.add("メールアドレスの形式が異なります");
			}
		}
		
		if(user.getName().length() > 255) {
			errors.add("255文字を超えるメールアドレスを登録することができません");
		}
		
		if(!StringUtils.hasLength(user.getPassword())) {
			errors.add("パスワードを入力してください");
		}
		
		if(!StringUtils.hasLength(confirmPassword)) {
			errors.add("パスワード(確認)を入力してください");
		}
		
		if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
			if(!ObjectUtils.nullSafeEquals(user.getPassword(), confirmPassword)) {
				errors.add("パスワードが一致しません");	
			}
			
			if(user.getPassword().length() < 4) {
				errors.add("パスワードは4文字以上で設定してください");
			}
		}
		
		return errors;
	}
	
	// 新規エラーチェック
	public List<String> createErrorCheck(Users user, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		List<Users> users = usersMapper.findEmail(user);
		
		if(users.size() > 0) {
			errors.add("メールアドレスは既に登録されています");
		} else {
			errors = errorCheck(user, confirmPassword);
		}
		
		return errors;
	}
	
	// 編集エラーチェック
	public List<String> editErrorCheck(Users user, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		List<Users> users = usersMapper.findEmail(user);
		
		if(users.size() > 0) {
			if(!Objects.equals(users.get(0).getId(), user.getId())) {
				errors.add("メールアドレスは既に登録されています");
			} else {
				errors = errorCheck(user, confirmPassword);
			}
		} else {
			errors = errorCheck(user, confirmPassword);
		}
		
		return errors;
	}
}
