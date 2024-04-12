package jp.co.illmatics.apps.shopping.service.admin.url;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProductUrlService {
	
	// 
	public String searchUrl(Long categoryId, String name, Long price, String standard, String sortType, String sortDirection, Integer displayCount) {
		String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		URI url = UriComponentsBuilder
		        .fromUriString(uri)
		        .queryParam("category_id", categoryId)
		        .queryParam("name", name)
		        .queryParam("price", price)
		        .queryParam("standard", standard)
		        .queryParam("sort_type", sortType)
		        .queryParam("sort_direction", sortDirection)
		        .queryParam("display_count", displayCount)
		        .build()
		        .encode()
		        .toUri();
		return url.toString();
	}
}
