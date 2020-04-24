<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/include-head.jsp" %>
	<style>
		#body {
			position:fixed;
			top:90px;
			left:0;
			width:100%;
			display: flex;
			
			font-family: 'Recipekorea'; 
			
		}
		#body menu {
			flex:1;
			margin: 8px;
			align-content: center;
			align-items: center;
		}
		
		#body menu li {
			list-style: none;
		}
		
		#body menu li a {
			display: inline-block;
			padding: 10px 10px;
			text-decoration: none;
			width: 170px;
			margin-left: 10px;
			border-bottom: 2px solid white;
			background: #dccbed;
			
			color : black;
			
		}
		
		#body menu li a:hover {
			background: #eadff2;
			border-bottom: 2px solid black;
			transition : ease 0.3s;
		}
		
		#body article {
			flex: 5;
			margin: 5px;
		}
		
		#admin_content table, #admin_content form{
			width : 90%;
		}
	</style>
	<script>
		$(function(){
			$(document).on("click","#user_list",function(){
				$.get("${rootPath}/admin/user_list",function(result){
					$("#admin_content").html(result)
				})
			})
			
			$(document).on("click","tr.tr_user",function(){
				let username = $(this).data("id")
				$.get("${rootPath}/admin/user_detail_view/" + username,
						function(result){
					$("#admin_content").html(result)
				})
			})
			
			$(document).on("click", "button#btn_save", function(){
				let formdata = $("form").serialize()
				$.post("${rootPath}/admin/user_detail_view", formdata,
						function(result){
					$("#admin_content").html(result)
					alert('update 성공!!')
				})
			})
			
			$(document).on("click","#auth_append",function(){
				
				let auth_input = $("<input/>", {class:"auth form-control", name:"auth"})
				// auth_input.append($("<p/>",{text:'제거',class:'auth_delete'}))
				$("div#auth_box").append(auth_input)
			})
			
		})
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include-nav.jspf" %>
	<section id="body">
		<menu>
			<h3>&nbsp;관리자 페이지</h3>
			<ul>
				<li><a href="javascript:void(0)" id="user_list">USER LIST</a>
				<li><a href="#">메뉴1</a>
				<li><a href="#">메뉴2</a>
			</ul>
		</menu>
		<article id="admin_content">
			
		</article>
	</section>
</body>
</html>