

$(document).ready(function() {



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
          data: {searchJSON: sJSON},
          success:function(data, textStatus, jqXHR){
              console.log("data: " + data);
              console.log("type: " + typeof data);
              loadResultsToCards(JSON.parse(data));
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


function searchSimilar(){

  	getSearchResults(); 
	changeContainers();

}

function getSimScore(i)
{

}

function loadResultsToCards(data){

	var template = $( ".resultsCardSpace" );
	var size = data.length;
	for(var i= 0; i<size; i++){
		var scientificName = data[i]['attributes']['Scientific Name'];
		//var author = data[i].author; TODO: NO ESTÁ, SOLO EL ID
		//var similarityScore = getSimScore(i);
		var newCard = template.clone().addClass("currentNewCard").delay(1000);
		//$(".currentNewCard #authorField").text("").text(author);
		$(".currentNewCard #sciNameField").text("").text(scientificName);
		//$(".currentNewCard #similarityScorField").text("").text(similarityScore);
		newCard.removeClass("currentNewCard");
		newCard.appendTo( ".resultsBlockRow" );
		newCard.id = "card" + i;

		$.ajax({
              url: "/methods/getImageBinary",
              type: 'GET',
              data: {src: data[i]['imgSource']},
              success:function(data2, textStatus, jqXHR){
                  console.log("image binary: " + data2);
                   var resultingImage = new Image();
                   resultingImage.src = "data:image/png;base64," + data2//El url del resultado.
                   $("#card" + i).empty().append(resultingImage);
              },
              error:function(jqXHR, textStatus, errorThrown ){
                  console.log(errorThrown);
              }
          });
	}

	$(".resultsCard").hover(function(){
		$(this).addClass('selectedCard');
		var scientificName = $(".selectedCard .sciNameFieldCard").text();
		//var author  = $(".selectedCard  .authorFieldCard").text();
		//var similarityScore = $(".selectedCard  .similarityScorFieldCard").text();
		$(this).removeClass('selectedCard');	

		//$("#authorField").text("").text(author);
		$("#sciNameField").text("").text(scientificName);
		//$("#similarityScorField").text("").text(similarityScore);

		$("#resultInformationBlock").toggleClass("hideInfo");
	});

}

function changeContainers(){
  var searchImg = new Image();
  var canvasShot = document.getElementById('areaDeDibujo');
  searchImg.src = canvasShot.toDataURL();
  $(".searchImage").empty().append(searchImg);

      if(onSearch){
        onSearch=false;
        $("#searchSimilarContainer").toggle( "left" , function(){
          $("#compositionMaker").toggle("left");
         });
      }else{
        onSearch=true;
        $("#compositionMaker").toggle( "left" , function(){
          $("#searchSimilarContainer").toggle("left");
         });
      }


   
   

}