package jp.co.illmatics.apps.shopping.service.admin.url;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AdminUrlService {
	
	public String searchQueryUrl(String name, String email, String authority, String sortType, String sortDirection, Integer displayCount, Integer currentPage) {
		String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		URI url = UriComponentsBuilder
		        .fromUriString(uri)
		        .queryParam("name", name)
		        .queryParam("email", email)
		        .queryParam("authority", authority)
		        .queryParam("sort_type", sortType)
		        .queryParam("sort_direction", sortDirection)
		        .queryParam("display_count", displayCount)
		        .build()
		        .encode()
		        .toUri();
		return url.toString();
	}
}
