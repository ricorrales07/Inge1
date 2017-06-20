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
	canvasStandardHeight: 1000,
	draggedBitmapFirstPoint: {x: 0, y: 0},
	capturedFirstBitmapPoint: false,
	newImage: null
}

//BEGIN SETTING UP STUFF AFTER LOADING THE HTML!
$(window).on("load",function(){
	$( "#editor-container" ).resizable({ ghost: true }); //Make things resizable. 
	//var heightPer = $(window).height() - $(".top-nav").height();
	heightPer = 600; //Height for the tool's menu. 

	//Set up the height for the tools' menu.
	$(".toolset").height(heightPer);

	//Let's position the editor in the row:
	$("#editor-container").css("top",20).css("height", 500);


	//Setting up the canvas size.
	$("#canvasBackGround").attr("width", createPieceG.canvasStandardWidth).attr("height", createPieceG.canvasStandardHeight);
	$("#leCanvas").attr("width", createPieceG.canvasStandardWidth).attr("height", createPieceG.canvasStandardHeight);

    initiate();
});

/**
* This is the click event handler from the "Cancel" button. Redirectos to the main page. 
* @return: void. 
*/
function cancelSprite(){
	window.location.href = "/";
}

///Event listerners (with anonymous even handlers) for the different menus. 
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

//Creation a stage object (the canvas object on which we are going to draw on)
var stage = new createjs.Stage("leCanvas");


//Each of the following surfaces represent a view (instead of having different canvaces for each view). 
var surfaceF = new createjs.Container();
var surfaceRS = new createjs.Container();
var surfaceB = new createjs.Container();
var surfaceLS = new createjs.Container();
var surfaceS = new createjs.Container();


//Initial brush style. 
 var brushStyle = new createjs.Graphics();
 brushStyle.setStrokeStyle(2,"round", 1); ////stroke style
 brushStyle.beginStroke("#222121");//stroke color

//Differente brushes dedicated to one view. 

 var brushF = new createjs.Shape(brushStyle);
 var brushRS = new createjs.Shape(brushStyle);
 var brushB = new createjs.Shape(brushStyle);
 var brushLS = new createjs.Shape(brushStyle);
 var brushS = new createjs.Shape(brushStyle);


//Imágenes proxy para el canvas. 
var direction = "" + document.URL;
if(direction.charAt(direction.length-1) >= '0' && direction.charAt(direction.length-1) <= '9'){
	var bitmapFront = new createjs.Bitmap("\\assets\\images\\odo-head2.png");
	var bitmapSide = new createjs.Bitmap("\\assets\\images\\odo-zyg-head2.png");
	surfaceF.addChild(bitmapFront).set({x:150,y:250,scaleX:7,scaleY:7});
	surfaceS.addChild(bitmapSide).set({x:150,y:250,scaleX:7,scaleY:7});
}


 surfaceF.addChild(brushF);
 surfaceRS.addChild(brushRS);
 surfaceB.addChild(brushB);
 surfaceLS.addChild(brushLS);
 surfaceS.addChild(brushS);

/**
* Initiates various configurations related to the state, events, cache, and touch options. 
* @return: void. 
*/
 function initiate() {
 	//Allowing touch and caching each surface. 
 	createjs.Touch.enable(stage, false, allowDefault=true);
 	//stage.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	surfaceF.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	//surfaceRS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	//surfaceB.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	//surfaceLS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));*/
	surfaceS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));

 	updateView($("#changeView select"));

 	$( "#pointerRadius #slider" ).slider({
 		 min: 1,
 		 max: 100,
 		 value: 3
	});

	$( "#rotation-slider" ).slider({
 		 min: 0,
 		 max: 360,
 		 value: 0,
 		 change: function( event, ui ) {
			if(createPieceG.newImage != null){
				createPieceG.newImage.rotation = ui.value;
				console.log(ui.value);
			}
 		 }
	});

	updateStageListeners();
	stage.update();
}

