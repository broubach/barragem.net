$(function(){
	$('.language li').not('.active').fadeOut();
	$('.language li').click(function(){
	    $(this).siblings().toggle('slow');
	})	
})

function aplicarMascaras() {
	$('.date').mask("99/99/9999"); 
	$('.dateTime').mask("99/99/9999 99:99"); 
	$('.MMyyyy').mask("99/9999"); 
	$('.parcial').mask("9");
}
