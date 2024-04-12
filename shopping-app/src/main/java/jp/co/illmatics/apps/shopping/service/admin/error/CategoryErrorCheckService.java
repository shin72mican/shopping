package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoryErrorCheckService {
	
	public List<String> errorCheck(String name, Long orderNo) {
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(name)){
			errors.add("名前を入力してください");
		}
			
		if (orderNo == null) {
			errors.add("並び順番号を入力してください");
		} else if(orderNo <= 0) {
			errors.add("1以上の並び順番号を入力してください");
		}
		
		return errors;
	}
	
}
