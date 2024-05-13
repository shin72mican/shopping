package jp.co.illmatics.apps.shopping.service.admin.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Products;

@Service
public class ProductImageService {
	
	// 画像ファイル保存後、ファイルパスを返す
	public String saveImage(MultipartFile productImage, Products product) throws IOException {
		// 一意な画像ファイル名の作成
		// ファイル名取得
		String originalFileName = productImage.getOriginalFilename();
		// ファイル拡張子取得
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		// 一意な文字列取得
//		UUID uuid = UUID.randomUUID();
		// 新しいファイル名
//		String fileName = uuid.toString() + extension;
		long millTime = System.currentTimeMillis();
		String fileName = "products_" + millTime + extension;
		// 保存先
		Path filePath=Paths.get("static/products/" + fileName);
		// 保存
		Files.copy(productImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		return "/products/" + fileName;
	}
	
	public void delete(Products product) {
		// 画像の削除
		if(Objects.nonNull(product.getImagePath())) {
			// fileパスの作成
			String staticDirPath = "static";
			Path deleteFilePath = Paths.get(staticDirPath, product.getImagePath());
			File fileToDelete = deleteFilePath.toFile();
			// ファイルを削除
            boolean isDeleted = fileToDelete.delete();
            if (isDeleted) {
                System.out.println(product.getImagePath() + " を削除しました。");
            } else {
                System.out.println(product.getImagePath() + " の削除に失敗しました。");
            }
		}
	}
}
