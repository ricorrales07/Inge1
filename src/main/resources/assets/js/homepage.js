//canvas where the user drags animal pieces
var stage = new createjs.Stage("areaDeDibujo");
//these two variables are used to make up for the user not pressing images in the exact center
var diffX, diffY;
//these variable indicates the current view of the insect
var view = "front";
//this are the guidelines (it's an image)
var lineasDeGuia = new createjs.Bitmap("assets/images/cuadricula.png");
//this variable represents the current composition
var composicionActual = {partIds: [], partsList: [], matrices: [[],[]]};
//this variable represents the currently selected piece
var selected = null;
//this object is used to draw a rectangle around the currently selected piece
var grapher = new createjs.Shape();
//this structure helps us keep track of transformations performed by the user on the pieces
var lastTouchPos = [[-1,-1],[-1,-1]];

/*
selectPart: this function in called when a part is tapped or
            clicked on. It draws a rectangle around the
            selected sprite, updates the "selected" variable
            and it sets the pieceEditorLink in a ready state
            to open the selected image in the piece editor.

index: an integer that identifies the piece inside the current composition.

returns: void
*/
function selectPart(index)
{
  grapher.graphics.clear();

  selected = index;
  var part = composicionActual.partsList[index];

  var b = part.getTransformedBounds();

  grapher.graphics.beginStroke("black").drawRect(b.x, b.y, b.width, b.height);

  var link = document.getElementById("pieceEditorLink");
  link.setAttribute("href", "/editPiece?pieceId=" + composicionActual.partIds[selected]);
}

/*
unselectPart: this function is called when a piece is moved or scaled.
              It updates the value of "selected" to null, deletes the
              rectangle around the previously selected image, and sets
              the pieceEditorLink to open up a blank canvas, in case it
              is pressed afterwards.

returns: void
*/
function unselectPart()
{
  selected = null;
  grapher.graphics.clear();
  var link = document.getElementById("pieceEditorLink");
  link.setAttribute("href", "/createPiece");
}

/*
drag: called by "pressmove" event. Has the effect of dragging a
      piece around the screen as the user wishes. It works both
      with mouse and touch events.

returns: void
*/
function drag(evt)
{
    evt.target.x = evt.stageX - diffX;
    evt.target.y = evt.stageY - diffY;
    console.log("dragging; x: " + evt.target.x + ", y: " + evt.target.y);
    stage.update();
}

/*
calculateDifference: sets variables diffX and diffY before a dragging phase
                     starts.

evt: the "mousepress" event that eventually generated this code to run

returns: void
*/
function calculateDifference(evt)
{
    var index;
    for (index = 0; composicionActual.partsList[index] != evt.target; index++);
    selectPart(index);
    diffX = evt.stageX - evt.target.x;
    diffY = evt.stageY - evt.target.y;
}

/*
manageKey: just a dummy that manages what happens when letter i is pressed.
           It calls addPart(), which in turn adds a piece to the canvas.

evt: keypressed event that generated the call

returns: void
*/
/*function manageKey(evt)
{
    var key = evt.which || evt.keyCode || evt.charCode; //alguna de todas va a servir
    console.log(evt.which, evt.keyCode, evt.CharCode, key);
    if (key == 105) //letter i
    {
        addSinglePartToCanvas();
    }
}*/

function addImageToCanvas(img){
    var partData = [img.src,
                    "assets/images/odo-zyg-head2.png",
                    Math.floor(Math.random() * document.getElementById("areaDeDibujo").width),
                    Math.floor(Math.random() * document.getElementById("areaDeDibujo").height),
                    2,2,0];
    addPart(partData);
}

