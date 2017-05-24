var createPieceAtt = {

}



function checkCard(card){
	$(".attribute-card").removeClass("selectedCard");
	$(card).addClass("selectedCard");
}

function deleteCard() {
	//TODO Verify with the user. 
	$(".selectedCard").fadeOut( "slow", function() {
    	this.remove();
  });
}


function addProperty(){
	console.log($(".cardTemplate").length);
	var newCard = $(".cardTemplate").clone().attr("display","inline-block").removeClass("cardTemplate");
	$("#attribute-card-list").append(newCard);
	
}