

function searchPieces(){
  getPieceSearchResults();
  changeContainers();
}

function getPieceSearchResults()
{
  var pieceType = $("#pieceType select").val();
  console.log(pieceType);
  //TODO: MODIFICAR y crear recursos para solo traer pieces por tipo. 
  /*
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
              //prepareResults(JSON.parse(data));

              //ACÁ TIENE EN DATA LOS RESULTADOS DE BÚSQUEDA.
              //Debería ser un arreglo de JSONS, cada JSON es igual al de la BD.
              //(Mandé los JSONS completos por si en el futuro necesitamos
              //extraer otra información.)
          },
          error:function(jqXHR, textStatus, errorThrown ){
              console.log(errorThrown);
          }
      });*/
}


function prepareResults(data){
  var size = data.length;

  resultsCounter = 0;


  //TODO
  for(var i= 0; i<size; i++){
    var scientificName = data[i]['attributes']['Scientific Name'];
    $.ajax({
        url: "/methods/getImageBinary",
        type: 'GET',
        data: {src: data[i]['imgSource']},
        success:function(data2, textStatus, jqXHR){
            //console.log("image binary: " + data2);
             var resultingImage = new Image();
             resultingImage.src = "data:image/png;base64," + data2//El url del resultado.
             loadResultsToCards(resultingImage);
             resultsCounter++;
        },
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
      });
  }
  //addListenerToResultsCard();

}

function loadResultsToCards(resultingImage){

  var template = $( ".resultsCardSpaceTemplate" );

  var newCard = template.clone().addClass("oldSearch").removeClass("resultsCardSpaceTemplate").addClass("currentNewCard").delay(1000).appendTo( ".resultsBlockRow" );
  newCard.removeClass("hideInfo");
  addListenerToResultsCard(newCard);
  //$(".currentNewCard #authorField").text("").text(author);
  //$(".currentNewCard .sciNameFieldCard").text("").text(scientificName);
  //$(".currentNewCard .similarityScorFieldCard").text("").text(similarityScore + "%")
  $(".currentNewCard .resultsCardImage").empty().append(resultingImage);
  $(".currentNewCard").removeClass("currentNewCard");

}

function addListenerToResultsCard(card){
  //  $(".resultsCard").hover(function(){
  $(card).hover(function(){


    $(this).addClass('selectedCard');
    var scientificName = $(".selectedCard .sciNameFieldCard").text();
    //var author  = $(".selectedCard  .authorFieldCard").text();

    /*
    var similarityScore = $(".selectedCard  .similarityScorFieldCard").text();
    $(this).removeClass('selectedCard');  */

    //$("#authorField").text("").text(author);
    $("#sciNameField").text("").text(scientificName);
    //$("#similarityScorField").text("").text(similarityScore);

    $("#resultInformationBlock").toggleClass("hideInfo");
  });
}

function changeContainers(){
  var searchImg = new Image();
  var canvasShot = document.getElementById('leCanvas');
  searchImg.src = canvasShot.toDataURL();
  $(".searchImage").empty().append(searchImg);

  if(onSearch){
    onSearch=false;
    $(".oldSearch").remove();
    $("#pieceSearchPieceContainer").toggle( "left" , function(){
      $("#pieceMakerContainer").toggle("left");
     });
  }else{
    onSearch=true;
    $("#pieceMakerContainer").toggle( "left" , function(){
      $("#pieceSearchPieceContainer").toggle("left");
     });
  }
  
}