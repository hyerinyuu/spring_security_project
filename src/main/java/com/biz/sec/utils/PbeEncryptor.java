package com.biz.sec.utils;

import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PbeEncryptor {
	
	private static StandardPBEStringEncryptor pbEnc;
	
	// static 생성자 : static클래스가 생성될 때 자동으로 객체를 만들어 초기화시켜줌
	static {
		
		pbEnc = new StandardPBEStringEncryptor();

		// 암호화를 하기 위한 salt BIZ.COM 환경변수 값을 사용
		Map<String,String> envList = System.getenv(); // environment값 꺼내서 envlist에 세팅
		String strSalt = envList.get("BIZ.COM");
		pbEnc.setAlgorithm("PBEWithMD5AndDES");
		pbEnc.setPassword(strSalt);
		
	}

	// 암호화
	public static String getEncrypt(String plainText) {
		
		return pbEnc.encrypt(plainText);
	}
	
	// 복호화
	public static String getDecrypt(String encText) {

		return pbEnc.decrypt(encText);
	}
	
}
