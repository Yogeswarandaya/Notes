$(document).ready(function(e){
	
	$('#new-user').click(function(){
		$('.login-div').css('opacity', '0.1');
		$('.form-login').hide();
		$('.form-new-user').animate({width: 'show'}, "slow");
		$('.login-div').css('opacity', '1');
		hideLoginPopover();
	});
	
	$('.back-but').click(function(){
		$('.login-div').css('opacity', '0.1');
		$('.form-new-user').hide();
		$('.form-login').animate({width: 'show'}, "slow");
		$('.login-div').css('opacity', '1');
	});
	
	$('.password-text').popover({
		title: '',
		placement: 'right',
		container: 'body',
		trigger: 'manual'
	});
	
	$('.confirm-password-text').popover({
		title: '',
		placement: 'right',
		container: 'body',
		trigger: 'manual'
	});
	
	$('.username-text').popover({
		title: '',
		content: 'Should contain minimum of 5 characters.',
		placement: 'right',
		container: 'body',
		trigger: 'manual'
	});
	
	validateCredentailError();
	vaidateException();
	validateMessage();
	
	$('#form-login').submit(function(e){
		var $this=$(this);
		if(!(validateEmptyField($this.find('.username-text')) & validateEmptyField($this.find('.password-text')))){
			e.preventDefault();
		}
	});
	
	$('.login-div').on('submit', '#form-new-user:not(.loading)', function(e){
		e.preventDefault();
		var result=false;
		if(validateUsername($(this)) & validatePassword($(this))){
			result=true;
		}
		var passwordText = $(this).find('.password-text').val().trim();
		var confirmpasswordTextObj=$(this).find('.confirm-password-text');
		var confirmPasswordText = confirmpasswordTextObj.val().trim();
		if(passwordText != confirmPasswordText){
			showPopover(confirmpasswordTextObj, 'Password and Confirm password should match.');
			result=false;
		}else{
			confirmpasswordTextObj.popover('hide');
		}
		if(result){
			var textObj=$(this).find('.username-text');
			var usernameText = textObj.val().trim();
			var callBack=function(data){
				removeClass($(this), $('#register'));
				if(data != 'navigate'){
					showPopover(textObj, $('#error').val());
				}else{
					textObj.popover('hide');
					$('#navigate-login').modal('show');
					$('.back-but').trigger('click');
				}
			}
			var jsonObject={'userName' : usernameText, 'password' : passwordText};
			addClass($(this), $('#register'));
			ajaxCallPostJson('register', JSON.stringify(jsonObject), '', callBack);
		}
	});
	
});

function hideLoginPopover(){
	$('.password-text').popover('hide');
	$('.username-text').popover('hide');
}

function validateEmptyField(textObj){
	var result=false;
	var text = textObj.val().trim();
	if(text){
		result=true;
		textObj.popover('hide');
	}else{
		showPopover(textObj, 'Should not be empty.');
	}
	return result;
}

function validateUsername(thisObj){
	var result=false;
	var usernameTextObj=thisObj.find('.username-text');
	if(usernameTextObj.val()){
		result=true;
		usernameTextObj.popover('hide');
	}else{
		usernameTextObj.popover('show');
	}
	return result;
}

function validatePassword(thisObj){
	var result=false;
	var passwordTextObj=thisObj.find('.password-text');
	var password = passwordTextObj.val();
	var patt1 = /[a-z]/g;
	var patt2 = /[A-Z]/g;
	var patt3 = /[0-9]/g;
	var patt4 = /[^a-zA-Z0-9]/g;
	if(password.length < 8){
		showPopover(passwordTextObj, 'Should contain minimum of 5 characters.');
		return result;
	}
	if(!(patt1.test(password) && patt2.test(password) && patt3.test(password))){
		showPopover(passwordTextObj, 'Should contain at least one alphabet, one number, one special character.');
		return result;
	}
	if(!patt4.test(password)){
		showPopover(passwordTextObj, 'Should contain at least one alphabet, one number, one special character.');
		return result;
	}
	passwordTextObj.popover('hide');
	return result=true;
}

function validateCredentailError(){
	var errorVal=$('#validation-error').val();
	if(errorVal && (errorVal == $('#validation-error').data('error'))){
		var textObj=$('#form-login').find('.username-text');
		showPopover(textObj, errorVal);
	}
}

function vaidateException(){
	var exceptionMessage=$('#exception-message').val();
	if(exceptionMessage){
		showMessageModal(exceptionMessage);
	}
}

function validateMessage(){
	var message=$('#message').val();
	if(message){
		showMessageModal(message);
	}
}

function addClass(formSelector, registerButton){
	formSelector.addClass('loading');
	registerButton.addClass('loading');
}

function removeClass(formSelector, registerButton){
	formSelector.removeClass('loading');
	registerButton.removeClass('loading');
}

function validate(){
	var data=ajaxSyncCallGet('/validate/session', '', '');
	if(!data){
		window.location='/note';
	}
}
