/*$j(function(){
	$j('.language li').not('.active').fadeOut();
	$j('.language li').click(function(){
	    $j(this).siblings().toggle('slow');
	});	
}); */

function aplicarMascaras() {
	if ($j.mask != null) {
		$j('.date').unmask(); 
		$j('.date').mask("99/99/9999"); 
		$j('.dateTime').unmask(); 
		$j('.dateTime').mask("99/99/9999 99:99"); 
		$j('.MMyyyy').unmask(); 
		$j('.MMyyyy').mask("99/9999"); 
		$j('.parcial').unmask(); 
		$j('.parcial').mask("9");
		$j('.time').unmask(); 
		$j('.time').mask("99:99"); 
	}
}