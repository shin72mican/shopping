package jp.co.illmatics.apps.shopping.service.admin.error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Products;

@Service
public class ProductErrorCheckService {
	
	public List<String> errorCheck(Products product, MultipartFile productImage, String formPrice) {
		
		
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(product.getName())) {
			errors.add("名前を入力してください");
		}
		
		if(product.getName().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば127文字を超える名前を登録することができません");
        } else if(product.getName().length() > 255) {
			errors.add("255文字を超える名前を登録することができません");
		}
		
		if (!StringUtils.hasLength(formPrice)) {
			errors.add("価格を入力してください");
		}
		Pattern pattern = Pattern.compile("^[-1-9][0-9]*$");
		Matcher matcher = pattern.matcher(formPrice);
		
		if(matcher.find()) {
			// 価格の桁数が11以内であるか
			if(formPrice.length() > 11) {
				errors.add("価格は11桁までしか登録することができません");
			}
		} else {
			// 入力された並び順が数値であるかの判定
			errors.add("価格は整数でしか登録することができません");
		}
		
		if (StringUtils.hasLength(formPrice) && Objects.nonNull(product.getPrice()) && product.getPrice() < 0) {
			errors.add("0以上で価格を入力して下さい");
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
