//package jp.co.illmatics.apps.shopping.config;
//
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//	/*
//		@Autowired
//		private UserDetailsService userDetailsService;
//	*/
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Configuration
//	@Order(1)
//	public static class UsersConfig {
//
//		@Bean
//		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.formLogin(login -> login
//				.loginProcessingUrl("/users/login")
//				.loginPage("/users/login")
//				.failureUrl("/users/login")
//				.usernameParameter("email")
//				.passwordParameter("password")
//				.defaultSuccessUrl("/users/home", true)
//			).logout(logout -> logout
//				.logoutUrl("/logout")
//				.logoutSuccessUrl("/logout")
//			).authorizeHttpRequests(authz -> authz
//				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//				.requestMatchers("/").permitAll()
//				.requestMatchers("/sample").permitAll()
//				.requestMatchers("/api/sample").permitAll()
//				.requestMatchers("/users").hasRole("USERS")
//				.requestMatchers("/admin").hasRole("ADMIN")
//				.anyRequest().authenticated()
//			);
//			return http.build();
//		}
//	}
//
//	@Configuration
//	@Order(2)
//	public static class AdminConfig {
//
//		@Bean
//		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.formLogin(login -> login
//				.loginProcessingUrl("/admin/login")
//				.loginPage("/admin/login")
//				.failureUrl("/admin/login")
//				.usernameParameter("email")
//				.passwordParameter("password")
//				.defaultSuccessUrl("/admin/home", true)
//			).logout(logout -> logout
//				.logoutUrl("/logout")
//				.logoutSuccessUrl("/logout")
//			).authorizeHttpRequests(authz -> authz
//				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//				.requestMatchers("/").permitAll()
//				.requestMatchers("/sample").permitAll()
//				.requestMatchers("/api/sample").permitAll()
//				.requestMatchers("/users").hasRole("USER")
//				.requestMatchers("/admin").hasRole("ADMIN")
//				.anyRequest().authenticated()
//			);
//			return http.build();
//		}
//	}
//}