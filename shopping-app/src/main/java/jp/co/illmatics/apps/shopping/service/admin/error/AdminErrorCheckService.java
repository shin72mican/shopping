package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Admins;
import jp.co.illmatics.apps.shopping.mapper.AdminsMapper;

@Service
public class AdminErrorCheckService {
	
	@Autowired
	AdminsMapper adminsMapper;
	
	public List<String> errorCheck(Admins admin, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		
		if(!StringUtils.hasLength(admin.getName())) {
			errors.add("名前を入力してください");
		}
		
		if(admin.getName().length() > 255) {
			errors.add("255文字を超える名前を登録することができません");
		}
		
		if(!StringUtils.hasLength(admin.getEmail())) {
			errors.add("メールアドレスを入力してください");
		} else {
			if(!admin.getEmail().matches("^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$")) {
				errors.add("メールアドレスの形式が異なります");
			}
		}
		
		if(admin.getName().length() > 255) {
			errors.add("255文字を超えるメールアドレスを登録することができません");
		}
		
		return errors;
	}
	
	// 新規エラーチェック
	public List<String> createErrorCheck(Admins admin, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		List<Admins> admins = adminsMapper.find(admin);
		
		if (admins.size() > 0) {
			errors.add("メールアドレスは既に登録されています");
		} else {
			errors = errorCheck(admin, confirmPassword);
			
			if(!StringUtils.hasLength(admin.getPassword())) {
				errors.add("パスワードを入力してください");
			}
			
			if(!StringUtils.hasLength(confirmPassword)) {
				errors.add("パスワード(確認)を入力してください");
			}
			
			if(StringUtils.hasLength(admin.getPassword()) && StringUtils.hasLength(confirmPassword)) {
				if(!ObjectUtils.nullSafeEquals(admin.getPassword(), confirmPassword)) {
					errors.add("パスワードが一致しません");	
				}
				
				if(admin.getPassword().length() < 4) {
					errors.add("パスワードは4文字以上で設定してください");
				}
			}
		}
		
		return errors;
	}
	
	// 編集エラーチェック
	public List<String> editErrorCheck(Admins admin, String confirmPassword) {
		List<String> errors = new ArrayList<String>();
		List<Admins> admins = adminsMapper.find(admin);
		
		if(admins.size() > 0) {
			if(!Objects.equals(admins.get(0).getId(), admin.getId())) {
				errors.add("メールアドレスは既に登録されています");
			} else {
				errors = errorCheck(admin, confirmPassword);
				
				if(!ObjectUtils.nullSafeEquals(admin.getPassword(), confirmPassword)) {
					errors.add("パスワードが一致しません");	
				}
				
				if(admin.getPassword().length() < 4 && admin.getPassword().length() > 0) {
					errors.add("パスワードは4文字以上で設定してください");
				}
			}
		} else {
			errors = errorCheck(admin, confirmPassword);
			
			if(!ObjectUtils.nullSafeEquals(admin.getPassword(), confirmPassword)) {
				errors.add("パスワードが一致しません");	
			}
			
			if(admin.getPassword().length() < 4) {
				errors.add("パスワードは4文字以上で設定してください");
			}
		}
		
		return errors;
	}
}
