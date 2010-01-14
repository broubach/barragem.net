$j(function(){
	$j('.language li').not('.active').fadeOut();
	$j('.language li').click(function(){
	    $j(this).siblings().toggle('slow');
	})	
})

function aplicarMascaras() {
	$j('.date').mask("99/99/9999"); 
	$j('.dateTime').mask("99/99/9999 99:99"); 
	$j('.MMyyyy').mask("99/9999"); 
	$j('.parcial').mask("9");
}