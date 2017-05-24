//CREATED BY IRVIN UMAÑA
/*
Global variables for createPiece.
*/
createPieceG = {
	origin: {x: 0, y:0},
	pointer: {x: 0, y: 0}, //Universal pointer, contains position for the mouse, or whatever else pointer used (touch).
	originCaptured: false, 
	drawing: false,
	toolSelected: "pencil",
	stage: new createjs.Stage("leCanvas"),
	canvasStandardWidth: 1000,
	canvasStandardHeight: 1000



}

//BEGIN SETTING UP STUF!

$(window).on("load",function(){
	var heightPer = $(window).height() - $(".top-nav").height();

	//Set up the canvas and it's menu to viewport's heght minus the top nav height. 
	$(".toolset").height(heightPer); 

	//Let's position the editor in the row:
	var distanceTopEditor = 20;

	$("#editor").css("left",$(window).width()/12).css("top",distanceTopEditor).width( $(window).width()/12*10 ).css("height", (heightPer-distanceTopEditor));
	

	//$("#leCanvas").attr("width", $("#editor").width()).attr("height", heightPer - distanceTopEditor); 
	$("#leCanvas").attr("width", createPieceG.canvasStandardWidth).attr("height", createPieceG.canvasStandardHeight); 

    initiate();
});


//Toggle open, or close the tool's menu. 
$( "#editor-menu-handle" ).click(function() {
  $( "#editor-menu-content " ).toggle( "slow", function() {
  });
});


$( "#attributes-menu-handle" ).click(function() {
  $( "#attributes-menu-content" ).toggle( "slow", function() {
  });
});


//Set up tools selector
$(".tool").on("click",function(){
	$(".tool").removeClass("selectedTool");
	$(this).addClass("selectedTool");
	createPieceG.toolSelected = this.id+"";
	console.log(createPieceG.toolSelected);
});

var stage = new createjs.Stage("leCanvas");
var surface = new createjs.Container();


 var brushStyle = new createjs.Graphics();
 brushStyle.setStrokeStyle(2,"round", 1); ////stroke style
 // brushStyle.setStrokeStyle(2); ////stroke style
 brushStyle.beginStroke("#222121");//stroke color
 var brush = new createjs.Shape(brushStyle);
 surface.addChild(brush);

 function initiate() {
 	$( "#pointerRadius #slider" ).slider({
 		 min: 1,
 		 max: 10,
 		 value: 3
	});
	//$( "#pointerRadius #slider" ).slider( "values", [ 1, 55] );
	surface.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	stage.addChild(surface);

	/*
	stage.addEventListener("stagemousedown", function(event) {
	    console.log("the canvas was stagemousedown at "+event.stageX+","+event.stageY);

	})*/

	stage.addEventListener("stagemousedown", function(event) {
		console.log("the canvas was mousedown at "+event.stageX+","+event.stageY);
		createPieceG.pointer.x = event.stageX;
		createPieceG.pointer.y = event.stageY;
		createPieceG.drawing = true;
		canvasCycle();
	});


	stage.addEventListener("stagemousemove", function handleMouseMove(event) {
		//console.log("the canvas was pressmove at "+event.stageX+","+event.stageY);
		  createPieceG.pointer.x = event.stageX;
		  createPieceG.pointer.y = event.stageY;
		canvasCycle();
	  	
	});

	stage.addEventListener("stagemouseup", function(event) {
		console.log("the canvas was pressup at "+event.stageX+","+event.stageY);
		//Break the stroke. 
		createPieceG.originCaptured = false;
		createPieceG.drawing = false;
		canvasCycle();
	});
	

}

 

//createjs.Ticker.addEventListener("tick", handleTick);
createjs.Ticker.interval = 10; // FPS

 function handleTick(event) {
     // Actions carried out each tick (aka frame)

 	//canvasCycle();

     if (!event.paused) {
         // Actions carried out when the Ticker is not paused.
     }
 }

function canvasCycle(){
    if (createPieceG.drawing) {
     	console.log("TICK AND DRAWING");
	     if(createPieceG.originCaptured){
	     	drawStroke(brush);
	     }else{
	     	
	     	captureOrigin();
	     }
  	 }

     stage.update();
}




 function captureOrigin(){
 	createPieceG.origin = {x: createPieceG.pointer.x, y: createPieceG.pointer.y};
 	createPieceG.originCaptured = true;
 }

 function drawStroke(brush){
    //clear previous line
    //brush.graphics.clear();
    brush.graphics.clear();
    brush.graphics.setStrokeStyle($( "#pointerRadius #slider" ).slider( "option", "value" ),"round", "round", 10); ////stroke style
 // brushStyle.setStrokeStyle(2); ////stroke style
 	brush.graphics.beginStroke("#222121");//stroke color
    //draw line from the origin to the most recent pointer position. 
    brush.graphics.moveTo(createPieceG.origin.x, createPieceG.origin.y);
    brush.graphics.lineTo(createPieceG.pointer.x, createPieceG.pointer.y);
    surface.updateCache(createPieceG.toolSelected=="eraser"?"destination-out":"source-over");
    //brush.graphics.clear();
    //console.log("originX: "+createPieceG.origin.x+", originY: "+createPieceG.pointer.x);

    //Update origin
    createPieceG.origin.x = createPieceG.pointer.x;
    createPieceG.origin.y = createPieceG.pointer.y;

}


