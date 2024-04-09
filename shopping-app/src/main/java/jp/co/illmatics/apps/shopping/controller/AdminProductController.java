package jp.co.illmatics.apps.shopping.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.values.Page;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.products.SortType;

@Controller
public class AdminProductController {
	@Autowired
	private ProductsMapper productsMapper;
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
	@GetMapping("/admin/products")
	public String index(
			@RequestParam(name = "category_id", defaultValue = "0") Long categoryId,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "price", defaultValue = "") Long price,
			@RequestParam(name = "standard", defaultValue = "over") String standard,
			@RequestParam(name = "sort_type", defaultValue = "id") String sortType,
			@RequestParam(name = "sort_direction", defaultValue = "asc") String sortDirection,
			@RequestParam(name = "display_count", defaultValue = "10") int displayCount,
			@RequestParam(name = "page", defaultValue = "1") Integer currentPage,
			HttpServletRequest request,
			Model model) {
		
		List<Products> products = productsMapper.findSearch(categoryId, name, price, standard, sortType, sortDirection, displayCount, currentPage);
		List<Categories> categories = categoriesMapper.findAll();
		
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("name", name);
		model.addAttribute("price", price);
		model.addAttribute("standard", standard);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("displayCount", displayCount);
		
		// 検索
		model.addAttribute("typeList", SortType.values());
		model.addAttribute("sortList", SortDirection.values());
		model.addAttribute("countList", Display.values());
		
		model.addAttribute("products", products);
		model.addAttribute("categories", categories);
		
		String url = request.getRequestURL().toString() + "?category_id=" + categoryId + "&name=" + name + "&price=" + price + "&standard=" + standard + "&sort_type=" + sortType + "&sort_direction=" + sortDirection + "&display_count=" + displayCount;
		
		int totalPage = categories.size() / displayCount + 1;
		int startPage = currentPage - (currentPage - 1) % Page.COUNT.getValue();
		int endPage = (currentPage + Page.COUNT.getValue() - 1 > totalPage) ? totalPage : (currentPage + Page.COUNT.getValue() -1);
		
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
        model.addAttribute("url", url);
		
		return "admin/products/index";
	}
	
	@GetMapping("/admin/products/{id}")
	public String show(
			@PathVariable("id") Long id,
			Model model) {
		Products product = new Products(id);
		List<Products> products = productsMapper.find(product);
		
		model.addAttribute("product", products.get(0));
		
		return "admin/products/show";
	}
	
	@GetMapping("/admin/products/create")
	public String create(Model model) {
		List<Categories> categories = categoriesMapper.findAll();
		model.addAttribute("categories", categories);
		return "admin/products/create";
	}
	
	@PostMapping("/admin/products")
	public String store(
			@RequestParam(name = "category_id", defaultValue = "0") Long categoryId,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "price", defaultValue = "") Long price,
			@RequestParam(name = "description", defaultValue = "") String description,
			@RequestParam(name = "product_image", defaultValue="") MultipartFile productImage,
			Model model) throws IOException {
		
		Products product = new Products();
		
		// エラーチェック
		List<String> errors = new ArrayList<String>();
		
		if (!StringUtils.hasLength(name)) {
			errors.add("名前を入力してください");
		}
		
		if (price == null) {
			errors.add("価格を入力してください");
		} else {
			product = new Products(categoryId, name, price, description);
			if (price < 0) {
				errors.add("0以上で価格を入力して下さい");
			}
		}
		
		model.addAttribute("errors", errors);
		
		if(!productImage.isEmpty()) {
			// 一意な画像ファイル名の作成
			// ファイル名取得
			String originalFileName = productImage.getOriginalFilename();
			// ファイル拡張子取得
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			// 一意な文字列取得
			UUID uuid = UUID.randomUUID();
			// 新しいファイル名
			String fileName = uuid.toString() + extension;
			// 保存先
			Path filePath=Paths.get("static/products/" + fileName);
			// 保存
			Files.copy(productImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			product.setImagePath("/products/" + fileName);
		} else {
			product.setImagePath("");
		}
		
		if (errors.size() > 0) {
			List<Categories> categories = categoriesMapper.findAll();
			model.addAttribute("errors", errors);
			model.addAttribute("categories", categories);
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("name", name);
			model.addAttribute("price", price);
			model.addAttribute("description", description);
			return "/admin/products/create";
		} else {
			productsMapper.insert(product);
			return "redirect:/admin/products";
		}
	}
}




