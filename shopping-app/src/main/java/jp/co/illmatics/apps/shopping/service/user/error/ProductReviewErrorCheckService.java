package jp.co.illmatics.apps.shopping.service.user.error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.ProductReviews;

@Service
public class ProductReviewErrorCheckService {
	
	public List<String> errorCheck(ProductReviews productReview) {
		List<String> errors = new ArrayList<String>();
		
		if(!StringUtils.hasLength(productReview.getTitle())) {
			errors.add("タイトルを入力して下さい");
		}
		
		if(productReview.getTitle().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば127文字を超えるタイトルを登録することができません");
        } else if(productReview.getTitle().length() > 255) {
			errors.add("255文字を超えるタイトルを登録することができません");
		}
		
		if(!StringUtils.hasLength(productReview.getBody())) {
			errors.add("本文を入力してください");
		}
		
		if(productReview.getBody().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば127文字を超える本文を登録することができません");
        } else if(productReview.getBody().length() > 255) {
			errors.add("255文字を超える本文を登録することができません");
		}
		
		return errors;
	}
}
