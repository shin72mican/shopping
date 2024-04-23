package jp.co.illmatics.apps.shopping.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@Component
@SessionScope
@Data
public class AdminAccount {
	
	private Long id;
	
	private String name;
	
	private String email;
	
	private Long isOwner;
	
}
