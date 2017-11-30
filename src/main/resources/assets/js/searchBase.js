var resultsCounter = 0;

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
              //console.log("data: " + data);
              //console.log("type: " + typeof data);
              prepareResults(JSON.parse(data));

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

//DUMMY
function getSimScore(i)
{
    scores = [76, 65, 61, 50, 44, 10]

    try
    {
        return scores[i]
    }
    catch(e)
    {
        return 0;
    }
}

function prepareResults(data){
	var size = data.length;

	resultsCounter = 0;

	for(var i= 0; i<size; i++){
		var scientificName = data[i]['attributes']['Scientific Name'];
		var similarityScore = data[i]['similarityScore'];
    var compositionId = data[i]['_id'];
		var resultingImage = new Image();
		resultingImage.src = "data:image/png;base64," + data[i]['imageBinaryCover'];
    recoverRelatedPhotos(compositionId);
		loadResultsToCards(resultingImage,scientificName, similarityScore, compositionId);
	}
	//addListenerToResultsCard();

}


function recoverRelatedPhotos(compositionId){
  //Se busca con 
        $.ajax({
          url: "/methods/loadPhotos2",
          type: 'POST',
          data: compositionId,
          contentType: "text/plain",
          success:function(data, textStatus, jqXHR){
              appedRelatedPhotos(data, compositionId);
          },
          error:function(jqXHR, textStatus, errorThrown ){
              console.log(errorThrown);
          }
      });
}

function appedRelatedPhotos(imgArray, compositionId){
    var bla = imgArray.substring(1, imgArray.length - 1).split(",");
  $("#hiddenImages").append("<div id=\"images"+compositionId+"\"></div>");

  for(var i = 0; i < bla.length; i++){
    var newImage = new Image();
    newImage.src = "data:image/png;base64," + bla[i];
    $("#images"+compositionId+"").append(newImage);
    $("#images"+compositionId+" img").addClass("resultsRelatedImages");
  }
}

function loadResultsToCards(resultingImage, scientificName, similarityScore, compositionId){

	var template = $( ".resultsCardSpaceTemplate" );
	//var author = data[i].author; TODO: NO ESTÁ, SOLO EL ID
	
	var newCard = template.clone().addClass("oldSearch").removeClass("resultsCardSpaceTemplate").addClass("currentNewCard").delay(1000).appendTo( ".resultsBlockRow" );
	newCard.removeClass("hideInfo");
	addListenerToResultsCard(newCard);
	//$(".currentNewCard #authorField").text("").text(author);
  $(".currentNewCard .compositionId").text("").text(compositionId);
	$(".currentNewCard .sciNameFieldCard").text("").text(scientificName);
	$(".currentNewCard .similarityScorFieldCard").text("").text(similarityScore + "%")
	$(".currentNewCard .resultsCardImage").empty().append(resultingImage);
	$(".currentNewCard").removeClass("currentNewCard");

}

function addListenerToResultsCard(card){
	//	$(".resultsCard").hover(function(){
	$(card).hover(function(){
	    console.log("hover called");
		$(this).addClass('selectedCard');
		var scientificName = $(".selectedCard .sciNameFieldCard").text();
		//var author  = $(".selectedCard  .authorFieldCard").text();
		var similarityScore = $(".selectedCard  .similarityScorFieldCard").text();
    var compositionId = $(".selectedCard .compositionId").text();
		$(this).removeClass('selectedCard');	

		//$("#authorField").text("").text(author);
    var chosenImages =  $("#images"+compositionId+"");
    var cloneImages = chosenImages.clone();
    $("#relatedImages").empty().append(cloneImages);


		$("#sciNameField").text("").text(scientificName);
		$("#similarityScorField").text("").text(similarityScore);

		//$("#resultInformationBlock").toggleClass("hideInfo");
	});
}

function changeContainers(){
  var searchImg = new Image();
  var canvasShot = document.getElementById('areaDeDibujo');
  searchImg.src = canvasShot.toDataURL();
  $(".searchImage").empty().append(searchImg);

  if(onSearch){
    onSearch=false;
    $(".oldSearch").remove();
    $("#hiddenImages").empty();
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