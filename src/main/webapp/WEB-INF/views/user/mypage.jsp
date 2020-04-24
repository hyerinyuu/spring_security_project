<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include/include-head.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>나의 홈페이지</title>
</head>
<style>
.mypage-box{
	margin-top: 250px;
}

.btn-margin{
	margin-left: 10px;
}



</style>
<body>
<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>


<div class="container mypage-box">
	<h2>${userVO.username}'s PAGE</h2>
	<hr/>
	<div class="table-view">
		<table class="table table-striped">
	
		<br/>
		 	<thead class="thead-dark">
				<tr>
					<th>ID</th>
						<td colspan="5">
							<div>
								${userVO.username}
								<button class="btn btn-sm btn-primary btn-margin" data-toggle="collapse" data-target="#colps-id">수정</button>
							</div>	
							<div id="colps-id">
								<form class="form-inline">
									<input class="form-control mr-sm-2" placeholder="id">
									<button class="btn btn-primary">수정완료</button>
								</form>
							</div>		
						</td>
				</tr>
				
				<tr>
					<th>만료여부</th><td <c:if test="${userVO.accountNonExpired}">O</c:if>>X</td>
					<th>잠금여부</th><td <c:if test="${userVO.accountNonLocked}">O</c:if>>X</td>
				</tr>
				
				<tr>
					<th>이메일</th><td>${userVO.email}</td>
					<th>전화번호</th><td>${userVO.phone}</td>
					<th>주소</th><td>${userVO.address}</td>
				</tr>
			</thead>	
		</table>
	</div>	
</div>

</body>
</html>