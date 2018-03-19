$(function() {
	var id;
	$(".button-side").sideNav();

	// form modal
	$('.modal').modal();

	$('.btn-submit').click(
			function() {
				$.post($('#likeForm').attr('action'), $('#likeForm').serialize(),
						function(data) {
							if (data.isOk) {
								$('#modal1').modal('close');
								window.location.reload();
							} else {
								Materialize.toast(data.error, 4000);
							}
						});
			});
	$('.btn-test').click(function() {
		Materialize.toast('敬请期待', 4000);
	});
	
	//Add
	$('.addBtn').click(function(){
		$.post(ctx+'/configure/add',function(data){
			$('#divform').html(data);
			$('input').characterCounter();
			$('#modal1').modal('open');
		},'text')
	});
	//Edit
	$('.editBtn').click(function(){
		$.post(ctx+'/configure/edit/'+$(this).attr('data-id'),function(data){
			$('#divform').html(data);
			Materialize.updateTextFields();
			$('#modal1').modal('open');
		},'text');
	});
	
	$('.deleteBtn').click(function(){
		id=$(this).attr('data-id');
		$('#modal-confirm').modal('open');
	});
	$('.btnDelete').click(function(){
		$.post(ctx+'/configure/delete/'+id,function(data){
			if(data.isOk){
				window.location.reload();
			}else{
				 Materialize.toast('删除失败!', 4000);
			}
		});
	});
});