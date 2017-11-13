

function searchPieces(){
  changeContainers();
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