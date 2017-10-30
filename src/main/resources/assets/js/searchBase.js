

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



function getSearchResults()
{
    pieces = [];
          for (var i = 0; i < composicionActual.partsList.length; i++){
              var hiddenData = composicionActual.partIds[i].split("C");
              pieces.push(
                  {_id: composicionActual.partIds[i],
                  positionX: composicionActual.partsList[i].x,
                  positionY: composicionActual.partsList[i].y,
                  ScaleX: composicionActual.partsList[i].scaleX,
                  ScaleY: composicionActual.partsList[i].scaleY,
                  Rotation: composicionActual.partsList[i].rotation,
                  Source1: "./userData/" + hiddenData[0] + "/PieceA" + hiddenData[1] + ".png",
                  Source2: "./userData/" + hiddenData[0] + "/PieceB" + hiddenData[1] + ".png",
                  }
              );
          }

      console.log(pieces);

      var sJSON = JSON.stringify({
          auth: {
              userID: Cookies.get("userID"),
              accessToken: Cookies.get("accessToken")
          },
          composition: {pieces}
      });

      console.log("sJSON: " + sJSON);

      $.ajax({
          url: "/methods/getSearchResults",
          type: 'GET',
          data: sJSON,
          success:function(data, textStatus, jqXHR){
              console.log(data);
              //ACÁ TIENE EN DATA LOS RESULTADOS DE BÚSQUEDA.
              //Debería ser un arreglo de JSONS, cada JSON es igual al de la BD.
              //(Mandé los JSONS completos por si en el futuro necesitamos
              //extraer otra información.)
          },
          error:function(jqXHR, textStatus, errorThrown ){
              console.log(errorThrown);
          }
      });
}