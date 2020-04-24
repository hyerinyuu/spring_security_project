<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	form div.password {
		display: none;
	}
	
	form input.auth {
		display: block;
	}
	
	#auth_append{
		margin-bottom: 1rem;
	}
	
	.state{
		color : red;
	}
</style>
<script>
	$(function(){
		let state = $("#state p").text()
		if(state == '계정상태 : 정지'){
			$("form input").css({
				color : "lightgray"
			})
			alert("권한정지 계정입니다.")
		}
		
	
	})
	
</script>
<section>
	<form:form modelAttribute="userVO">
		
		<div class="form-group">
			<label for="username">username : </label>
			<form:input class="form-control" path="username" readonly="readonly"/>
		</div>
		
		<div class="form-group">
			<label for="email">Email : </label>
			<form:input path="email"  class="form-control"/>
		</div>
		
		<div class="form-group">
			<label for="phone">Phone : </label>
			<form:input path="phone" class="form-control"/>
		</div>
		
		<div class="form-group">
			<label for="address">Address : </label>
			<form:input path="address" class="form-control"/>
		</div>
		
		<div class="form-group" id="state">
			
			<c:choose>
				<c:when test="${userVO.enabled}">
					<p class="state">계정상태 : 활성</p>
					<label for="address">이 유저를 정지하려면 체크를 해제하세요 : </label>
					<form:checkbox path="enabled"/>
				</c:when>
				<c:otherwise>
					<p class="state">계정상태 : 정지</p>
					<label for="address">이 유저를 활성화하려면 체크하세요 : </label>
					<form:checkbox path="enabled"/>
				</c:otherwise>
			</c:choose>
			
		</div>
		
		<div id="auth_box" class="form-group">
			<button id="auth_append"  class="btn btn-primary" type="button">권한 정보 입력추가</button>
				<c:if test="${not empty userVO.authorities}">
					<c:forEach items="${userVO.authorities}" var="auth">
						<input class="form-control" name="auth" value="${auth.authority}" class="auth">
					</c:forEach>
				</c:if>
		</div>
		<div>
			<button type="button" id="btn_save" class="btn btn-success">저장</button>
		</div>
	</form:form>
</section>

