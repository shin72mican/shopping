package jp.co.illmatics.apps.shopping.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.co.illmatics.apps.shopping.values.Page;

@Service
public class PageService {
	
	public void indexPaging(Model model, int pageCount, int displayCount, int currentPage) {
		int totalPage = (pageCount - 1) / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        return;
	}
}
