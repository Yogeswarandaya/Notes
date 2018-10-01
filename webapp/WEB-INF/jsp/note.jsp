<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="e/b-dist/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="css/note.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Notes</title>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Notes</a>
			</div>
			<form action="logout" method="post" id="logout-form">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#"><span class="glyphicon glyphicon-user"></span>
							${responsemodel.userName}</a></li>
					<li id="logout"><a href="#"><span
							class="glyphicon glyphicon-log-out"></span> Logout</a></li>
				</ul>
			</form>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4 section-group">
				<div class="add-section">
					<button type="button" class="btn btn-primary" id="add-section">
						<span class="glyphicon glyphicon-plus-sign custom-align"></span>Add
						Section
					</button>
				</div>
				<div class="section-group-div">
					<div class="section hidden">
						<span class="section-span"></span><span
							class="glyphicon glyphicon-trash pull-right hidden"></span>
					</div>
					<c:forEach items="${responsemodel.sectionList}" var="sectionData">
						<div class="section">
							<span class="section-span">${sectionData.title}</span><span
								class="glyphicon glyphicon-trash pull-right hidden"></span>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="col-md-8 notes-group">
				<div class="col-md-12 add-note-div">
					<div class="add-note pull-right">
						<button type="button" class="btn btn-primary disabled"
							id="add-button">
							<span class="glyphicon glyphicon-plus-sign custom-align"></span>Add
							Note
						</button>
					</div>
				</div>
				<div class="note-div hidden">
					<div class="note">
						<div class="note-title">
							<div class="title-div">
								<span class="title"></span>
							</div>
							<span class="glyphicon glyphicon-trash pull-right hidden"></span>
							<span class="createddate pull-right"></span>

						</div>
						<div class="note-content">
							<span></span>
						</div>
						<div class="note-footer">
							<span class="modifieddate"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade large edit-note" id="edit-note" tabindex="0"
		role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<div contenteditable="true" class="modal-title edit-note-title"></div>
				</div>
				<div class="modal-body">
					<div class="edit-note-content" contenteditable="true"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default save-button"
						id="save-button">
						<i class="fa fa-refresh fa-spin hidden"></i> Save<span
							class="glyphicon glyphicon-ok save-icon"></span>
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade small delete-section-modal"
		id="delete-section-modal" tabindex="0" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<div class="modal-title">
						<span class="glyphicon glyphicon-exclamation-sign"></span>Warning
					</div>
				</div>
				<div class="modal-body">
					<span id="modal-body-text">All the notes associated with
						this section will get deleted? Do you still want to proceed?</span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						id="delete-section-button">Delete</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade small" id="small-modal" tabindex="0"
		role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<div class="modal-title"></div>
				</div>
				<div class="modal-body">
					<span id="modal-body-text"></span>
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
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="e/b-dist/js/bootstrap.js"></script>
<script type="text/javascript" src="js/ajaxUtil.js"></script>
<script type="text/javascript" src="js/note.js"></script>
</html>
