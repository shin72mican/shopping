package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Products;

@Service
public class ProductErrorCheckService {
	
	public List<String> errorCheck(Products product) {
		
		
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(product.getName())) {
			errors.add("名前を入力してください");
		}
		
		if (product.getPrice() == null) {
			errors.add("価格を入力してください");
		} else {
			if (product.getPrice() < 0) {
				errors.add("0以上で価格を入力して下さい");
			}
		}
		
		return errors;
	}
}
