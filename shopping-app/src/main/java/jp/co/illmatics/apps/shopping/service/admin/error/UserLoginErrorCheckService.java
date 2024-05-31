package jp.co.illmatics.apps.shopping.service.admin.error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;

@Service
public class UserLoginErrorCheckService {
	
	@Autowired
	UsersMapper usersMapper;
	
	// ログインエラーチェック
	public List<String> loginErrorCheck(Users user) {
		List<Users> users = usersMapper.findEmail(user);
		// ハッシュ化呼び出し
		BCryptPasswordEncoder password = new BCryptPasswordEncoder();
		
		List<String> errors = new ArrayList<String>();
		
		// メールアドレス入力チェック
		if (!StringUtils.hasLength(user.getEmail())) {
			errors.add("メールアドレスを入力してください");
		}
		// パスワード入力チェック
		if (!StringUtils.hasLength(user.getPassword())) {
			errors.add("パスワードを入力してください");
		}
		
		// メールアドレス、パスワード入力あり
		if (StringUtils.hasLength(user.getEmail()) && StringUtils.hasLength(user.getPassword())) {
			// メールアドレス存在確認
			if (users.size() == 0) {
				errors.add("アカウントが存在しません");
			}
			
			// パスワード比較
			if (users.size() > 0 && !password.matches(user.getPassword(), users.get(0).getPassword())) {
				errors.add("パスワードが一致しません");
			}
		}
		
		return errors;
	}
	
	// 新規登録エラーチェック
	public List<String> signinErrorCheck(Users user, String confirmPassword, MultipartFile userImage) throws IOException {
		List<Users> users = usersMapper.findEmail(user);
		
		List<String> errors = new ArrayList<String>();
		
		if(users.size() > 0) {
			// メールアドレスがすでに登録されるかチェック
			errors.add("メールアドレスは既に使われています");
		} else {
			// name - byte数255以下であるかの判定
			if(user.getName().getBytes(StandardCharsets.UTF_8).length > 255) {
	        	errors.add("半角の文字列であれば255文字、全角の文字列ならば85文字を超えるユーザー名を登録することができません");
	        } else if (!StringUtils.hasLength(user.getName())) {
	        	// ユーザー名入力チェック
				errors.add("ユーザー名を入力してください");
			}
			
			// ユーザー名、255文字以内であるかチェック
			if(user.getName().length() > 255) {
				errors.add("255文字を超える名前を登録することができません");
			}
			
			// メールアドレス入力チェック
			if (!StringUtils.hasLength(user.getEmail())) {
				errors.add("メールアドレスを入力してください");
			} else if(!user.getEmail().matches("^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$")) {
				errors.add("メールアドレスの形式が異なります");
			}
			
			// メールアドレス、255文字以内であるかチェック
			if (user.getEmail().length() > 255) {
				errors.add("255文字を超えるメールアドレスを登録することができません");
			}
			
			// パスワード入力チェック
			if (!StringUtils.hasLength(user.getPassword())) {
				errors.add("パスワードを入力してください");
			}
			
			// password - byte数255以下であるかの判定
			if(user.getEmail().getBytes(StandardCharsets.UTF_8).length > 255) {
	        	errors.add("半角の文字列であれば255文字、全角の文字列ならば85文字を超えるパスワードを登録することができません");
	        } else if (user.getPassword().length() > 255) {
	        	// パスワード、255文字以内であるかチェック
				errors.add("255文字を超えるパスワードを登録することができません");
			}
			
			// パスワード(確認)入力チェック
			if (!StringUtils.hasLength(confirmPassword)) {
				errors.add("パスワード(確認)を入力してください");
			}
			
			if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
				if (user.getPassword().length() < 4) {
					errors.add("パスワードは4文字以上で設定してください");
				}
				
				if (!user.getPassword().equals(confirmPassword)) {
					errors.add("パスワードが一致しません");
				}
			}
		}
		
		return errors;
	}
}
