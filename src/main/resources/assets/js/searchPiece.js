var associatedImagesToResults = [];

function searchPieces(){
  getPieceSearchResults();
  changeContainers();
}

function getPieceSearchResults()
{
  var pieceType = $("#pieceType select").val();
  console.log(pieceType);
      var params = "type="+generateTypeIndex(pieceType);
      $.ajax({
          url: "/methods/getPiecesByType",
          type: 'GET',
          data: params,
          success:function(data, textStatus, jqXHR){
              prepareImageResultsFromPiecesSearch(data);
          },
          error:function(jqXHR, textStatus, errorThrown ){
              console.log(jqXHR);
              console.log(textStatus);
              console.log(errorThrown);
          }
      });
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

function prepareImageResultsFromPiecesSearch(data){
  var piecesList = JSON.parse(data);
  var numPieces = piecesList.length;
  for(var i= 0; i < numPieces; i++){
    var id = piecesList[i]._id;
    var image_binary = piecesList[i].image_binary;
    var imageResult = new Image();
    imageResult.src = "data:image/png;base64," + image_binary;
    loadResultsToCards(imageResult);
  }
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