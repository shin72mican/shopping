package jp.co.illmatics.apps.shopping.service.admin.url;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CategoryUrlService {
	
	// 検索 - クエリパラメータ作成
	public String searchUrl(String name, String sortType, String sortDirection, Integer displayCount) {
		String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		URI url = UriComponentsBuilder
		        .fromUriString(uri)
		        .queryParam("name", name)
		        .queryParam("sort_type", sortType)
		        .queryParam("sort_direction", sortDirection)
		        .queryParam("display_count", displayCount)
		        .build()
		        .encode()
		        .toUri();
		return url.toString();
	}
	
	// id - パスパラメータ作成
	public String idUrl(Long id) {
		String url = "admin/categories/" + id.toString();
		return url;
	}
}
