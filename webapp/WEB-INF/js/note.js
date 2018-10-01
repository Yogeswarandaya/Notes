$(document).ready(function(){
	
	cacheSectionNames();
	monthMap();
	
	$('#logout').click(function(e){
		$(this).closest('form').submit();
	});
	
	$('.notes-group').on('click', '.note-div', function(){
		var noteTitle=$(this).find('.title').text();
		$('#edit-note').find('.modal-title').text(noteTitle);
		var noteContent=$(this).find('.note-content').find('span').text();
		$('.active-note').removeClass('active-note');
		$(this).addClass('active-note');
		$('#edit-note').find('.edit-note-content').text(noteContent);
		$('#edit-note').find('.modal-footer').find('button').attr('id', 'update-note');
		$('#edit-note').modal('show');
	});
	
	var popoverObj=$('#add-section').popover({
		title: '<label class="p-r-5">Title</label><span class="p-l-5 glyphicon glyphicon-remove-sign pull-right"></span>',
		content: '<div class="section-title-header"><input id="section-title" type="text" />'
			+''
			+'<span class="p-l-5 glyphicon glyphicon-ok"></span></div>',
		html: true,
		placement: 'right',
		container: 'body',
		trigger: 'manual'
	});
	
	$('#add-section').click(function(e){
		$(this).popover('show');
	});
	
	$('body').on('click', '.glyphicon-remove-sign', function(e){
		$('#add-section').popover('hide');
	});
	
	$('.section-group-div').on('click', '.glyphicon-trash', function(e){
		e.stopPropagation();
		$('.active-delete-section').removeClass('active-delete-section');
		$(this).closest('div.section').addClass('active-delete-section');
		$('#delete-section-modal').modal('show');
	});
	
	$('#delete-section-button').click(function(e){
		var activeDeleteSection=$('.active-delete-section');
		var sectionName=activeDeleteSection.find('span.section-span').text();
		var callBack=function(data){
			activeDeleteSection.remove();
			sectionNameMap.delete(sectionName);
			$('.notes-group').find('.note-div:not(.hidden)').remove();
		}
		$('#delete-section-modal').modal('hide');
		ajaxCallDelete('section', '/'+sectionName, '', callBack);
	});
	
	$('.section-group-div').on('click', '.section', function(e){
		$('#add-button').removeClass('disabled');
		$('.active-section').removeClass('active-section');
		$(this).addClass('active-section'); 
		var sectionName=$(this).find('.section-span').text();
		var callBack=function(data){
			$('.notes-group').find('.note-div:not(.hidden)').remove();
			$.each(data.sectionList, function(index, section){
				$.each(section.noteDataList, function(index, note){
					var noteDivClone=$('.notes-group').find('.note-div.hidden').clone();
					var setNoteDataObj=new setNoteData();
					setNoteDataObj.init(noteDivClone);
					setNoteDataObj.commonData(note.title, note.description);
					setNoteDataObj.previousCreatedDate(note.createdDate);
					setNoteDataObj.previousModifiedDate(note.modifiedDate);
					noteDivClone.removeClass('hidden');
					$('.notes-group').append(noteDivClone);
				});
			});
		}
		ajaxCallGet('section/note?', 'sectionName='+sectionName, '', callBack);
	});
	
	$('.notes-group').on('click', '#add-button:not(.disabled)', function(){
		var modalTitleObj=$('#edit-note').find('.modal-title');
		modalTitleObj.css({'width' : 'auto'});
		modalTitleObj.text('');
		$('#edit-note').find('.edit-note-content').text('');
		$('#edit-note').find('.modal-footer').find('button').attr('id', 'create-note');
		$('#edit-note').modal('show');
	});
	
	$('body').on('keydown', '.section-title-header #section-title, .edit-note-title', function(e){
		if($(this).val().length > 50){
			return false;
		}
	});
	
	$('body').on('keydown', '.edit-note-title', function(e){
		if($(this).text().length > 50){
			return false;
		}
	});
	
	$('body').on('click', '.section-title-header .glyphicon-ok', function(e){
		var $this=$(this);
		var sectionTitle=$('#section-title').val().trim();
		if(sectionTitle){
			if(!sectionNameMap.has(sectionTitle)){
				var callBack=function(data){
					if(data != 'navigate'){
						showSmallModal(data);
					}else{
						$('#section-title').val('');
						var cloneobj=$('.section.hidden').clone();
						cloneobj.find('.section-span').text(sectionTitle);
						cloneobj.removeClass('hidden');
						$('div.section-group-div').append(cloneobj);
						sectionNameMap.set(sectionTitle, new Date());
						sortDivNames();
					}
				}
				ajaxCallPost('create/section/', {sectionName: sectionTitle, createdDate: new Date()}, '', callBack);
			}
		}
	});
	
	var editNoteTitle=$('.edit-note-title').popover({
		title: '',
		content: 'Title already exists.',
		html: true,
		placement: 'left',
		container: 'body',
		trigger: 'manual'
	});
	
	$('#edit-note').on('click', '#create-note', function(e){
		var editNoteModal=$(this).closest('#edit-note');
		if(validateNoteTitle(editNoteModal)){
			var noteTitle=editNoteModal.find('.edit-note-title').text().trim();
			var noteContent=editNoteModal.find('.edit-note-content').text();
			var createdDate=new Date();
			var sectionName=getSectionName();
			var callBack=function(data){
				if(data != 'navigate'){
					showPopover($('.edit-note-title'), 'Title already exists.');
				}else{
					var noteDivClone=$('.notes-group').find('.note-div.hidden').clone();
					var setNoteDataObj=new setNoteData();
					setNoteDataObj.init(noteDivClone);
					setNoteDataObj.commonData(noteTitle, noteContent);
					setNoteDataObj.currentCreatedDate();
					setNoteDataObj.currentModifiedDate();
					noteDivClone.removeClass('hidden');
					$('.notes-group').find('.note-div.hidden').after(noteDivClone);
					$('#edit-note').modal('hide');
				}
			}
			var noteJsonObj={'sectionName': sectionName, 'title': noteTitle, 'description': noteContent, 'createdDate': createdDate};
			ajaxCallPostJson('note', JSON.stringify(noteJsonObj), '', callBack);
		}
	});
	
	$('#edit-note').on('click', '#update-note', function(e){
		var editNoteModal=$(this).closest('#edit-note');
		if(validateNoteTitle(editNoteModal)){
			var noteTitle=editNoteModal.find('.edit-note-title').text().trim();
			var noteContent=editNoteModal.find('.edit-note-content').text();
			var modifiedDate=new Date();
			var sectionName=getSectionName();
			$('#edit-note').modal('hide');
			var callBack=function(data){
				if(data != 'navigate'){
					$('.edit-note-title').popover('show');
				}else{
					var noteDivObj=$('.active-note');
					var setNoteDataObj=new setNoteData();
					setNoteDataObj.init(noteDivObj);
					setNoteDataObj.commonData(noteTitle, noteContent);
					setNoteDataObj.currentModifiedDate();
				}
			}
			var oldTitle=$('.active-note').find('.title').text().trim();
			var noteJsonObj={'sectionName': sectionName, 'oldTitle': oldTitle,  
					'title': noteTitle, 'description': noteContent, 'modifiedDate': modifiedDate};
			ajaxCallPutJson('note', JSON.stringify(noteJsonObj), '', callBack);
		}
	});
	
	$('.notes-group').on('click', '.note-title .glyphicon-trash', function(e){
		e.stopPropagation();
		var noteDivObj=$(this).closest('div.note');
		var sectionName=getSectionName();
		var noteTitle=noteDivObj.find('.title').text();
		var callBack=function(data){	
			if(data == 'navigate'){
				noteDivObj.closest('.note-div').remove();
			}else{
				showSmallModal(data);
			}
		}
		ajaxCallDelete('note','/'+sectionName+'/'+noteTitle, '', callBack);
	});
	
	$('.edit-note').on('hide.bs.modal', function(e){
		$('.edit-note-title').popover('hide');
	});
	
});