/*
addPart: for now, a stub that just adds the same fixed pair of images.
         supposedly, one of them is a front view of a piece, and the
         other one is a side view.

returns: void
*/
function addPart(partData) //proxy
{
  console.log("creating sprite");
  var img1 = new Image();
  img1.src = partData[0];
  var img2 = new Image();
  img2.src = partData[1];

  var imgs = {
      images: [img1, img2],
      frames: [
          [0,0,img1.width,img1.height,0,img1.width/2,img1.height/2],
          [0,0,img2.width,img2.height,1,img2.width/2,img2.height/2]
      ],
      animations: {front: 0, side: 1}
  };
  var partSheet = new createjs.SpriteSheet(imgs);
  var partSprite = new createjs.Sprite(partSheet, view);

  partSprite.x = partData[2];
  partSprite.y = partData[3];

  partSprite.name = "" + createjs.UID.get();

  console.log("Sprite " + partSprite.name + " created. visible: " + partSprite.visible);

  partSprite.set({scaleX: partData[4], scaleY: partData[5], rotation: partData[6]});

  addListeners(partSprite);

  stage.addChild(partSprite);

  composicionActual.partIds.push(partSprite.name);
  composicionActual.partsList.push(partSprite);
  composicionActual.matrices[0].push(partSprite.getMatrix());
  composicionActual.matrices[1].push(partSprite.getMatrix());

  selectPart(composicionActual.partsList.length-1);
}

/*
addListeners: adds listeners to a piece.

item: a piece.

returns: void
*/
function addListeners(item)
{
  item.on("mousedown", handleMouseDown);
  item.on("pressmove", handlePressMove);
  item.on("pressup", handlePressUp);
}

/*
handlePressUp: called when the user stops dragging or scaling an elmeent.
               It resets the lastTouchPos to it's original negative value.

evt: the pressup even that generated it.

returns: void
*/
function handlePressUp(evt)
{
  lastTouchPos = [[-1,-1],[-1,-1]];
}

/*
handlePressMove: called when the mouse moves while clicked, or when
                 dragging fingers on the screen. it either drags the
                 image around or calls transorm() to scale or rotate
                 the image.

evt: the "pressmove" event that generates the calls

returns: void
*/
function handlePressMove(evt)
{
  unselectPart();
  if(!evt.isTouch || evt.nativeEvent.touches.length == 1)
  {
    console.log("drag");
    drag(evt);
  }
  else if (lastTouchPos[0][0] == -1 || lastTouchPos[0][1] == -1 || lastTouchPos[1][0] == -1 || lastTouchPos[1][1] ==-1) {
    lastTouchPos =
      [[adjustCoordinateX(evt.nativeEvent.touches[0].pageX),
        adjustCoordinateY(evt.nativeEvent.touches[0].pageY)],
        [adjustCoordinateX(evt.nativeEvent.touches[1].pageX),
        adjustCoordinateY(evt.nativeEvent.touches[1].pageY)]];
  }
  else
  {
    transform(evt);
  }
}

/*
adjustCoordinateX: converts an X page coordinate into the local canvas
                   coordinate system.

coord: integer representing an X coordinate in the page coordinate system.

returns: the equivalent coordinate in the canvas coordinate system.
*/
function adjustCoordinateX(coord)
{
  var e = document.getElementById("areaDeDibujo");
  var b = e.getBoundingClientRect();
  return (coord - b.left) * 1024 / (b.right - b.left);
}

/*
adjustCoordinateY: converts an Y page coordinate into the local canvas
                   coordinate system.

coord: integer representing an Y coordinate in the page coordinate system.

returns: the equivalent coordinate in the canvas coordinate system.
*/
function adjustCoordinateY(coord)
{
  var e = document.getElementById("areaDeDibujo");
  var b = e.getBoundingClientRect();
  return (coord - b.top) * 512 / (b.bottom - b.top);
}

