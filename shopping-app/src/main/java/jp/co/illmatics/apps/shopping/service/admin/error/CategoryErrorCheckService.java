package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Service
public class CategoryErrorCheckService {
	
	@Autowired
	CategoriesMapper categoriesMapper;
	
	public List<String> errorCheck(Categories category, String formOrderNo) {
		
		List<String> errors = new ArrayList<String>();
		
		List<Categories> checkCategories = categoriesMapper.find(category);
		if (checkCategories.size() > 0 &&  Objects.nonNull(category.getOrderNo()) && category.getOrderNo() >= 0) {
			errors.add("指定された並び順番号は既に存在します");
		} else {
			if (!StringUtils.hasLength(category.getName())){
				errors.add("名前を入力してください");
			}
			
			// 並び順番号が入力されているかの判定
			if(!StringUtils.hasLength(formOrderNo)) {
				errors.add("並び順番号を入力してください");
			}
			
			// 入力された並び順が数値であるかの判定
			if(StringUtils.hasLength(formOrderNo) && Objects.isNull(category.getOrderNo())) {
				
				errors.add("並び順番号は整数でしか登録することができません");
			}
			
			if(StringUtils.hasLength(formOrderNo) && Objects.nonNull(category.getOrderNo()) && category.getOrderNo() <= 0) {
				errors.add("1以上の並び順番号を入力してください");
			}
			
		}
		
		return errors;
	}
	
}
