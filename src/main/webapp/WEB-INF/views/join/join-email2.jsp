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

section.email-body{
	width:80%;
	margin: 120px auto;
	dixplay: flex;
	flex-flow: column;
	justify-content: center;
	align-items: center;
}

</style>
<body>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>

<section class="email-body">
	<h2>Email 인증</h2>
	<div>회원가입을 완료하시려면 Email인증이 필요합니다.</div>
	
	
	<form:form action="${rootPath}/join/joinok" modelAttribute="userVO" class="form-group">
		<form:input type="email" path="email" class="form-control" placeholder="Enter Email" />
		<c:if test="${empty userVO.email}">
			<button class="btn btn-primary">인증(c)</button>
		</c:if>
		<c:if test="${not empty userVO.email}">
			<button class="btn btn-primary">재인증(c)</button>
		</c:if>	
		<c:choose>
			<c:when test="${JOIN == 'EMAIL_OK'}">
				<button class="btn btn-primary">발송(when)</button>
			</c:when>
			<c:otherwise>
				<button class="btn btn-primary">재발송(other)</button>
			</c:otherwise>
		</c:choose>		
	</form:form>
</section>
</body>
</html>