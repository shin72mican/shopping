package jp.co.illmatics.apps.shopping.service.admin.error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Users;
import jp.co.illmatics.apps.shopping.mapper.UsersMapper;

@Service
public class UserErrorCheckService {
	
	@Autowired
	UsersMapper usersMapper;
	
	// 新規・更新共通エラーチェック
	public List<String> errorCheck(Users user, String confirmPassword, MultipartFile userImage) {
		List<String> errors = new ArrayList<String>();
		
		if(!StringUtils.hasLength(user.getName())) {
			errors.add("名前を入力してください");
		}
		
        if(user.getName().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば127文字を超える名前を登録することができません");
        } else if(user.getName().length() > 255) {
			errors.add("255文字を超える名前を登録することができません");
		}
		
		if(!StringUtils.hasLength(user.getEmail())) {
			errors.add("メールアドレスを入力してください");
		} else {
			if(!user.getEmail().matches("^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,}$")) {
				errors.add("メールアドレスの形式が異なります(例: example@test.com)");
			}
		}
		
		if(user.getEmail().length() > 255) {
			errors.add("255文字を超えるメールアドレスを登録することができません");
		}
		
		if(!StringUtils.hasLength(user.getPassword())) {
			errors.add("パスワードを入力してください");
		}
		
		if(!StringUtils.hasLength(confirmPassword)) {
			errors.add("パスワード(確認)を入力してください");
		}
		
		if(user.getPassword().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば127文字を超えるパスワードを登録することができません");
        } else if(user.getPassword().length() > 255) {
        	errors.add("255文字を超えるパスワードを登録することができません");
		}
		
		if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
			if(!ObjectUtils.nullSafeEquals(user.getPassword(), confirmPassword)) {
				errors.add("パスワードが一致しません");	
			}
			
			if(user.getPassword().length() < 4) {
				errors.add("パスワードは4文字以上で設定してください");
			}
			
		}
		
		if(!userImage.isEmpty()) {
			// 一意な画像ファイル名の作成
			// ファイル名取得
			String originalFileName = userImage.getOriginalFilename();
			// ファイル拡張子取得
			String extension;
			try {
				extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			} catch(StringIndexOutOfBoundsException e) {
				extension = "";
			}
			
			// 画像タイプエラーチェック
			boolean extendionCheck = extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png");
			
			if (!extendionCheck) {
				errors.add("" + originalFileName + " " + "を保存することができません。"
						+ "jpg, jpeg, pngの形式のファイルのいずれかを保存することができます。");	
			}
		}
		
		return errors;
	}
	
	// 新規エラーチェック
	public List<String> createErrorCheck(Users user, String confirmPassword, MultipartFile userImage) {
		List<String> errors = new ArrayList<String>();
		List<Users> users = usersMapper.findEmail(user);
		
		if(users.size() > 0) {
			errors.add("メールアドレスは既に登録されています");
		} else {
			// 新規・編集共通エラー
			errors = errorCheck(user, confirmPassword, userImage);
			
			if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
				if(!ObjectUtils.nullSafeEquals(user.getPassword(), confirmPassword)) {
					errors.add("パスワードが一致しません");	
				}
				
				if(user.getPassword().length() < 4) {
					errors.add("パスワードは4文字以上で設定してください");
				}
			}
			
		}
		
		return errors;
	}
	
	// 編集エラーチェック
	public List<String> editErrorCheck(Users user, String confirmPassword, MultipartFile userImage) {
		List<String> errors = new ArrayList<String>();
		List<Users> users = usersMapper.findEmail(user);
		
		// 自身が登録したemailであるかのチェック
		if(users.size() > 0) {
			if(!Objects.equals(users.get(0).getId(), user.getId())) {
				errors.add("メールアドレスは既に登録されています");
			} else {
				errors = errorCheck(user, confirmPassword, userImage);
				if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
					if(!ObjectUtils.nullSafeEquals(user.getPassword(), confirmPassword)) {
						errors.add("パスワードが一致しません");	
					}
					
					if(user.getPassword().length() < 4) {
						errors.add("パスワードは4文字以上で設定してください");
					}
				}
			}
		} else {
			errors = errorCheck(user, confirmPassword, userImage);
			if(StringUtils.hasLength(user.getPassword()) && StringUtils.hasLength(confirmPassword)) {
				if(!ObjectUtils.nullSafeEquals(user.getPassword(), confirmPassword)) {
					errors.add("パスワードが一致しません");	
				}
				
				if(user.getPassword().length() < 4) {
					errors.add("パスワードは4文字以上で設定してください");
				}
			}
		}
		
		return errors;
	}
}