/**
* Creates all the stage's listeners, with anonymous event handlers. These are mostly dedicated to capture the position
* of the cursor/pointer when differente events. 
* @return: void. 
*/
function updateStageListeners(){

	stage.addEventListener("stagemousedown", function(event) {
		//console.log("the canvas was mousedown at "+event.stageX+","+event.stageY);
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
		//console.log("the canvas was pressup at "+event.stageX+","+event.stageY);
		//Break the stroke.
		createPieceG.originCaptured = false;
		createPieceG.drawing = false;
	});
}


//Setting up the configuration for the ticker instance from create JS. This 
// will generate a tick at a certain FPS. 
createjs.Ticker.addEventListener("tick", handleTick);
createjs.Ticker.interval = 10; // FPS

/**
* Event handler for each tick event generated by the createJs object. 
* Executes a canvasCycle. 
* @event: the event generated by the CreateJS Ticker.
* @return: void. 
*/
 function handleTick(event) {
     // Actions carried out each tick (aka frame)
 	canvasCycle();
     if (!event.paused) {
         // Actions carried out when the Ticker is not paused.
        
         //console.log("paused...")
     }
 }

/**
* Updates the stage (redraws the state of the canvas), and decides if an 
* initial reference point is needed or if it can start drawing strokes captured from the user. 
* @return: void. 
*/
function canvasCycle(){
    if (createPieceG.drawing) {
     	console.log("TICK AND DRAWING");
	     if(createPieceG.originCaptured){
	     	drawStroke(createPieceG.selectedBrush);
	     }else{
	     	captureOrigin();
	     }
	     
  	 }
  	 stage.update();
     
}

/**
* Captures the initial drawing point. This is used at the very beggin of a stroke to begin drawing it on the surface. 
* @return: void. 
*/
 function captureOrigin(){
 	createPieceG.origin = {x: createPieceG.pointer.x, y: createPieceG.pointer.y};
 	createPieceG.originCaptured = true;
 }

 /**
 * On a single cicle from the main loop (whether from the TICKER or evens), this function updates 
 * the selected surface to draw a portion of a stroke made by the user. 
 * @brush:  this is the brush that is going to be used to capture the stroke by the user. Each surface has 
 * it's own brush object. 
 * @return: void
 */

 function drawStroke(brush){
 	
    //clear previous line
    brush.graphics.clear();
    brush.graphics.setStrokeStyle($( "#pointerRadius #slider" ).slider( "option", "value" ),"round", "round", 10); ////stroke style
 	brush.graphics.beginStroke("#222121");//stroke color
    //draw line from the origin to the most recent pointer position.
    brush.graphics.moveTo(createPieceG.origin.x, createPieceG.origin.y);
    brush.graphics.lineTo(createPieceG.pointer.x, createPieceG.pointer.y);

    createPieceG.selectedView.updateCache(createPieceG.toolSelected=="eraser"?"destination-out":"source-over");
    //Update origin
    createPieceG.origin.x = createPieceG.pointer.x;
    createPieceG.origin.y = createPieceG.pointer.y;
    //console.log("Something else was drawn");

}

/**
* Updates the correct vuew (surface) depending on the selection on the dropdown. 
* This includes updated the correct brush as well. All of this done after removing all chrildren from stage to avoid overlapping. 
* @select: this is the select element to choose the view. 
* @return: void. 
*/
function updateView(select){
	stage.removeAllChildren();
	switch( $(select).val()  ){
		case "canvasFront":
			createPieceG.selectedView = surfaceF;
			createPieceG.selectedBrush = brushF;
			stage.addChild(surfaceF);
			break;
		case "canvasRightSide":
			 createPieceG.selectedView = surfaceRS;
			 createPieceG.selectedBrush = brushRS;
			 stage.addChild(surfaceRS);
			break;
		case "canvasBack":
		 	createPieceG.selectedView = surfaceB;
		 	createPieceG.selectedBrush = brushB;
		 	stage.addChild(surfaceB);
			break;
		case "canvasLeftSide":
		 	 createPieceG.selectedView = surfaceLS;
		 	 createPieceG.selectedBrush = brushLS;
		 	 stage.addChild(surfaceLS);
			break;
		case "canvasSide":
		 	 createPieceG.selectedView = surfaceS;
		 	 createPieceG.selectedBrush = brushS;
		 	 stage.addChild(surfaceS);
			break;
		default:
			createPieceG.selectedView = surfaceF;
			createPieceG.selectedBrush = brushF;
			stage.addChild(surfaceF);
			break;
	}
}

