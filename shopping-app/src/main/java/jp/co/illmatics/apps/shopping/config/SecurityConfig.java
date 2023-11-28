package jp.co.illmatics.apps.shopping.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	/*	@Autowired
		private UserDetailsService userDetailsService;
	*/
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
			.loginProcessingUrl("/admin/login")
			.loginPage("/admin/login")
			.failureUrl("/error")
			.usernameParameter("email")
			.passwordParameter("password")
			.defaultSuccessUrl("/admin/home", true)
		).logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login?logout")
		).authorizeHttpRequests(authz -> authz
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			.requestMatchers("/").permitAll()
			.requestMatchers("/sample").permitAll()
			.requestMatchers("/api/sample").permitAll()
			.requestMatchers("/general").hasRole("GENERAL")
			.requestMatchers("/admin").hasRole("ADMIN")
			.anyRequest().authenticated()
		);
		return http.build();
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		PasswordEncoder encoder = passwordEncoder();
//		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
//	}
}