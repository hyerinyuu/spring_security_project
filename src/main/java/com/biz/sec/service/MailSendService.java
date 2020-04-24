package com.biz.sec.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.biz.sec.domain.UserDetailsVO;
import com.biz.sec.utils.PbeEncryptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSendService {
	
	private final JavaMailSender javaMailSender;
	private final String from_email = "chant6@naver.com";
	
	public MailSendService(@Qualifier("naverMailHandler") JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}
	
	
	public void sendMail() {
		
		String to_email = "hyerin.you@gmail.com";
		
		String subject = "메일보내기 테스트";
		String content = "안녕안녕";
		
		this.sendMail(to_email, subject, content);;
	}	
		
	public void sendMail(String to_email, String subject, String content) {
			
		to_email = "hyerin.you@gmail.com";
		
		// mail을 보내기 위한 도구
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper mHelper;
		mHelper = new MimeMessageHelper(message, "UTF-8");
		
		try {
			mHelper.setFrom(from_email);
			mHelper.setTo(to_email);
			mHelper.setSubject(subject);
			mHelper.setText(content, true);  // true : 메일 본문에 html 효과 주기
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 회원가입된 사용자에게 인증 email을 전송
	 * 
	 * username을 암호화시키고 
	 * email인증을 수행할 수 있는 링크를 email 본문에 작성하여 전송한다.
	 * 
	 * @param userVO
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	// public boolean join_send(UserDetailsVO userVO) {
	public String join_send(UserDetailsVO userVO) throws UnsupportedEncodingException {
			
		String username = userVO.getUsername();
		String email = userVO.getEmail();
		String encUserName = PbeEncryptor.getEncrypt(username);
		String encEmail = PbeEncryptor.getEncrypt(email);
		
		// 실무에서는 문자열을 여러개 붙일때는 StringBuilder를 사용하라고 권장함(옛날코드)
		StringBuilder email_link = new StringBuilder();
		
		/*
		 * jasypt를 사용하여 username과 email을 암호화하였더니
		 * 슬래시(/)등 URL을 통해서 보내면 문제를 발생시키는
		 * 특수문자들이 포함이 된다.
		 * 이 특수문자를 URL을 통해서 정상적으로 보낼 수 있도록
		 * 암호화된 문자열을 URLEncoder.encode() method를 이용해서
		 * encoding을 수행해주어야한다.
		 */
		email_link.append("http://localhost:8080/sec/");
		email_link.append("join/emailok");
		email_link.append("?username=" + URLEncoder.encode(encUserName, "UTF-8"));  
		// email_link.append("/");
		email_link.append("&email=" + URLEncoder.encode(encEmail, "UTF-8")); // == localhost:8080/sec/join/emailok/암호화된Id값/암호화된email값
		
		StringBuilder email_message = new StringBuilder();
		email_message.append("<h2>회원가입을 환영합니다!</h2><br/>"); 
		email_message.append("<p>회원가입 절차를 마무리하시려면");
		email_message.append("Email 인증을 해주세요</p><br/>");
		email_message.append("<p>Email 인증을 하시려면");
		email_message.append("<a href='%s'>인증링크</a>");
		email_message.append("를 클릭하세요!</p>");
		
		String send_message
		= String.format(email_message.toString(), email_link.toString());
		
		String to_email = email;
		String subject = "혜린나라 회원인증 메일";
		
		this.sendMail(to_email, subject, send_message);
		
		return send_message;
	}


	/**
	 * @since 2020-04-21
	 * email인증을 위한 token정보를 email로 전송하기
	 * 
	 * @param userVO 
	 * @param email_token
	 */
	public void email_auth(UserDetailsVO userVO, String email_token) {

		StringBuilder email_content = new StringBuilder();
		
		email_content.append("<style>");
		email_content.append(".biz-token {");
		email_content.append("border : 1px solid #33d6ff;");
		email_content.append("background-color : #ccf5ff;");
		email_content.append("color : black");
		email_content.append("font-weight : bold");
		email_content.append("}");
		email_content.append("</style>");
		
		email_content.append("<h1>혜린나라 회원가입을 환영합니다</h1>");
		email_content.append("<p>다음 인증코드를 회원가입폼의 인증코드란에 입력하셔야만<br/></p>");
		email_content.append("<p>회원가입이 완료됩니다.</p>");
		
		email_content.append("<div class='biz-token'>");
		email_content.append(email_token);
		email_content.append("</div>");
		
		String subject = "혜린나라 회원인증 코드";
		
		this.sendMail(userVO.getEmail(), subject, email_content.toString());
		
	}




	
	
	
	
	

}
