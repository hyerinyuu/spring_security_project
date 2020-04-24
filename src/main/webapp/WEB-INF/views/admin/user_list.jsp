<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
table tr td{
	cursor: pointer;
}

.active {
	color : red;
}

.dead{
	color : lightgray;
}
.line-thourgh {
	text-decoration: line-through;
	color: lightgray;
}

</style>
<div class="table-view">
	<table class="table table-striped">
		<tr>
			<th>NO</th>
			<th>UserName</th>
			<th>Email</th>
			<th>Phone</th>
			<th>Address</th>
			<th>State</th>
		</tr>
		<c:choose>
			<c:when test="${empty userList}">
				<tr> <td colspan="5">user정보 없음</td></tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${userList}" var="user" varStatus="i">
					<tr data-id="${user.username}" class="tr_user">
						<td
								<c:if test="${!user.enabled}"> class="line-thourgh"</c:if>>${i.count}</td>
						<td
								<c:if test="${!user.enabled}"> class="line-thourgh"</c:if>>${user.username}</td>
						<td
								<c:if test="${!user.enabled}"> class="line-thourgh"</c:if>>${user.email}</td>
						<td
								<c:if test="${!user.enabled}"> class="line-thourgh"</c:if>>${user.phone}</td>
						<td
								<c:if test="${!user.enabled}"> class="line-thourgh"</c:if>>${user.address}</td>
						<c:choose>
							<c:when test="${user.enabled}">
								<td class="active">활성</td>
							</c:when>
							<c:otherwise>
							 	<td class="dead">정지</td>
							</c:otherwise>
						</c:choose>	
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
</div>