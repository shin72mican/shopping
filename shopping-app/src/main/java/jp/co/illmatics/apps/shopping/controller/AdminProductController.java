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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.illmatics.apps.shopping.entity.Categories;
import jp.co.illmatics.apps.shopping.entity.Products;
import jp.co.illmatics.apps.shopping.mapper.CategoriesMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductReviewsMapper;
import jp.co.illmatics.apps.shopping.mapper.ProductsMapper;
import jp.co.illmatics.apps.shopping.mapper.WishProductsMapper;
import jp.co.illmatics.apps.shopping.service.admin.error.ProductErrorCheckService;
import jp.co.illmatics.apps.shopping.service.admin.image.ProductImageService;
import jp.co.illmatics.apps.shopping.service.admin.url.ProductUrlService;
import jp.co.illmatics.apps.shopping.values.Page;
import jp.co.illmatics.apps.shopping.values.form.Display;
import jp.co.illmatics.apps.shopping.values.form.SortDirection;
import jp.co.illmatics.apps.shopping.values.form.products.SortType;

@Controller
public class AdminProductController {
	@Autowired
	ProductUrlService urlService;
	
	@Autowired
	ProductImageService imageService;
	
	// 新規・更新エラーチェック
	@Autowired
	ProductErrorCheckService errorCheckService;
	
	// DB処理
	@Autowired
	private ProductsMapper productsMapper;
	
	@Autowired
	private CategoriesMapper categoriesMapper;
	
	@Autowired
	private ProductReviewsMapper productReviewsMapper;
	
	@Autowired
	private WishProductsMapper wishProductsMapper;
	
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
		
		String url = urlService.searchUrl(categoryId, name, price, standard, sortType, sortDirection, displayCount);
		
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
		
		Products product = new Products(categoryId, name, price, description);
		
		// エラーチェック
		List<String> errors = new ArrayList<String>();
		

		errors = errorCheckService.errorCheck(product);
		
		model.addAttribute("errors", errors);
		
		if(!productImage.isEmpty()) {
			// 画像保存処理
			product = imageService.saveImage(productImage, product);
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
	
	@GetMapping("/admin/products/{id}/edit")
	public String edit(
			@PathVariable(name = "id") Long id,
			Model model) {
		Products product = new Products(id);
		List<String> errors = new ArrayList<String>();
		List<Categories> categories = categoriesMapper.findAll();
		List<Products> products = productsMapper.find(product);
		
		model.addAttribute("errors", errors);
		model.addAttribute("categories", categories);
		model.addAttribute("product", products.get(0));
		return "/admin/products/edit";
	}
	
	@PutMapping("/admin/products/{id}")
	public String update(
			@PathVariable("id") Long id,
			@RequestParam(name = "category_id", defaultValue = "0") Long categoryId,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "price", required = false) Long price,
			@RequestParam(name = "description", defaultValue = "") String description,
			@RequestParam(name = "product_image", defaultValue="") MultipartFile productImage,
			@RequestParam(name = "delete_check", defaultValue = "false") Boolean deleteCheck,
			Model model) throws IOException {
		Products product = new Products(id, categoryId, name, description);
		List<Products> products = productsMapper.find(product);
		
		// エラーチェック
		List<String> errors = new ArrayList<String>();
		
		errors = errorCheckService.errorCheck(product);
		
		model.addAttribute("errors", errors);
		
		if(!productImage.isEmpty() && errors.size() == 0) {
			// 画像の削除
			imageService.delete(products.get(0));
			
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
			
			products.get(0).setImagePath("/products/" + fileName);
		} else if(deleteCheck) {
			// 画像の削除
			imageService.delete(products.get(0));
            products.get(0).setImagePath("");
		}
		
		if(errors.size() > 0) {
			List<Categories> categories = categoriesMapper.findAll();
			model.addAttribute("errors", errors);
			model.addAttribute("categories", categories);
			model.addAttribute("product", product);
			return "admin/products/edit";
		} else {
			productsMapper.update(products.get(0));
			return "redirect:/admin/products";
		}
	}
	
	@DeleteMapping("/admin/products/{id}")
	public String delete(@PathVariable("id") Long id) {
		
		Products product = new Products(id);
		List<Products> products = productsMapper.find(product);
		
		// 画像の削除
		imageService.delete(products.get(0));
        
        // 関連レビュー削除
        productReviewsMapper.productsDelete(products.get(0));
        // 関連ほしいもの削除
        wishProductsMapper.productsDelete(products.get(0));
        // 商品削除
        productsMapper.delete(products.get(0));
		
		return "redirect:/admin/products";
	}
}




