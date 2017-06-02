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

//BEGIN SETTING UP STUFF AFTER LOADING THE HTML!
$(window).on("load",function(){
	//var heightPer = $(window).height() - $(".top-nav").height();
	heightPer = 600; //Height for the tool's menu. 

	//Set up the height for the tools' menu.
	$(".toolset").height(heightPer);

	//Let's position the editor in the row:
	var distanceTopEditor = 20;
	$("#editor").css("top",distanceTopEditor).css("height", 600);


	//Setting up the canvas size.
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
	surfaceF.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	surfaceRS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	surfaceB.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	surfaceLS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));
	surfaceS.cache(0,0,$("#leCanvas").attr("width"),$("#leCanvas").attr("height"));

 	updateView($("#changeView select"));

 	$( "#pointerRadius #slider" ).slider({
 		 min: 1,
 		 max: 100,
 		 value: 3
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
		console.log("the canvas was mousedown at "+event.stageX+","+event.stageY);
		createPieceG.pointer.x = event.stageX;
		createPieceG.pointer.y = event.stageY;
		createPieceG.drawing = true;
		//canvasCycle();
	});


	stage.addEventListener("stagemousemove", function handleMouseMove(event) {
		//console.log("the canvas was pressmove at "+event.stageX+","+event.stageY);
		  createPieceG.pointer.x = event.stageX;
		  createPieceG.pointer.y = event.stageY;
		//canvasCycle();

	});

	stage.addEventListener("stagemouseup", function(event) {
		console.log("the canvas was pressup at "+event.stageX+","+event.stageY);
		//Break the stroke.
		createPieceG.originCaptured = false;
		createPieceG.drawing = false;
		//canvasCycle();
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
        
         console.log("paused...")
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
    //brush.graphics.clear();
    brush.graphics.clear();
    brush.graphics.setStrokeStyle($( "#pointerRadius #slider" ).slider( "option", "value" ),"round", "round", 10); ////stroke style
 // brushStyle.setStrokeStyle(2); ////stroke style
 	brush.graphics.beginStroke("#222121");//stroke color
    //draw line from the origin to the most recent pointer position.
    brush.graphics.moveTo(createPieceG.origin.x, createPieceG.origin.y);
    brush.graphics.lineTo(createPieceG.pointer.x, createPieceG.pointer.y);
    createPieceG.selectedView.updateCache(createPieceG.toolSelected=="eraser"?"destination-out":"source-over");
    //Update origin
    createPieceG.origin.x = createPieceG.pointer.x;
    createPieceG.origin.y = createPieceG.pointer.y;

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
    /*console.log("btn clicked");
    $.ajax({
        url: "/methods/createPiece",
        type: 'POST',
        data: JSON.stringify({
            auth: {
                userID: Cookies.get("userID"),
                accessToken: Cookies.get("accessToken")
            },
            piece: {
                owner_id: Cookies.get("userID"),
                name: "Ricardo"
            }
        }),
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("piece created")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });*/

    var imageFront = surfaceF.getCacheDataURL();
    var imageSide =  surfaceB.getCacheDataURL();

    $.ajax({
        url: "/methods/saveCreatedImageFile",
        type: 'POST',
        data: JSON.stringify({ type: "Piece", image1: imageFront, image2: imageSide }),
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
enCanvas: loads an image in the canvas.

img: the image received to be loaded in the canvas.

returns: void
*/
function enCanvas(img) {

    var bitmap = new createjs.Bitmap(img.src);
    createPieceG.selectedView.addChild(bitmap);
    stage.update();
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
            enCanvas(img);
        };
    };
};


