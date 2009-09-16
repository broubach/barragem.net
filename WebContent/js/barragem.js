$(function(){
	$('.language li').not('.active').fadeOut();
	$('.language li').click(function(){
	    $(this).siblings().toggle('slow');
	})	
})
