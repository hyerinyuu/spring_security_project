package com.biz.sec.service;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.biz.sec.domain.AuthorityVO;
import com.biz.sec.domain.UserDetailsVO;
import com.biz.sec.persistance.AuthoritiesDao;
import com.biz.sec.persistance.UserDao;
import com.biz.sec.utils.PbeEncryptor;

import lombok.extern.slf4j.Slf4j;

// classdp @AutoWired Annotaion을 붙인것과 같은 역할
// 2020-04-14 생성자를 별도로 만드려고 required~~ 주석처리
// @RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

	// 변수를 private final로 설정하고 RequiredArgsConstructor Annotation을 설정해주면 된다.
	// security-context에 설정해놓은 passwordEncode를 가져와서 초기화해주는 생성자.(id값과 객체명을 똑같이 지정해줘야함.)
	private final PasswordEncoder passwordEncoder;
	private final UserDao userDao;
	private final AuthoritiesDao authDao;
	
	private final MailSendService mailService;
	
	@Autowired
	// 생성자부분에 테이블을 create하는 변수를 만들고 Dao의 create_table 호출
	public UserService(PasswordEncoder passwordEncoder, UserDao userDao, AuthoritiesDao authDao, MailSendService mailService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userDao = userDao;
		this.authDao = authDao;
		this.mailService = mailService;
		
		String create_table_users = " CREATE TABLE IF NOT EXISTS tbl_users( " + 
				"	id bigint PRIMARY KEY AUTO_INCREMENT, " + 
				"	user_name varchar(50) UNIQUE, " + 
				"   user_pass varchar(125), " + 
				"   enabled boolean DEFAULT TRUE, " +
				"  	email varchar(80), " +
				"   phone varchar(20), " +
				"   address varchar(125) " +
				" ) ";
		
		
		String create_table_auth = " CREATE TABLE IF NOT EXISTS authorities( " + 
				"	id bigint PRIMARY KEY AUTO_INCREMENT, " + 
				"   username varchar(50), " + 
				"   authority varchar(50) " + 
				" ) ";
		
		userDao.create_table(create_table_users);
		userDao.create_table(create_table_auth);
		
	}
	
	/**
	 * @since 2020-04-09
	 * @author hyerin.you@gmail.com
	 * 
	 * @param username
	 * @param password
	 * @return
	 * 
	 * 회원가입을 신청하면 비밀번호는 암호화하고,
	 * 아이디와 비밀번호를 DB Insert 수행
	 * 
	 * updated 2020-04-10
	 * Map String,String 구조의 VO 데이터를 UserVO로 변경
	 * 
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, // COMMIT이 수행되기 전에는 읽을 수 없음
			rollbackFor = Exception.class) // 모든 exception이 발생하면 rollback하라(rollback 조건)
	public int insert(String username, String password) {
		
		// 회원가입 form에서 전달받은 password값을 암호화시키는 과정
		String encPassword = passwordEncoder.encode(password);
		UserDetailsVO userVO = UserDetailsVO.builder().username(username).password(encPassword).build();
		
		int ret = userDao.insert(userVO);
		
		List<AuthorityVO> authList = new ArrayList<AuthorityVO>();
		authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_USER").build());
		authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("USER").build());
		authDao.insert(authList);
		
		return ret;
	}
	

	/**
	 * @since 2020-04-20
	 * @author hyerinyuu
	 *
	 * vo를 통채로 받아서
	 * 새로 작성된 회원가입에서 회원가입을 처리할 method
	 *  
	 * email인증방식으로 회원가입을 처리할 것이므로
	 * userVO를 파라메터로 받아서
	 * enabled를 false로 처리하고
	 * role 정보는 업데이트 하지 않는 것으로 처리해 놓는다.
	 * 
	 * 이후 email인증이 오면 enabled와 role정보를 설정하도록 한다.
	 *  
	 * @param userVO
	 * @return
	 */
	@Transactional
	public int insert(UserDetailsVO userVO) {

		String password = userVO.getPassword();
		// 회원가입 form에서 전달받은 password값을 암호화시키는 과정
		String encPassword = passwordEncoder.encode(password);
		
		// 회원정보에 저장할 준비가 되지만
		// 로그인을 했을 때 접근금지가 된 사용자가 된다.
		userVO.setPassword(encPassword);
		userVO.setEnabled(false);
		userVO.setAuthorities(null);
		
		// boolean bRet = mailService.join_send(userVO);
		String sRet;
		try {
			sRet = mailService.join_send(userVO);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ret = userDao.insert(userVO);
		return ret;
		
	}
	

	public boolean isExistsUserName(String username) {
		
		UserDetailsVO userDetailsVO = userDao.findByUserName(username);

		// 이미 DB에 회원정보(username)이 저장되어 있다.
		// userVO가 null이 아닌조건을 붙이지 않으면 nullpointException이 발생함
		if(userDetailsVO != null && userDetailsVO.getUsername().equalsIgnoreCase(username)) {
			return true;
		}
		return false;
	}

	public UserDetailsVO findById(long id) {

		UserDetailsVO userVO = userDao.findById(id);
		
		
		
		return userVO;
	}

	public boolean check_password(String password) {

		// login한 사용자의 비밀번호 뽑아내기
		// 이경우에는 본인만 수정이 가능하고
		// 관리자가 수정하기는 안됨
		UserDetailsVO userVO = (UserDetailsVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// 일치하면 true를 불일치하면 false를 return
		return passwordEncoder.matches(password, userVO.getPassword());
		
	}
	
	@Transactional
	public int update(UserDetailsVO userVO, String[] authList) {
		
		int ret = userDao.update(userVO);
		
		// 1. update가 성공하면
		if(ret > 0) {
			
			// 2. list 생성
			// authDao.update(new ArrayList(Arrays.asList(authList)));
			List<AuthorityVO> authCollection = new ArrayList();
			for(String auth : authList) {
				
				if(!auth.isEmpty()) {
					AuthorityVO authVO = AuthorityVO.builder().username(userVO.getUsername()).authority(auth).build();
					authCollection.add(authVO);
				}
				
			}
			// 3. 권한정보 수정
			authDao.delete(userVO.getUsername());
			authDao.insert(authCollection);
		}
		return ret;
	}

	/**
	 * MyPage Update용
	 * 
	 * @param userVO
	 * @return
	 */
	@Transactional
	public int update(UserDetailsVO userVO) {
		
		Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
		
		// session에서 username과  password, enabled, accountNonExpired 등등은 지금 update를 수행하지 않아도 되는 상황임
		// 따라서 email과 phone, address등만 다시 세팅을 해주고 
		// 그 외의 값은 유지를 해줘야함
		// 아니면 session에서 삭제되서 수정 후에 다시 재수정을 위해 비밀번호를 입력하면
		// 비밀번호가 틀렸다는 오류가 계속 발생함
		UserDetailsVO oldUserVO = (UserDetailsVO) oldAuth.getPrincipal();
		oldUserVO.setEmail(userVO.getEmail());
		oldUserVO.setPhone(userVO.getPhone());
		oldUserVO.setAddress(userVO.getAddress());
		
		int ret = userDao.update(oldUserVO);
		
		
		// update를 수행했는데도 view화면의 value가 변하지 않는 문제 발생
		// session에 value가 update되지 않은 탓
		// DB update가 성공하면 로그인된 session정보를 update 수행
		if(ret > 0) {
			// 새로운 session정보 만들기
			Authentication newAuth = new UsernamePasswordAuthenticationToken(oldUserVO, oldAuth.getCredentials(), oldAuth.getAuthorities());  
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
		
		return ret;
	}
	
	private Collection<GrantedAuthority> getAuthorities(String[] authList){
		
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(String auth : authList) {
			
			// 빈칸이 아닐경우에만 추가
			if(!auth.isEmpty()) {
				SimpleGrantedAuthority sAuth = new SimpleGrantedAuthority(auth);
				authorities.add(sAuth);
			}
			
		}
		return authorities;
	}

	public List<UserDetailsVO> selectAll() {
		return userDao.selectAll();
	}

	@Transactional
	public UserDetailsVO findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Transactional
	public boolean emailok(String username, String email) {
		
		// 복호화된 username과 Email을 가져와
		String strUserName = PbeEncryptor.getDecrypt(username);
		String strEmail = PbeEncryptor.getDecrypt(email);
		
		// findByUserName에 보내기
		UserDetailsVO userVO = userDao.findByUserName(strUserName);
		
		// 복호화된 email을 vo에 담겨있는 데이터와 비교하여 다시 한번 검사하기
		if(strEmail.equalsIgnoreCase(userVO.getEmail())) {
			
			// Enabled값 true로 세팅(메일 인증 완료됐으니까)
			userVO.setEnabled(true);

			userDao.update(userVO);
			
			// authority부분에 role 추가
			List<AuthorityVO> authList = new ArrayList<AuthorityVO>();
			authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_USER").build());
			authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("USER").build());
			authDao.insert(authList);
			
			return true;
			
		}
		
		return false;
		
		
	}

	/**
	 * @since 2020-04-21
	 * 회원정보를 받아서 DB에 저장하고
	 * 회원정보를 활성화 할 수 있도록 하기 위해
	 * 인증 정보를 생성한 후 
	 * controller로 return
	 * 
	 * @param userVO
	 * @return
	 */
	@Transactional
	public String insert_getToken(UserDetailsVO userVO) {
		//DB에 저장
		// 인증이 안되면 로그인을 할 수 없게 Enabled false로 먼저 세팅
		userVO.setEnabled(false);
		String encPassword = passwordEncoder.encode(userVO.getPassword());
		userVO.setPassword(encPassword);
		userDao.insert(userVO);

		/*
		email_token = UUID.randomUUID().toString();
		String[] _t = email_token.split("-");
		email_token = _t[0];
		email_token = email_token.toUpperCase();
		*/
		// 위의 코드를 method chaining을 이용해 한줄로 작성
		// UUID(ex:asdjf-sajfiw-fsjifa)를 '-'기준으로 split한 후 첫번째 값을 추출해 대문자로 바꾸기
		String email_token = UUID.randomUUID().toString().split("-")[0].toUpperCase();
		
		log.debug("EMAIL-TOKEN : " + email_token);
		String enc_email_token = PbeEncryptor.getEncrypt(email_token);

		
		// Email 보내기
		mailService.email_auth(userVO, email_token);
		
		return enc_email_token;
	}

	public boolean email_token_ok(String username, String secret_key, String secret_value) {

		boolean bKey = PbeEncryptor.getDecrypt(secret_key).equals(secret_value);
		
		if(bKey) {
			
			// username 다시 원래대로 복호화
			String strUserName = PbeEncryptor.getDecrypt(username);
			UserDetailsVO userVO = userDao.findByUserName(strUserName);
			userVO.setEnabled(true);
			userDao.update(userVO);
			
			// 혹시 모르니까 원래 데이터가 있으면 지워줌
			authDao.delete(userVO.getUsername());
			
			// authority부분에 role 추가
			List<AuthorityVO> authList = new ArrayList<AuthorityVO>();
			authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("ROLE_USER").build());
			authList.add(AuthorityVO.builder().username(userVO.getUsername()).authority("USER").build());
			authDao.insert(authList);
			
			// 성공하면 bKey값을 controller에 넘겨줘야함
			return bKey;
		}else {
			return false;
		}
		
		
	}


	
}