/*
transform: scales and/or rotates the image when multitouch gestures
           are used (e.g. pinching). This is done by computing the
           answer to a pre-solved linear algebra problem.

evt: the "pressmove" event that generated the call

returns: void
*/
function transform(evt)
{
  var x = [lastTouchPos[0][0], adjustCoordinateX(evt.nativeEvent.touches[0].pageX)];
  var y = [lastTouchPos[0][1], adjustCoordinateY(evt.nativeEvent.touches[0].pageY)];
  var z = [lastTouchPos[1][0], adjustCoordinateX(evt.nativeEvent.touches[1].pageX)];
  var w = [lastTouchPos[1][1], adjustCoordinateY(evt.nativeEvent.touches[1].pageY)];

  console.log("f1i: (" + x[0] + ", " + y[0] + "), f2i: (" + z[0] + ", " + w[0]
              + "), f1f: (" + x[1] + ", " + y[1] + "), f2f: (" + z[1] + ", " + w[1] + ")");

  var c1 = (x[1]+z[1])/2 - (x[0]+z[0])/2;
  var c2 = (y[1]+w[1])/2 - (y[0]+w[0])/2;

  evt.target.x += c1;
  evt.target.y += c2;

  console.log("stage width: " + stage.width + ", stage height: " + stage.height);

  console.log("stageXY: (" + evt.stageX + ", " + evt.stageY + "), realXY1: ("
  + evt.nativeEvent.touches[0].pageX + ", " + evt.nativeEvent.touches[0].pageY + "), realXY2: ("
  + evt.nativeEvent.touches[1].pageX + ", " + evt.nativeEvent.touches[1].pageY + "), mouseX: ("
  + stage.mouseX + ", " + stage.mouseY + ")");

  var a = -((-w[0]+y[0])*(-w[1]+y[1])-(-x[0]+z[0])*(x[1]-z[1]))/((w[0]-y[0])*(-w[0]+y[0])-(-x[0]+z[0])*(-x[0]+z[0]));
  var c = -(w[1]*x[0]-w[0]*x[1]+x[1]*y[0]-x[0]*y[1]-w[1]*z[0]+y[1]*z[0]+w[0]*z[1]-y[0]*z[1])/(w[0]*w[0]+x[0]*x[0]-2*w[0]*y[0]+y[0]*y[0]-2*x[0]*z[0]+z[0]*z[0]);

  // Previous divisions have 0 denominator if and only if both fingers are on
  // the same spot (i.e. should never happen).

  var k = Math.sqrt(a*a+c*c);
  var alpha = Math.acos(a/k) * 180 / Math.PI;

  if ((w[0]-y[0])*(x[1]-c1-x[0])-(z[0]-x[0])*(y[1]-c2-y[0]) < 0)
    alpha = -alpha;

  console.log("k: " + k + ", alpha: " + alpha);

  evt.target.scaleX *= k;
  evt.target.scaleY *= k;

  evt.target.rotation += alpha;

  lastTouchPos =
    [[adjustCoordinateX(evt.nativeEvent.touches[0].pageX),
      adjustCoordinateY(evt.nativeEvent.touches[0].pageY)],
      [adjustCoordinateX(evt.nativeEvent.touches[1].pageX),
      adjustCoordinateY(evt.nativeEvent.touches[1].pageY)]];
}

/*
handleMouseDown: prepares pages data structures to drag the image around or
                 transform them.

evt: "mousedown" event that called the function

returns: void
*/
function handleMouseDown(evt)
{
  if(!evt.isTouch || evt.nativeEvent.touches.length == 1)
  {
    calculateDifference(evt);
  }
  else {
    lastTouchPos =
      [[adjustCoordinateX(evt.nativeEvent.touches[0].pageX),
        adjustCoordinateY(evt.nativeEvent.touches[0].pageY)],
        [adjustCoordinateX(evt.nativeEvent.touches[1].pageX),
        adjustCoordinateY(evt.nativeEvent.touches[1].pageY)]];
  }
}

/*
handleTick: this function is called each time a "tick" happens (EaselJS is
            made especially for animations). It simply updates the stage if
            it is necessary.

evt: the event that generated the call.

returns: void
*/
function handleTick(evt)
{
    stage.update(evt);
}

