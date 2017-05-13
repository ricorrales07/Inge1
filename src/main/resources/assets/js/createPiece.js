//CREATED BY IRVIN UMAÑA
/*
Global variables for createPiece.
*/
createPieceG = {
	origin: {x: 0, y:0},
	pointer: {x: 0, y: 0}, //Universal pointer, contains position for the mouse, or whatever else pointer used (touch).
	originCaptured: false, 
	drawing: false,
	toolSelected: "pencil"
}

//BEGIN SETTING UP STUF!

$(window).on("load",function(){
	var heightPer = $(window).height() - $(".top-nav").height();
	//Let's position the editor in the row:
	//$("#editor").attr("left",400).attr("width",$(window).height()-400);
	$("#editor").css("left",$(window).width()/12).css("top",20).width( $(window).width()/12*10 );
	//Set up the canvas and it's menu to viewport's heght minus the top nav height. 
	$(".toolset").height(heightPer); 
	$("#leCanvas").attr("width", $("#editor").width()).attr("height", heightPer); 
	/*
    console.log("canvas html wdith (no px): "+$("#leCanvas").width());
    console.log("canvas html width attribute: "+$("#leCanvas").attr("width") );
    console.log("calculated height from window: "+$(window).width());
    console.log("width of the parent editor: "+$("#editor").width());*/
});


//Toggle open, or close the tool's menu. 
$( "#editor-menu-handle" ).click(function() {
  $( "#editor-menu-content " ).toggle( "slow", function() {
  });
});

//Set up tools selector
$(".tool").on("click",function(){
	$(".tool").removeClass("selectedTool");
	$(this).addClass("selectedTool");
	createPieceG.toolSelected = this.id+"";
	console.log(createPieceG.toolSelected);
});

function init() {
    // I dont need to wait for this to start, cuz you know, I AM FAAAAAVVZZZZZ!
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
createjs.Ticker.interval = 60; //50 FPS

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


