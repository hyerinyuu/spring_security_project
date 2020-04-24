package com.biz.sec.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biz.sec.domain.UserDetailsVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}
	
	/*
	 * security mapping을 Annotaion을 사용하여 설정
	 * @Secured(value={문자열 배열})
	 */
	// @Secured(value= {"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public String auth(Model model) {
		return "auth/auth_view";
	}
	
	/**
	 * controller의 method에서
	 * HttpServletRequest 클래스로부터 인증(로그인)한 정보를 추출하여 세부항목을 보는 방법
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	// HttpServletRequest
	// server에서 request한 모든것들이 담겨잇는 class
	// 얘를 이용해서 security 정보를 가져올 예정
	// 조금 오래된 코드지만 아직도 사용하고 있음
	@ResponseBody
	@Secured(value= {"ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping(value = "/auth/{id}", method = RequestMethod.GET)
	public Object auth(@PathVariable("id") String id, HttpServletRequest request) {
		
		int intId = 0;
		try {
			intId = Integer.valueOf(id);
		} catch (Exception e) {
			// return type을 Object로 하면 어떤 Type도 return 가능
			return e.getMessage();
		}
		
		// 인증정보(로그인정보) 추출
		Authentication auth = (Authentication) request.getUserPrincipal();

		
		// if문은 조건에 맞는 문장만 실행하고 바로 빠져나가지만(intId가 1이면 1인경우의 코드만 실행하고 2는 검사하지 않음)
		// switch문은 모든 case를 검사하고 빠져나간다.(intId가 1이어도 1일경우와 2일경우의 코드를 모두 실행)
		// 따라서 어떤 case를 검사하면 break나 return문을 써줘서 문장을 끝내줘야한다.
		/*
		if(intId == 1) {
			log.debug("intId == 1");
		}else if(intId == 2) {
			log.debug("intId == 2");
		}
		*/

		
		// if문이 중복될 때 사용할 수 있는 method
		// c와는 다르게 key값으로 정수값 외에 문자열도 넣을 수 있음
		switch (intId) {  // key == value값 식으로 비교함(따라서 문자열 비교에는 좋은 방법이 아님(그래서 int값으로 검사함) ==> 문자열은 .equals가 조금 더 정확함)
		case 1: // == if(intId == 1)
			return auth.getDetails();

		case 2: // == else if(intId == 2)
			return auth.getCredentials();
		
		case 3: // == else if(intId == 2)
			return auth.getPrincipal();
		
		case 4: // == else if(intId == 2)
			return auth.getAuthorities();

		case 5: // == else if(intId == 2)
			return auth.getName();
			
		case 6: // == else if(intId == 2)
			UserDetailsVO userVO = (UserDetailsVO) auth.getPrincipal();
			return userVO;
			
		default: // 그외
			return "NOT FOUND";
		}
		
	}
	
}
