

$(document).ready(function() {
	var original = $( ".resultsCardSpace" ).clone();
      for(var i= 0; i< 15; i++){
	original.clone().delay(1000).appendTo( ".resultsBlockRow" );
}
});

