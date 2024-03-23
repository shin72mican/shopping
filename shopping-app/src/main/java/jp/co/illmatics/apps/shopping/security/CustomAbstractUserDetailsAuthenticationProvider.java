//package jp.co.illmatics.apps.shopping.security;
//
//import java.util.Objects;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import jp.co.illmatics.apps.shopping.entity.Users;
//import jp.co.illmatics.apps.shopping.mapper.UsersMapper;
//
//public class CustomAbstractUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
//	
//	@Autowired
//	public UsersMapper usersMapper;
//	
//	@Autowired
//	public BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	@Override
//	protected void additionalAuthenticationChecks(UserDetails userDetails,
//			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//		// no processes
//	}
//
//	@Override
//	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
//			throws AuthenticationException {
//		String password = (String) authentication.getCredentials();
//		Users user = usersMapper.findByName(username).orElse(null);
//		if (Objects.nonNull(user) && bCryptPasswordEncoder.matches(password, user.getPassword())) {
//			return new CustomUserDetails(user);
//		} else {
//			throw new UsernameNotFoundException("user not found");
//		}
//	}
//
//}