package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Products;

@Service
public class ProductErrorCheckService {
	
	public List<String> errorCheck(Products product, MultipartFile productImage) {
		
		
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(product.getName())) {
			errors.add("名前を入力してください");
		}
		
		if(product.getName().length() > 255) {
			errors.add("255文字を超える名前を登録することができません");
		}
		
		if (product.getPrice() == null) {
			errors.add("価格を入力してください");
		} else {
			if (product.getPrice() < 0) {
				errors.add("0以上で価格を入力して下さい");
			}
		}
		
		if (product.getDescription().length() > 255) {
			errors.add("255文字を超える説明を登録することができません");
		}
		
		if(!productImage.isEmpty()) {
			// ファイル名取得
			String originalFileName = productImage.getOriginalFilename();
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
}
