package com.biz.sec.domain;

import java.util.Collection;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Spring Security와 연동하여 회원정보를 관리하기 위한 
 * UserVO는 단독으로 작성하지 않고
 * 
 * UserDetail이라는 Interface를 implements하여 작성한다.
 * 
 * updated 2020-04-10
 * User class를 상속받아 만든 userVO를 UserDetails 인터페이스를 implements한 UserDetailsVO로 변경
 * 
 * UserDetails interface를 implements한 UserDetailsVO로 변경
 * 
 * UserDetails를 implements하면 method를 Ovveride하라는 지시가 뜬다.
 * 하지만, method를 Ovverride하지 않고
 * 필드변수들을 선언하고 lombok의 getter와 setter를 선언해준다.
 * 
 * 여기서 만든 UserDetailsVO는 Spring Security와 연동하여 사용자 정보를 관리할 클래스가 되고,
 * 
 * 필요에 따라서 UserVO와 연동하여 데이터를 주고받기를 수행할 것이다.
 * 
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsVO implements UserDetails{
	
	/**
	 * vo객체를 map에 담아서 req, res에 실어서 보낼 대
	 * 객체를 문자열형으로 변환하는 과정이 있다.
	 * 이 과정을 serialize라고 하는데, 각 변환된 문자열이 서로 흐트러지지 않게 하도록 설정하는 Id
	 */
	private static final long serialVersionUID = 1L;  // add default serialId
	
	private long id;
	private String username;
	private String password;
	
	// 권한관련
	private boolean enabled;
	
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	 
	// 권한관련
	private Collection<? extends GrantedAuthority> authorities;

	private String email;
	private String phone;
	private String address;
	
}
