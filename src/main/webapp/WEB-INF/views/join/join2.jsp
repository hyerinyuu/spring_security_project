<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include-head.jsp" %>

<script>
$(function(){
	$(document).on("click", "#btn-join", function(){
		let username = $("#username")
		let password = $("#password")
		let re_password = $("#re_password")
		
		if(username.val() == ""){
			alert("아이디를 입력해주세요")
			username.focus()
			return false;
		}
		
		if(password.val() == ""){
			alert("비밀번호를 입력해주세요")
			password.focus()
			return false;
		}
		
		if(re_password.val() == ""){
			alert("비밀번호 확인을 입력해주세요")
			re_password.focus()
			return false;
		}
		
		if(password.val() != re_password.val()){
			alert("비밀번호가 일치하지 않습니다.")
			password.focus()
			return false;
		}
		
		$("form").submit()
	})
	
	/* blur : 현재 입력박스에서 포커스가 벗어났을때 발생하는 event */
	$(document).on("blur", "#username", function(){
		let username = $(this).val()
		if(username == ""){
			$("#m_username").text("please fill out this field")
			return false;
		}
		
		$.ajax({
			url : "${rootPath}/user/idcheck",
			data : {username : username},
			success : function(result){
				if(result == "USED"){
					$("#m_username").text("*이미 사용중인 Id입니다")
					return false;
				}
			},
			error : function(){
				alert("서버와 통신오류")
			}
		})
	})
	
	
	// checkbox 비밀번호 보이게 설정
	// 현재 DOM화면에 class가 view_pass인 모든것에 적용하라
	$(".view_pass").each(function(index,input){
		// 매개변수로 전달된 input을 선택하여
		// 변수 input_ref에 임시저장
		let input_ref = $(input)
		$("input#view_pass").click(function(){
			let change = $(this).is(":checked")
				? "text" : "password";
			// 가상의 input 생성
			// <input type='text'> 또는 <input type='password'>
			let ref = $("<input  class='form-control' type='" + change + "'/>")
				.val(input_ref.val())
				.insertBefore(input_ref);
			
			// 앞에 있는 input 지우고 새로 입력하라
			input_ref.remove();
			input_ref = ref;
			
		})
	})
	
})

</script>
<style>

	.message{
		color: red;
		font-size: 1rem;
	}
	
	.join_text{
	font-size: 55px;
	}
	
	.join-form{
	width : 700px;
	border : 2px solid gray;
	border-radius: 8px;
	padding: 3rem;
	margin-top: 220px;
	/* font-family: 'Playfair Display', serif;*/
	font-family: 'Do Hyeon', sans-serif;
}
</style>
</head>
<body>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>
<div class="container join-form">
  <h2 class="text-center join_text">JOIN</h2>
  <!-- 
  <input class="form-control" type="text" name="${_csrf.parameterName}" value="${_csrf.token}">
   -->
  <form:form method="POST" action="${rootPath}/join/user" modelAttribute="userVO" >
    <div class="form-group">
      <label for="username">User Id:</label>
      <form:input class="form-control" path="username" placeholder="Enter user ID" />
      <div class="message" id="m_username"></div>
    </div>
    
    <div class="form-group">
      <label for="password">Password:</label>
      <form:input class="form-control view_pass" type="password" path="password" placeholder="Enter password" />
      <div class="message" id="m_password"></div>
    </div>
    
    <div class="form-group">
      <label for="re_password">Recheck your Password:</label>
      <input class="form-control view_pass" type="password" id="re_password" name="re_password" placeholder="Enter password">
      <div class="message" id="m_repassword"></div>
    </div>
    
    <div class="option">
    	<label for="view_pass">
    		<input type="checkbox" id="view_pass">비밀번호 보이기
		</label>
    </div>
    <br/>
    
    <button type="button" id="btn-join" class="btn btn-primary">JOIN</button>
    <button type="button" class="btn btn-danger join">Forgot Id or Password?</button>
  </form:form>
</div>

</body>
</html>
