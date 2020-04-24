package com.biz.sec.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.biz.sec.domain.UserDetailsVO;

// login을 customizing할 때 가장 중요하게 설계해야하는 class(가장 마지막에 작성함)

// security-context에 bean으로 등록해주고 provider ref로 사용해주면 됨
// <bean id="authProvider" class="com.biz.sec.service.auth.AuthenticationProviderImpl"></bean>
public class AuthenticationProviderImpl implements AuthenticationProvider{

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDService;

	// security-context에 bean으로 등록되어 있는 passwordEncoder;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * spring security 에서 customizing을 수행하여 
	 * 로그인을 세세히 제어하고자 할 때
	 * 코드를 작성해야 하는 중요한 method
	 * 
	 * login을 수행할 경우 id와 password정보는 authentication에 담겨온다
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		
		// authentication으로부터 로그인 폼에서 보낸 username과 password를 추출한 후 문자열로 형변환
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		
		// Service -> Dao를 통해서 DB로부터 사용자 정보 가져오기
		// user정보에는 암호화된 값이 담겨있고
		// password에는 암호화 되지 않는 값이 담겨있음
		UserDetailsVO userVO = (UserDetailsVO) userDService.loadUserByUsername(username);
		
		// 암호화x비밀번호와 암호화o비밀번호를 비교
		if(!passwordEncoder.matches(password.trim(), userVO.getPassword().trim())) {
			throw new BadCredentialsException("비밀번호 오류");
		}
		
		// enabled가 false이면 사용금지된 username
		if(!userVO.isEnabled()) {
			throw new BadCredentialsException(username + "접근권한 없음");
		}
		
		// UserDetailsService에서 보내준 사용자 정보를 Controller로 보내는 일을 수행(가장 중요)
		// user.getAuthorities()에는 Role list가 담겨있음(중요중요)
		return new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities() );
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
