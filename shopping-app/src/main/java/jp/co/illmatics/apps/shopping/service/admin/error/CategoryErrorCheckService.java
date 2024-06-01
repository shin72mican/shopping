package jp.co.illmatics.apps.shopping.service.admin.error;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;

@Service
public class CategoryErrorCheckService {
	
	@Autowired
	CategoriesMapper categoriesMapper;
	
	// 新規登録・編集 - 共通
	public List<String> errorCheck(Categories category, String formOrderNo) {
		
		List<String> errors = new ArrayList<String>();
		
		if(category.getName().getBytes(StandardCharsets.UTF_8).length > 255) {
        	errors.add("半角の文字列であれば255文字、全角の文字列ならば85文字を超える名前を登録することができません");
        } else if (!StringUtils.hasLength(category.getName())){
			errors.add("名前を入力してください");
		}
		
		// 並び順番号が入力されているかの判定
		if(!StringUtils.hasLength(formOrderNo)) {
			errors.add("並び順番号を入力してください");
		}
		
		Pattern pattern = Pattern.compile("^[-1-9][0-9]*$");
		Matcher matcher = pattern.matcher(formOrderNo);
		
		// 数値であるかどうか
		if(matcher.find()) {
			// 価格の桁数が11以内であるか
			if(formOrderNo.length() > 11) {
				errors.add("価格は11桁までしか登録することができません");
			}
		} else {
			// 入力された並び順が数値であるかの判定
			errors.add("並び順番号は整数でしか登録することができません");
		}
		
		if(StringUtils.hasLength(formOrderNo) && Objects.nonNull(category.getOrderNo()) && category.getOrderNo() <= 0) {
			errors.add("1以上の並び順番号を入力してください");
		}
		
		return errors;
	}
	
	// 新規作成エラーチェック
	public List<String> createErrorCheck(Categories category, String formOrderNo) {
		List<String> errors = new ArrayList<String>();
		
		Categories findCategory = new Categories();
		findCategory.setOrderNo(category.getOrderNo());
		
		List<Categories> checkCategories = categoriesMapper.find(findCategory);
		if (checkCategories.size() > 0 &&  Objects.nonNull(category.getOrderNo()) && category.getOrderNo() >= 0) {
			errors.add("指定された並び順番号は既に存在します");
		} else {
			errors = errorCheck(category, formOrderNo);
		}
		
		return errors;
	}
	
	// 編集エラーチェック
	public List<String> editErrorCheck(Categories category, String formOrderNo) {
		List<String> errors = new ArrayList<String>();
		
		Categories findCategory = new Categories();
		findCategory.setOrderNo(category.getOrderNo());
		
		List<Categories> checkCategories = categoriesMapper.find(findCategory);
		if (checkCategories.size() > 0 && Objects.nonNull(category.getOrderNo())) {
			if(!(checkCategories.get(0).getId().equals(category.getId()))) {
				errors.add("指定された並び順番号は既に存在します");
			} else {
				errors = errorCheck(category, formOrderNo);
			}
		} else {
			errors = errorCheck(category, formOrderNo);
		}
		
		return errors;
	}
	
}
