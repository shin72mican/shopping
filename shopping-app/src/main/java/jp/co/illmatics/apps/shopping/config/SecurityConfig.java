package jp.co.illmatics.apps.shopping.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	/*
		@Autowired
		private UserDetailsService userDetailsService;
	*/
	
	// パスワードハッシュ化
	@Bean	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Configuration
	@Order(2)
	public static class UsersConfig {
		
//		// パスワードハッシュ化
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
//		@Autowired
//		private CustomerAbstractUserDetails
		
		@Bean
		InMemoryUserDetailsManager userDetailsService() {
		    UserDetails admin = User
		        .withUsername("user")
		        .password(passwordEncoder().encode("user1234"))
		        .roles("USERS")
		        .build();
		    return new InMemoryUserDetailsManager(admin);
		}

		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			System.out.println("test2");
		http.formLogin(login -> login // フォーム認証の設定記述開始
				.loginProcessingUrl("/users/login") // ユーザー名・パスワードの送信先URL
				.loginPage("/users/login") // ログイン画面のURL
				.failureUrl("/users/login") // ログイン失敗後のリダイレクト先URL
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/users/home", true) // ログイン成功後のリダイレクト先URL
			).logout(logout -> logout // ログアウトの設定記述開始
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logout") // ログアウト成功後のリダイレクト先URL
			).authorizeHttpRequests(authz -> authz // URLごとの認可設定記述開始
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/sample").permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/users/login").permitAll()
				.requestMatchers("/admin/login").permitAll()
				.requestMatchers("/api/sample").permitAll()
				.requestMatchers("/users/**").hasRole("USERS")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated() // 他のURLはログイン後のみアクセス可能
			);
		System.out.println("test3");
			return http.build();
		}
	}
	
	@Configuration
	@Order(1)
	public static class AdminConfig {
		
		// パスワードハッシュ化
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
//		@Bean
//		public AdminAbstractUserDetailsAuthenticationProvider getAuthenticationProvider() {
//		    return new AdminAbstractUserDetailsAuthenticationProvider();
//		}
		
		@Bean
		InMemoryUserDetailsManager userDetailsService() {
		    UserDetails admin = User
		        .withUsername("admin")
		        .password(passwordEncoder().encode("admin1234"))
		        .roles("ADMIN")
		        .build();
		    return new InMemoryUserDetailsManager(admin);
		}
		
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			System.out.println("test1");
		http.formLogin(login -> login
				.loginProcessingUrl("/admin/login")
				.loginPage("/admin/login")
				.failureUrl("/admin/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/admin/home", true)
			).logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logout")
			).authorizeHttpRequests(authz -> authz
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/admin/login").permitAll()
				.requestMatchers("/sample").permitAll()
				.requestMatchers("/api/sample").permitAll()
				.requestMatchers("/users/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			);
		System.out.println("test4");
			return http.build();
		}
	}

}