/*
changeView: changes between the front and the side view of the insect.

returns: void
*/
function changeView()
{
    var s = selected;
    unselectPart();
    var btn = document.getElementById("changeViewButton");
    btn.textContent = (btn.textContent == "Front view") ? "Side view" : "Front view";

    view = (view == "front") ? "side" : "front";
    var viewNum = (view == "front") ? 0 : 1;

    for(var i = 0; i < composicionActual.partsList.length; i++)
    {
      composicionActual.matrices[(viewNum == 0) ? 1 : 0][i] = composicionActual.partsList[i].getMatrix();
      composicionActual.partsList[i].gotoAndStop(view);
      composicionActual.matrices[viewNum][i].decompose(composicionActual.partsList[i]);
    }
    if (s != null)
      selectPart(s);
}

/*
init: this function is called when the page is first loaded. It simply
      initializes some structures.

returns: void
*/
function init()
{
    console.log("init");
    createjs.Touch.enable(stage, false, allowDefault=true);

    lineasDeGuia.visible = false;
    stage.addChild(lineasDeGuia).set({x:0,y:0});

    stage.addChild(grapher);

    stage.update();

    createjs.Ticker.timingMode = createjs.Ticker.RAF;
    createjs.Ticker.addEventListener("tick", handleTick);

    //window.onkeypress = manageKey;
}

/*
guidelines: shows or hides guidelines.

returns: void
*/
function guidelines()
{
    lineasDeGuia.visible = !lineasDeGuia.visible;
}

/*
function nombre(auth){

    var url = '/methods/pedacitoX'; // the script where you handle the form input.

    var auth = $.cookie('authentication'); //nombre de la cookie

    $.ajax({
        url: url,
        type: 'POST', //GET
        data: {authorization:auth},
        success:function(data, textStatus, jqXHR){

            $('#FOLDER_SELECT').html(data.html); //Mete los datos.

        },
        error:function(jqXHR, textStatus, errorThrown ){


        }
    });
}
*/

function saveCompositionImage(){
    console.log("Test");
    var comp = document.getElementById("areaDeDibujo");
    $.ajax({
        url: "/methods/saveCreatedImageFile",
        type: 'POST',
        data: "Composition," + comp.toDataURL(),
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("image saved in server directory")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}

function saveCompositionData(){
    console.log("Test");
    var data = [];
    var pieces = "{\n";
    for(i = 0; i < composicionActual.partsList.length; i++){
        pieces += "\"piece" + i + "\": {\n ";
        pieces += "\"PositionX\": \"" + composicionActual.partsList[i].x + "\", ";
        pieces += "\"PositionY\": \"" + composicionActual.partsList[i].y + "\", ";
        pieces += "\"ScaleX\": \"" + composicionActual.partsList[i].scaleX + "\", ";
        pieces += "\"ScaleY\": \"" + composicionActual.partsList[i].scaleY + "\", ";
        pieces += "\"Rotation\": \"" + composicionActual.partsList[i].rotation + "\", ";
        pieces += "\"Source1\": \"" + (composicionActual.partsList[i].spriteSheet._images["0"].src).substr(21) + "\", ";
        pieces += "\"Source2\": \"" + (composicionActual.partsList[i].spriteSheet._images["1"].src).substr(21) + "\"\n}";
        if(i < composicionActual.partsList.length - 1){
            pieces += ",\n";
        }
    }
    pieces += "\n}";
    $.ajax({
        url: "/methods/saveCompositionData",
        type: 'POST',
        data: JSON.stringify({
            auth: {
                userID: Cookies.get("userID"),
                accessToken: Cookies.get("accessToken")
            },
            composition: JSON.parse(pieces)
        }),
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("image saved in server directory")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}

function loadComposition(){
    $.getJSON("assets/imagesData/Composition0.json", function(pieces){
        var real = pieces.composition;
        $.each(real, function(attribute, value){
            if(attribute != "_id") {
                var partData = [value.Source1, value.Source2, value.PositionX,
                    value.PositionY, value.ScaleX, value.ScaleY, value.rotation];
                addPart(partData);
            }
        })
        
    });

    /*$.ajax({
        url: "/methods/loadSavedComposition",
        type: 'POST',
        data: pieces,
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("Composition reloaded succesfullt")},
        error:function(jqXHR, textStatus, errorThrown){
            console.log(errorThrown);
        }
    });*/
}
