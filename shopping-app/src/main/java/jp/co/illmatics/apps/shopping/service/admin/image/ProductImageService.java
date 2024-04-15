package jp.co.illmatics.apps.shopping.service.admin.image;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jp.co.illmatics.apps.shopping.entity.Products;

@Service
public class ProductImageService {
	
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
