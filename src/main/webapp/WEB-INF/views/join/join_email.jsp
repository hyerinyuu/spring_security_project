<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include-head.jsp" %>
</head>
<style>
section form{
	width:80%;
	margin: 230px auto;
	display: flex;
	flex-flow: column;
	justify-content: center;
	align-items: center;
	
	border : 2px solid gray;
	border-radius: 8px;
	padding : 3rem;
	height: 400px;
	
	font-family: 'Do Hyeon', sans-serif;
}

span#secret {
	display: none;
}
.p-top{
	margin-bottom: 4rem;
	color : gray;
}

fieldset {
	width : 80%;
}

fieldset p {
	font-size: 20px;
	font-weight: bold;
}

fieldset h1 {
	font-size: 55px;
	font-weight: bold;
}

.fst-input{
	margin-bottom: 2rem;
}

.btn{
	font-weight: bold;
}

</style>
<script>
$(function(){
	
	$(document).on("click", "#btn-auth", function(){
		let email_value = $("input#email").val()
			
			if(email_value == ""){
				alert("이메일을 입력하세요")
				$("input#email").focus()
				return false
			}
	})
	
	$(document).on("click", "#btn_email_ok", function(){
		let secret_key = $("span#secret").text()
		// alert(secret_id)
		let secret_value = $("input#email_ok").val()
		
		
		if(secret_value == ""){
			alert("인증코드를 입력하세요!")
			$("input#email_ok").focus()
			return false
		}
		
		
		$.ajax({
			url : "${rootPath}/join/email_token_check",
			method : "POST",
			data : {
				// post로 보내기 때문에 기본값으로 csrf token값을 보내야함
				"${_csrf.parameterName}" : "${_csrf.token}",
				secret_id : "${username}",
				secret_key : secret_key,
				secret_value : secret_value
			},
			success : function(result){
					alert(result)
					document.location.replace("${rootPath}/user/login")
			},
			error : function(){
				alert("서버통신오류")
			}
		})
		
	})
	
	if("EMAIL_OK" == "${JOIN}") {
		
		$("button#btn-auth").text("재발송")
		document.getElementById("btn-auth").className = "btn btn-warning col-2"
		
		let changetext = '<p>발송된 인증코드를 입력하시고 인증하기를 누르세요<br/>'
		changetext += '*인증번호를 입력하셔야만 회원가입이 완료됩니다.*</p>'
		
		$("p#warn-message").html(changetext)
		$("p#warn-message").css({
			color : "red",
			"font-weight" : "bold"
		})
		$("h1#h1-message").text("Email 발송 완료!")
		
	}
	
	
})
</script>
<body>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>

<section class="container main-body">
	<form:form action="${rootPath}/join/join_last" modelAttribute="userVO">
		<fieldset>
		<h1 id="h1-message" class="text-center">Email 인증</h1>
		<p id="warn-message" class="text-center p-top">회원가입을 완료하시려면 Email 인증이 필요합니다.</p>
		
		<div class="row fst-input">
			<form:input type="email" path="email" class="form-control col-10" placeholder="Enter Email" />
			<button id="btn-auth" class="btn btn-primary col-2">발송</button>
		</div>
				
		<c:choose>
			<c:when test="${JOIN == 'EMAIL_OK'}">
				<span id="secret">${My_Email_Secret}</span>
				<div class="row">
					<input id="email_ok" class="form-control col-10">
					<button type="button" id="btn_email_ok" class="btn btn-primary col-2">인증하기</button>
				</div>
			</c:when>
		</c:choose>	
		</fieldset>	
	</form:form>
</section>
</body>
</html>