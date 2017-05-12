//CREATED BY IRVIN UMAÑA

$(window).on("load",function(){
	var heightPer = $(window).height() - $(".top-nav").height();
	$(".toolset").height(heightPer); //Set up the canvas and it's menu to viewport's heght minus the top nav height. 
	//$("#leCanvas").height(5000).width(5000); 
});



$( "#editor-menu-handle" ).click(function() {
  $( "#editor-menu-content " ).toggle( "slow", function() {
    // Animation complete.
  });
});

function init() {
    // code here.
}

/*
Global variables for createPiece.
*/
createPieceG = {
	origin: {x: 0, y:0},
	pointer: {x: 0, y: 0}, //Universal pointer, contains position for the mouse, or whatever else pointer used (touch).
	originCaptured: false, 
	drawing: false
}

var stage = new createjs.Stage("leCanvas");

/*
stage.addEventListener("stagemousedown", function(event) {
    console.log("the canvas was stagemousedown at "+event.stageX+","+event.stageY);

})*/



stage.addEventListener("stagemousedown", function(event) {
	console.log("the canvas was mousedown at "+event.stageX+","+event.stageY);
  createPieceG.pointer.x = event.stageX;
  createPieceG.pointer.y = event.stageY;
  createPieceG.drawing = true;
});


stage.addEventListener("stagemousemove", function handleMouseMove(event) {
	//console.log("the canvas was pressmove at "+event.stageX+","+event.stageY);
	  createPieceG.pointer.x = event.stageX;
	  createPieceG.pointer.y = event.stageY;
});

stage.addEventListener("stagemouseup", function(event) {
	console.log("the canvas was pressup at "+event.stageX+","+event.stageY);
	//Break the stroke. 
	createPieceG.originCaptured = false;
	createPieceG.drawing = false;
});

 var brushStyle = new createjs.Graphics();
 brushStyle.setStrokeStyle(2,"round", 1); ////stroke style
 // brushStyle.setStrokeStyle(2); ////stroke style
 brushStyle.beginStroke("#0000FF");//stroke color
 var brush = new createjs.Shape(brushStyle);
 stage.addChild(brush);
 

createjs.Ticker.addEventListener("tick", handleTick);
createjs.Ticker.interval = 20; //50 FPS

 function handleTick(event) {
     // Actions carried out each tick (aka frame)

     if (createPieceG.drawing) {
     	console.log("TICK AND DRAWING");
	     if(createPieceG.originCaptured){
	     	drawStroke(brush);
	     }else{
	     	
	     	captureOrigin();
	     }
  	 }

     stage.update();

     if (!event.paused) {
         // Actions carried out when the Ticker is not paused.
     }
 }





 function captureOrigin(){
 	createPieceG.origin = {x: createPieceG.pointer.x, y: createPieceG.pointer.y};
 	createPieceG.originCaptured = true;
 }

 function drawStroke(brush){
    //clear previous line
    //brush.graphics.clear();

    //draw line from the origin to the most recent pointer position. 
    brush.graphics.moveTo(createPieceG.origin.x, createPieceG.origin.y);
    brush.graphics.lineTo(createPieceG.pointer.x, createPieceG.pointer.y);
    //console.log("originX: "+createPieceG.origin.x+", originY: "+createPieceG.pointer.x);

    //Update origin
    createPieceG.origin.x = createPieceG.pointer.x;
    createPieceG.origin.y = createPieceG.pointer.y;

}


