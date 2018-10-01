
function ajaxCallPost(action,dataValue,loaderID, callBack){
	$.ajax({
		type : 'post',
		url : action,
		data : dataValue,
		statusCode: {529: function(data, textStatus, xhr) {errorModal();},
			589: function(data, textStatus, xhr) {window.location='ajax/session/expire?exceptionMessage='+data.responseText;}
		},
		beforeSend : function() {$(loaderID).css('display','block');},
		complete : function() {$(loaderID).css('display', 'none');},
		success : function(dt, status, request) {
			callBack(dt);
		},
	});
}

function ajaxCallPostJson(action,dataValue,loaderID, callBack){
	var data;
	$.ajax({
		type : 'post',
		url : action,
		data : dataValue,
		contentType: 'application/json; encoding=utf-8',
		statusCode: {529: function(xhr) {errorModal();},
			589: function(data, textStatus, xhr) {window.location='ajax/session/expire?exceptionMessage='+data.responseText;}
		},
		beforeSend : function() {$(loaderID).css('display', 'block');},
		complete : function() {$(loaderID).css('display', 'none');},
		success : function(dt, status, request) {
			data = dt;
			callBack(data);
		},
	});
}

function ajaxCallPutJson(action,dataValue,loaderID, callBack){
	var data;
	$.ajax({
		type : 'put',
		url : action,
		data : dataValue,
		contentType: 'application/json; encoding=utf-8',
		statusCode: {529: function(xhr) {errorModal();},
			589: function(data, textStatus, xhr) {window.location='ajax/session/expire?exceptionMessage='+data.responseText;}
		},
		beforeSend : function() {$(loaderID).css('display', 'block');},
		complete : function() {$(loaderID).css('display', 'none');},
		success : function(dt, status, request) {
			data = dt;
			callBack(data);
		},
	});
}

function ajaxCallGet(action, dataValue, loaderID, callBack){
	$.ajax({
		type : 'get',
		url : action+dataValue,
		statusCode: {529: function(data, textStatus, xhr) {errorModal();},
			589: function(data, textStatus, xhr) {window.location='ajax/session/expire?exceptionMessage='+data.responseText;}
		},
		beforeSend : function() {$(loaderID).css('display','block');},
		complete : function() {$(loaderID).css('display', 'none');},
		success : function(dt, status, request) {
			callBack(dt);
		},
	});
}

function ajaxCallDelete(action, dataValue, loaderID, callBack){
	$.ajax({
		type : 'delete',
		url : action+dataValue,
		data : dataValue,
		statusCode: {529: function(data, textStatus, xhr) {errorModal();},
			589: function(data, textStatus, xhr) {window.location='ajax/session/expire?exceptionMessage='+data.responseText;}
		},
		beforeSend : function() {$(loaderID).css('display','block');},
		complete : function() {$(loaderID).css('display', 'none');},
		success : function(dt, status, request) {
			callBack(dt);
		},
	});
}

function showMessageModal(message){
	$('#message-modal').find('.modal-body').text(message);
	$('#message-modal').modal('show');
}

function errorModal(){
	$('#message-modal').find('.modal-body').text('Server encountered a problem. Please try again later.');
	$('#message-modal').modal('show');
}

function showPopover(objectRef, content){
	objectRef.attr('data-content', content);
	objectRef.popover('show');
}