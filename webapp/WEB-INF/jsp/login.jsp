<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/e/b-dist/css/bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/e/f-awe/css/font-awesome.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/note.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<div class="login-div">
		<c:if test="${not empty validationError}">
			<input type="hidden" id="validation-error" value="${validationError}"
				data-error="<spring:eval expression="@environment.getProperty('username.password.not.correct')" />">
		</c:if>
		<c:if test="${not empty exceptionMessage}">
			<input type="hidden" id="exception-message"
				value="${exceptionMessage}">
		</c:if>
		<c:if test="${not empty message}">
			<input type="hidden" id="message" value="${message}">
		</c:if>
		<form action="/login" method="post" class="form-login" id="form-login">
			<div class="username-div">
				<label>UserName</label> <input type="text" name="userName"
					class="username-text form-control" maxlength="25" />
			</div>
			<div class="password-div">
				<label>Password</label> <input type="password" name="password"
					class="password-text form-control" maxlength="25" />
			</div>
			<div class="submit-div">
				<button id="submit" class="submit-button">Login</button>
				<a id="new-user" class="new-user">New user?</a>
			</div>
		</form>
		<form action="register" method="post" class="form-new-user"
			style="display: none;" id="form-new-user">
			<div>
				<span class="glyphicon glyphicon-arrow-left back-but"></span>
			</div>
			<div class="username-div">
				<label>UserName</label> <input type="text" name="userName"
					class="username-text form-control" maxlength="25" />
			</div>
			<div class="password-div">
				<label>Password</label> <input type="password" name="password"
					class="password-text form-control" maxlength="25" />
			</div>
			<div class="confirm-password-div">
				<label>Confirm Password</label> <input type="password"
					name="confirm-password" class="confirm-password-text form-control"
					maxlength="25" />
			</div>
			<div class="submit-div">
				<button id="register" class="submit-button register">
					<i class="fa fa-refresh fa-spin hidden"></i>Register
				</button>
			</div>
		</form>
	</div>
	<div class="modal fade small navigate-login" id="navigate-login"
		tabindex="0" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<div class="modal-title"></div>
				</div>
				<div class="modal-body">
					<span>User registration successful.</span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade small message-modal" id="message-modal"
		tabindex="0" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/e/b-dist/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/ajaxUtil.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/login.js"></script>
</html>