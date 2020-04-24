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
<body>
<!-- insert Modal -->
<div class="modal fade" id="join-modal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">

			<!-- Modal Header -->
			<div class="modal-header">
				<h2 class="modal-title font-weight-bold text-center">JOIN</h2>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>

			<!-- Modal body -->
			<form action="user/join" method="POST">
				<div class="modal-body">
						<div class="form-group">
							<label for="username">User Id:</label>
							<input name="username" id="username" class="form-control" placeholder="Enter UserId">
						</div>
						<div class="form-group">
							<label for="password">Password:</label>
							<input class="form-control" type="password" id="password" name="password" placeholder="Enter password">
						</div>
						<div class="form-group">
							<label for="password">Recheck Password:</label>
							<input class="form-control" type="password" id="re_password" name="re_password" placeholder="Re-Check your Password">
						</div>
				</div>
	
				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" id="btn-login-save" class="btn btn-primary">Join</button>
					<button type="button" id="btn-find-password" class="btn btn-danger">Forgot Id or Password?</button>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>