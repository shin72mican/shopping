package jp.co.illmatics.apps.shopping.service.user.url;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserProductUrlService {
	public String searchUrl(Long categoryId, String name) {
		String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
		URI url = UriComponentsBuilder
		        .fromUriString(uri)
		        .queryParam("category_id", categoryId)
		        .queryParam("name", name)
		        .build()
		        .encode()
		        .toUri();
		return url.toString();
	}
}
