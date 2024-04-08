package jp.co.illmatics.apps.shopping.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jp.co.illmatics.apps.shopping.entity.Admins;

public class AdminUserDetails implements UserDetails {
	
//	private final Users users;
	private final Admins admin;
	private final Collection <? extends GrantedAuthority> authorities;
	
	public AdminUserDetails(Admins admin) {
		super();
		String[] array = {"ROLE_ADMIN"};
		this.admin = admin;
		this.authorities = Arrays.stream(array)
			.map(role -> new SimpleGrantedAuthority(role))
			.toList();
	}

	public Admins getAdmin() {
		return admin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 実装
		return authorities;
	}

	@Override
	public String getPassword() {
		return admin.getPassword();
	}

	@Override
	public String getUsername() {
		return admin.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
