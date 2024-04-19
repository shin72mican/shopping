package jp.co.illmatics.apps.shopping.service.admin.image;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jp.co.illmatics.apps.shopping.entity.Users;

@Service
public class UserImageService {
	
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
