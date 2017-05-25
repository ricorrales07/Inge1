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
    console.log("AAA");
    var text = [];
    console.log("BBB");
    $("input").each(function() {
        text.push($(this).val());
    });
        $.ajax({
            url: "/methods/saveAttributes",
            type: 'POST',
            data: JSON.stringify({ Content: text }),
            contentType: "text/plain",
            success:function(data, textStatus, jqXHR){
                console.log("attributes saved in server directory")},
            error:function(jqXHR, textStatus, errorThrown ){
                console.log(errorThrown);
            }
        });

}