function getSectionName(){
	var sectionName=$('.active-section').find('.section-span').text();
	return sectionName;
}

function keypressValidation(event){
	var regex = new RegExp("^[a-zA-Z,]+$");
	var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
	if (!regex.test(key)) {
	    event.preventDefault();
	    return false;
	}
}

function cacheSectionNames(){
	sectionNameMap = new Map(); 
	$('.section:not(.hidden)').each(function(index, data){
		var sectionName=$(this).find('.section-span').text();
		sectionNameMap.set(sectionName, new Date());
	});
}

function sortDivNames(){
    var sortedDivs=$('div.section').sort(function(a, b){
    	var aText=$(a).find('span.section-span').text().toUpperCase();
    	var bText=$(b).find('span.section-span').text().toUpperCase();
        return (aText < bText) ? -1 : (aText > bText) ? 1 : 0;
    });
    $('div.section-group-div').append(sortedDivs);
}
function getParsedCurrentDate(){
	var currentDate=new Date();
	var year=currentDate.getFullYear();
	var date=currentDate.getDate();
	var monthIndex=currentDate.getMonth();
	var month=monthMap.get(monthIndex);
	return month+' '+date+' '+year;
}

function monthMap(){
	monthMap = new Map();
	monthMap.set(0, 'Jan');
	monthMap.set(1, 'Feb');
	monthMap.set(2, 'Mar');
	monthMap.set(3, 'Apr');
	monthMap.set(4, 'May');
	monthMap.set(5, 'Jun');
	monthMap.set(6, 'Jul');
	monthMap.set(7, 'Aug');
	monthMap.set(8, 'Sep');
	monthMap.set(9, 'Oct');
	monthMap.set(10, 'Nov');
	monthMap.set(11, 'Dec');
}

function showSmallModal(data){
	$('#small-modal').find('#modal-body-text').text(data);
	$('#small-modal').modal('show');
}

function setNoteData(){
	var noteDivObj;
	this.init=function(noteDivObj){
		this.noteDivObj=noteDivObj;
	};
	this.commonData=function(noteTitle, noteContent){
		this.noteDivObj.find('.title').text(noteTitle);
		this.noteDivObj.find('.note-content').find('span').text(noteContent);
	};
	this.previousCreatedDate=function(previousCreatedDate){
		this.noteDivObj.find('.createddate').text('Created '+previousCreatedDate);
	};
	this.currentCreatedDate=function(){
		this.noteDivObj.find('.createddate').text('Created '+getParsedCurrentDate());
	};
	this.previousModifiedDate=function(previousModifiedDate){
		this.noteDivObj.find('.modifieddate').text('Edited '+previousModifiedDate);
	};
	this.currentModifiedDate=function(){
		this.noteDivObj.find('.modifieddate').text('Edited '+getParsedCurrentDate());
	}
}

function validateNoteTitle(editNoteModal){
	var result=true;
	var noteTitle=editNoteModal.find('.edit-note-title').text().trim();
	if(!noteTitle){
		showPopover($('.edit-note-title'), 'Enter a title');
		result=false;
	}
	return result;
}