var createPieceAtt = {

}




function checkCard(card){
	$(".attribute-card").removeClass("selectedCard");
	$(card).addClass("selectedCard");
}

function deleteCard(card_button) {
	//TODO Verify with the user. 
	$(card_button).addClass("selectedCard");
	$(".attribute-card").has(".selectedCard").fadeOut( "slow", function() {
    	this.remove();
  });
}


function addProperty(){
	console.log($(".cardTemplate").length);
	var newCard = $(".cardTemplate").clone().attr("display","inline-block").removeClass("cardTemplate");
	$("#attribute-card-list").append(newCard);
	
}

function saveAttributes(){
    var text = [];
    var attributes = "{\n";
    $("input").each(function() {
        text.push($(this).val());
    });
    for(i = 2; i < text.length; i++){
        attributes += "\"" + text[i++] + "\": \"" + text[i] + "\",\n";
    }
        $.ajax({
            url: "/methods/saveAttributes",
            type: 'POST',
            data: attributes,
            contentType: "text/plain",
            success:function(data, textStatus, jqXHR){
                console.log("attributes saved in server directory")},
            error:function(jqXHR, textStatus, errorThrown ){
                console.log(errorThrown);
            }
        });

}