<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form"%>
<%@ include file="/WEB-INF/views/include/include-head.jsp" %>
<%@ include file="/WEB-INF/views/auth/join-modal.jsp" %>
<!DOCTYPE html>
<html>
<head>
</head>
<script>
	$(function(){
		$(document).on("click", "button.join", function(){
			document.location.href = "${rootPath}/join"
			
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
				let ref = $("<input name='password' class='form-control' type='" + change + "'/>")
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
.login-form{
	width : 700px;
	border : 2px solid gray;
	border-radius: 8px;
	padding: 3rem;
	margin-top: 220px;
	/* font-family: 'Playfair Display', serif;*/
	font-family: 'Do Hyeon', sans-serif;
	height : 100%;
}

.login_text{
	font-size: 55px;
}

#excep_msg{
	text-align: center;
	font-weight: bold;
	font-family: 'Do Hyeon', sans-serif;
	color: red;
}
</style>
<body>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>
<div class="container login-form">
  <h1 class="text-center login_text">LOGIN</h1>
  <form:form action="${rootPath}/login" method="POST">
    <div class="form-group">
    
    <div id="excep_msg" >
    	<!--  SPRING SECURITY_LAST_EXCEPTION : 사용자 아이디등이 잘못되었을 경우 등 EXCEPTION정보가 담겨있는 변수 -->
    	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
    		<span>${SPRING_SECURITY_LAST_EXCEPTION.message}</span>
    	</c:if>	
    </div>
    
      <label for="username">USER ID : </label>
      <input class="form-control" id="username"  name="username" placeholder="Enter user ID">
    </div>
    
    <div class="form-group">
      <label for="password">PASSWORD :</label>
      <input class="form-control view_pass" type="password" id="password" name="password" placeholder="Enter password">
    </div>
    
    <div class="option">
    	<label for="view_pass">
    		<input type="checkbox" id="view_pass">비밀번호 보이기
    	</label>
    </div>
    
    <button type="submit" class="btn btn-primary">LOGIN</button>
    <button type="button" class="btn btn-danger join">JOIN</button>
  </form:form>
</div>

</body>
</html>