function saveCreation(){
    var imageFront = surfaceF.getCacheDataURL();
    var imageSide =  surfaceS.getCacheDataURL();

    $.ajax({
        url: "/methods/saveCreatedImageFile",
        type: 'POST',
        data: "Piece," + imageFront + "," + imageSide,
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("image saved in server directory")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}

function increaseFileNameCounter(){
    $.ajax({
        url: "/methods/increaseFileNameCounter",
        type: 'POST',
        success:function(data, textStatus, jqXHR){
            console.log("Counter increased")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}

/*
openFile: Using FileReader, reads as data URL the event target
          (supposedly an image) to load it into the canvas
          using the previously defined function "enCanvas".

event: An image is selected using an input (type="file") tag

returns: void
*/
var openFile = function(event) {
    var input = event.target;
    var reader = new FileReader();
    reader.readAsDataURL(input.files[0]);
    reader.onloadend = function(event){
        var dataURL = reader.result;
        var img = new Image();
        img.src = event.target.result;
        img.onload = function()
        {
            addImageToCanvas(img);
        };
    };
};

/*
addImageToCanvas: loads an image in the canvas.

img: the image received to be loaded in the canvas.

returns: void
*/
function addImageToCanvas(img){
	createPieceG.newImage = new createjs.Bitmap(img.src);
	var graphics = new createjs.Graphics().beginFill("#000000").drawRect(0, 0, createPieceG.newImage.image.width+10, createPieceG.newImage.image.height+10)
	.beginFill("#ffffff").drawRect(2, 2, createPieceG.newImage.image.width+8, createPieceG.newImage.image.height+8);
 	var shape = new createjs.Shape(graphics);


	createPieceG.newImage = new createjs.Bitmap(img.src);
	createPieceG.newImage.regX = createPieceG.newImage.image.width/2;
	createPieceG.newImage.regY = createPieceG.newImage.image.height/2;
	createPieceG.newImage.x = createPieceG.newImage.image.width/2;
	createPieceG.newImage.y = createPieceG.newImage.image.height/2;
	stage.addChild(createPieceG.newImage);

	createPieceG.newImage.addEventListener("pressmove", function(event) {
		if(createPieceG.capturedFirstBitmapPoint){
			event.target.x += event.stageX - createPieceG.draggedBitmapFirstPoint.x;
			event.target.y += event.stageY - createPieceG.draggedBitmapFirstPoint.y;
			createPieceG.draggedBitmapFirstPoint.x = event.stageX;
			createPieceG.draggedBitmapFirstPoint.y = event.stageY;
			console.log("the canvas was mousedown at "+event.localX+","+event.localX);
		}else{
			createPieceG.draggedBitmapFirstPoint.x = event.stageX;
			createPieceG.draggedBitmapFirstPoint.y = event.stageY;
			createPieceG.capturedFirstBitmapPoint = true;
		}
	});

	createPieceG.newImage.addEventListener("pressup", function(event) {
		createPieceG.capturedFirstBitmapPoint = false;
	});
	
	stage.update();
    console.log("Image added*******");
}


function stickImageToSurface(){
	if(createPieceG.newImage != null){
		createPieceG.selectedBrush.graphics.clear();
		createPieceG.selectedBrush.graphics.beginBitmapFill(createPieceG.newImage.image,"no-repeat",createPieceG.newImage.getMatrix());
		//createPieceG.selectedBrush.graphics.drawRect(createPieceG.newImage.x,createPieceG.newImage.y,createPieceG.newImage.image.width, createPieceG.newImage.image.height);
		createPieceG.selectedBrush.graphics.drawRect(0,0,createPieceG.canvasStandardWidth, createPieceG.canvasStandardHeight);
		createPieceG.selectedView.updateCache("source-over");
		console.log("Image pasted to surface*******");
		cancelImageToSurface();
    }
}


function cancelImageToSurface(){
	if(createPieceG.newImage != null){
		stage.removeChild(createPieceG.newImage);
		createPieceG.newImage = null;
		console.log("Image bitmap deleted*******");
	}
}
