package jp.co.illmatics.apps.shopping.service.admin.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Service
public class CategoryErrorCheckService {
	
	@Autowired
	CategoriesMapper categoriesMapper;
	
	public List<String> errorCheck(Categories category) {
		
		List<String> errors = new ArrayList<String>();
		
		List<Categories> checkCategories = categoriesMapper.find(category);
		if (checkCategories.size() > 0 && category.getOrderNo() != null && category.getOrderNo() >= 0) {
			errors.add("指定された並び順番号は既に存在します");
		} else {
			if (!StringUtils.hasLength(category.getName())){
				errors.add("名前を入力してください");
			}
				
			if (category.getOrderNo() == null) {
				errors.add("並び順番号を入力してください");
			} else if(category.getOrderNo() <= 0) {
				errors.add("1以上の並び順番号を入力してください");
			}
		}
		
		return errors;
	}
	
}
