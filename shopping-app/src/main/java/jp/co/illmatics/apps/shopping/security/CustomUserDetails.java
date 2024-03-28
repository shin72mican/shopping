//package jp.co.illmatics.apps.shopping.security;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import jp.co.illmatics.apps.shopping.entity.Users;
//
//public class CustomUserDetails implements UserDetails {
//
//	private final Users users;
//	
//	public CustomUserDetails(Users user) {
//		super();
//		this.users = user;
//	}
//
//	public Users getUser() {
//		return users;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
//		// TODO 実装
//		return authorities;
//	}
//
//	@Override
//	public String getPassword() {
//		return users.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return users.getEmail();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//
//}