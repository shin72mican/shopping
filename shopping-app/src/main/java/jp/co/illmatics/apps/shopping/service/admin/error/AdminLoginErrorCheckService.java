package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Admins;

@Service
public class AdminLoginErrorCheckService {
	
	public List<String> errorCheck(Admins admin, List<Admins> admins) {
		// ハッシュ化呼び出し
		BCryptPasswordEncoder password = new BCryptPasswordEncoder();
		
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(admin.getEmail())) {
			errors.add("メールアドレスを入力してください");
		}
		
		if (!StringUtils.hasLength(admin.getPassword())) {
			errors.add("パスワードを入力してください");
		}
		
		// メールアドレス、パスワード入力あり
		if (StringUtils.hasLength(admin.getEmail()) && StringUtils.hasLength(admin.getPassword())) {
			// メールアドレス存在確認
			if (admins.size() == 0) {
				errors.add("アカウントが存在しません");
			}
			
			// パスワード比較
			if (admins.size() > 0 && !password.matches(admin.getPassword(), admins.get(0).getPassword())) {
				errors.add("パスワードが一致しません");
			}
		}
		
		return errors;
	}
}
