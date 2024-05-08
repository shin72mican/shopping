package jp.co.illmatics.apps.shopping.service.admin.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.illmatics.apps.shopping.entity.Users;

@Service
public class UserImageService {
	
	// 画像ファイル保存後、ファイルパスを返す
	public String saveImage(MultipartFile userImage) throws IOException {
		// 一意な画像ファイル名の作成
		// ファイル名取得
		String originalFileName = userImage.getOriginalFilename();
		// ファイル拡張子取得
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		// 一意な文字列取得
		UUID uuid = UUID.randomUUID();
		// 新しいファイル名
		String fileName = uuid.toString() + extension;
		// 保存先
		Path filePath=Paths.get("static/users/images/" + fileName);
		// 保存
		Files.copy(userImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		return "/users/images/" + fileName;
	}
	
	public void delete(Users user) {
		
		// 画像の削除
		if(Objects.nonNull(user.getImagePath())) {
			// fileパス
			String staticDirPath = "static";
			Path deleteFilePath = Paths.get(staticDirPath, user.getImagePath());
			File fileToDelete = deleteFilePath.toFile();
			
			// ファイルを削除
			boolean isDeleted = fileToDelete.delete();
            if (isDeleted) {
                System.out.println(user.getImagePath() + " を削除しました。");
            } else {
                System.out.println(user.getImagePath() + " の削除に失敗しました。");
            }
		}
	}
}
