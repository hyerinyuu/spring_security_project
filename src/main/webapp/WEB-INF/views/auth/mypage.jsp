<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include-head.jsp" %>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>
<script>
	$(function(){
		$("input").prop("readonly", true)
		$("#btn-save").prop("disabled", true)
		
		$(document).on("click", "#btn-update", function(){
			
			let pass = $("#password").val()
			if(pass == ""){
				alert(" 비밀번호를 입력하신 후\n 다시 수정버튼을 클릭하세요")
				$("div.password").css("display", "block")
				$("#password").prop("readonly", false)
				$("#password").focus()
				return false;
		}
			
			if(pass != ""){
				
				$("#btn-save").css("display","inline")
				$("#btn-lostpass").css("display","inline")
				
				$.ajax({
					url : '${rootPath}/user/password',
					method: 'POST',  // post로 데이터를 보낼때는 반드시 csrf token도 같이 보내줘야함(get은 상관x)
					data : {
							password:pass,
							"${_csrf.parameterName}" : "${_csrf.token}"
						   },
					success: function(result){
						if(result == 'PASS_OK'){
							$("input").prop("readonly", false)
							$("input").css("color", "black")
							$("button#btn-save").prop("disabled", false)
							$("button#btn-update").prop("disabled", true)
						}else{
							alert("비밀번호를 다시 확인하시고 입력하세요")
							$("#password").focus()
						}
							
					},
					error : function(result){
						alert('서버 통신 오류')
					}
				})
			}
		})
		
	})

</script>
</head>
<style> 
.mypage-form{
	margin-top : 250px;
	font-family: 'Recipekorea'; 
}

form div.password, #btn-lostpass, #btn-save {
	display: none;
}
</style>
<body>
<div class="container mypage-form">
	<form:form modelAttribute="userVO">
		<h1>${userVO.username}'s MYPAGE</h1>
		<hr/>
		
		<div class="form-group">
			<form:input path="username" class="form-control"/>
		</div>
		
		<div class="form-group password">
			<input class="form-control" id="password" type="password" placeholder="비밀번호를 입력하세요">
		</div>
		
		<div class="form-group">
			<form:input path="email" class="form-control" placeholder="email"/>
		</div>
		
		<div class="form-group">
		<form:input path="phone" class="form-control" placeholder="전화번호"/>
		</div>
		
		<div class="form-group">
			<form:input path="address" class="form-control" placeholder="주소"/>
		</div>
		
		<div>
			<button type="button" id="btn-update" class="btn btn-primary">수정</button>
			<button type="submit" id="btn-save" class="btn btn-danger">저장</button>
			<button type="button" id="btn-lostpass" class="btn btn-warning">비밀번호 찾기</button>
		</div>
	</form:form>
</div>

</body>
</html>