

$(document).ready(function() {
	var original = $( ".resultsCardSpace" );
    for(var i= 0; i< 15; i++){
		original.clone().delay(1000).appendTo( ".resultsBlockRow" );
	}

	$(".resultsCard").hover(function(){
		$(this).addClass('selectedCard');
		var scientificName = $(".selectedCard .sciNameFieldCard").text();
		var author  = $(".selectedCard  .authorFieldCard").text();
		var similarityScore = $(".selectedCard  .similarityScorFieldCard").text();
		$(this).removeClass('selectedCard');	

		$("#authorField").text("").text(author);
		$("#sciNameField").text("").text(scientificName);
		$("#similarityScorField").text("").text(similarityScore);

		$("#resultInformationBlock").toggleClass("hideInfo");
	});

});



