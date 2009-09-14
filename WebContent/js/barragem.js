$j(function(){
	$j('.language li').not('.active').fadeOut();
	$j('.language li').click(function(){
	    $j(this).siblings().toggle('slow');
	})	
})
