//canvas where the user drags animal pieces
var stage = new createjs.Stage("areaDeDibujo");
//these two variables are used to make up for the user not pressing images in the exact center
var diffX, diffY;
//this variable indicates the current view of the insect
var view = "front";
//these are the guidelines (it's an image)
var lineasDeGuia = new createjs.Bitmap("assets/images/cuadricula.png");
//this variable represents the current composition
var composicionActual = {partIds: [], partsList: [], matrices: [[],[]]};
//this variable represents the currently selected piece
var selected = null;
//this object is used to draw a rectangle around the currently selected piece
var grapher = new createjs.Shape();
//this structure helps us keep track of transformations performed by the user on the pieces
var lastTouchPos = [[-1,-1],[-1,-1]];

var savedImg = ""; //NOT FINAL

var imagesAttributes = [];

var currentCompositionID = "undefined";

var pieceLimits = [0,0,0,0,0,0]; // Head, Torso, Feet, Tail, Wings, Beak

var onSearch = false; // this is helpful to avoid using toggle in the searche's ui

var photographCompostion = [];

/*
selectPart: this function is called when a part is tapped or
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

function newComposition(){
    unselectPart();
    var totalPieces = composicionActual.partsList.length;
    var currentPiece;
    for (i = 0; i < totalPieces; i++){
        currentPiece = composicionActual.partsList[i];
        stage.removeChild(currentPiece);
    }
    currentCompositionID = "undefined";
    composicionActual = {partIds: [], partsList: [], matrices: [[],[]]};
    pieceLimits = [0,0,0,0,0];

    $(".attribute-card").each(function(i) {
        if(this.id == "optional-attribute-card" && i > 2){
            ($(this)).fadeOut("slow", function(){
                this.remove();
            });
        }
    });

    document.getElementById("scientificNameVal").value = "";
    document.getElementById("publicAttr").checked = false;
}

function deletePart() {
    if(selected != null) {
        var currentPiece = composicionActual.partsList[selected];
        composicionActual.partsList.splice(selected, 1);
        composicionActual.partIds.splice(selected, 1);
        stage.removeChild(currentPiece);
        unselectPart();
        pieceLimits[currentPiece.pieceType]--;
    }
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

function addImageToCanvas(img, front, side, id, type, partType){
    if((partType != 4 && pieceLimits[partType] == 1)
    || (partType == 4 && pieceLimits[4] == 2)){
        var typeName;
        switch (partType){
            case 0:
                typeName = "head";
                break;
            case 1:
                typeName = "torso";
                break;
            case 2:
                typeName = "feet";
                break;
            case 3:
                typeName = "tail";
                break;
            case 4:
                typeName = "wings";
                break;
            case 5:
                typeName = "beak";
                break;
        }
        alert("Is not possible to add more than " + pieceLimits[partType] + " " + typeName + " to the current composition.\n"
        + "Delete one to add another or replace it with the one that you want.");
    }else {
        pieceLimits[partType]++;
        var x;
        var y;
        if (type == "new") {
            //para que no se salga del cuadro hay que restarle el tamaño de la imagen (si da negativo, usarmos 0)
            x = Math.max(Math.floor(Math.random() * (document.getElementById("areaDeDibujo").width - img.width)), 0);
            y = Math.max(Math.floor(Math.random() * (document.getElementById("areaDeDibujo").height - img.height)), 0);
        } else {
            x = composicionActual.partsList[selected].x;
            y = composicionActual.partsList[selected].y;
        }
        var partData = [front, side, x, y, 1, 1, 0, id, partType];
        addPart(partData, type);
        modifySearchButton();
    }
}

function mirrorImage()
{
  if (selected != null)
  {
    composicionActual.partsList[selected].scaleX *= -1;
  }
}

/*
addPart: adds a part to the canvas

returns: void
*/
function addPart(partData, type)
{
  console.log("creating sprite");
  var img1 = new Image();

  //img1.src = partData[0];

  $.ajax({
  		url: "/methods/getImageBinary",
  		type: 'GET',
  		data: {src: partData[0]},
  		success:function(data, textStatus, jqXHR){
  			img1.src = "data:image/png;base64," + data;
  			console.log(partData[0] + " loaded successfully: " + data);
  		},
  		error:function(jqXHR, textStatus, errorThrown ){
  		    //TODO: mostrar un error significativo para el usuario acá
  			console.log(errorThrown);
  		}
  	});

  img1.onload = function(){
      var img2 = new Image();

      //img2.src = partData[1];

        $.ajax({
                url: "/methods/getImageBinary",
                type: 'GET',
                data: {src: partData[1]},
                success:function(data, textStatus, jqXHR){
                    img2.src = "data:image/png;base64," + data;
                    console.log(partData[1] + " loaded successfully: " + data);
                },
                error:function(jqXHR, textStatus, errorThrown ){
                    //TODO: mostrar un error significativo para el usuario acá
                    console.log(errorThrown);
                }
            });

      img2.onload = function(){
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

          partSprite.pieceType = partData[8];

          addListeners(partSprite);

          if(type == "change"){
              var currentPiece = composicionActual.partsList[selected];
              pieceLimits[currentPiece.pieceType]--;
              stage.removeChild(currentPiece);
              stage.addChild(partSprite);
              console.log("Changed image with ID: " + composicionActual.partIds[selected] + " to " + partData[7]);
              composicionActual.partIds[selected] = partData[7];
              composicionActual.partsList[selected] = partSprite;
              composicionActual.matrices[0][selected] = partSprite.getMatrix();
              composicionActual.matrices[1][selected] = partSprite.getMatrix();
          }else {
              stage.addChild(partSprite);
              composicionActual.partIds.push(partData[7]);
              console.log("Added image with ID: " + partData[7]);
              composicionActual.partsList.push(partSprite);
              composicionActual.matrices[0].push(partSprite.getMatrix());
              composicionActual.matrices[1].push(partSprite.getMatrix());
          }
          selectPart(composicionActual.partsList.length-1);
      };
  };
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
handlePressUp: called when the user stops dragging or scaling an element.
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
adjustCoordinateY: converts a Y page coordinate into the local canvas
                   coordinate system.

coord: integer representing a Y coordinate in the page coordinate system.

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
    var canvas = document.getElementById(("wildcard"));
    var img = new Image;
    img.src = document.getElementById("areaDeDibujo").toDataURL();
    img.onload = function(){
        $.ajax({
            url: "/methods/saveCreatedImageFile",
            type: 'POST',
            data: "Composition," + img.src + "," + Cookies.get("userID") + "," + currentCompositionID,
            contentType: "text/plain",
            success:function(data, textStatus, jqXHR){
                console.log("image saved in server directory: " + data);
                savedImg = data.split(",")[0];
                //console.log("savedImg: " + savedImg);
                currentCompositionID = data.split(",")[1];
            },
            error:function(jqXHR, textStatus, errorThrown ){
                console.log(errorThrown);
            }
        });
    };
}


function saveCompositionPhotographs(){
    console.log("saving photographs");

    for(var i = 0; i < imagesAttributes.length; i = i + 4) {
        //console.log("saving: " + imagesAttributes[i].split(",")[1]);
        $.ajax({
            url: "/methods/saveCreatedImageFile",
            type: 'POST',
            data: "Photo," + imagesAttributes[i].split(",")[1] + "," + imagesAttributes[i+1].split("\\")[2],
            contentType: "text/plain", //TODO: revisar esto
            success:function(data, textStatus, jqXHR){
                console.log("image saved in server directory: " + data);
                savedImg = data.split(",")[0];
                //console.log("savedImg: " + savedImg);
                currentCompositionID = data.split(",")[1];
            },
            error:function(jqXHR, textStatus, errorThrown ){
                console.log(errorThrown);
            }
        });

    }
}

function saveComp()
{
    var saved = false;
    saved = saveCompositionData();

    if(saved) {
        saveCompositionImage();

        saveCompositionPhotographs();
        alert("Composition successfully saved in the server.");
    }
}

function saveCompositionData(){
    images = [];

    for(var i = 0; i < imagesAttributes.length; i = i + 4){
        images.push(
            {
                image: "./userData/" + imagesAttributes[i+1].split("\\")[2],
                name: imagesAttributes[i+2],
                type: imagesAttributes[i+3]
            }
        );
    }

    var required = [];
    var optional = [];
    var requiredAttributes = "{\n";
    var optionalAttributes = "{\n";

    /*$("#attribute-card-list input").each(function() {
        if(($(this).type!="checkbox")){
            if($(this).val() != "") {
                text.push($(this).val());
            }
        }
    });*/

    required.push("Scientific Name");
    required.push(document.getElementById("scientificNameVal").value);

    required.push("Public");
    required.push(document.getElementById("publicAttr").checked);

    required.push("ownerId");
    required.push(Cookies.get("userID"));

    $("#attribute-card-list input[type = optional]").each(function() {
        if($(this).val() != "") {
            optional.push($(this).val())
        }
    })

    for(i = 0; i < required.length; i = i+2){
        requiredAttributes += "\"" + required[i] + "\": \"" + required[i+1] + "\"";
        if(i < required.length - 2){
            requiredAttributes += ",\n";
        }
    }

    for(i = 0; i < optional.length; i = i+2){
        optionalAttributes += "\"" + optional[i] + "\": \"" + optional[i+1] + "\"";
        if(i < optional.length - 2){
            optionalAttributes += ",\n";
        }
    }
    optionalAttributes += "\n}";

    var attributes = requiredAttributes + ", \"optional\":" + optionalAttributes + "\n}";

    //console.log(required);

    console.log("composition attributes: " + attributes);

    console.log("Test");
    var data = [];
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
            PieceType: composicionActual.partsList[i].pieceType
            }
        );
    }

    attributes = JSON.parse(attributes);

    console.log(pieces);

    var result = false;

    $.ajax({
        url: "/methods/saveCompositionData",
        type: 'POST',
        data: JSON.stringify({
            auth: {
                userID: Cookies.get("userID"),
                accessToken: Cookies.get("accessToken")
            },
            composition: {attributes, pieces, images},
            file: currentCompositionID
        }),
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            if(data == "Repeated"){
                alert("There is another composition with the sames pieces.\n" +
                    "Please change, add or delete one piece in your current" +
                    "composition to be able to save it.");
                result =  false
            } else {
                console.log("composition data saved in server directory");
                result = true
            }
        },
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
            result = false
        },
        async:false
    });

    return result;
}

function loadComposition(id){
    $.ajax({
        url: "/methods/getCompositionPieces",
        type: 'POST',
        data: id,
        contentType: "text/plain",
        success:function(data, textStatus,jqXHR){
            currentCompositionID = id;

            var result = JSON.parse(data);

            for(x in result){
                pieces = [result[x].Source1,
                    result[x].Source2,
                    result[x].positionX,
                    result[x].positionY,
                    result[x].ScaleX,
                    result[x].ScaleY,
                    result[x].Rotation,
                    result[x]._id,
                    result[x].PieceType
                ];
                pieceLimits[result[x].PieceType]++;
                addPart(pieces, "new");
            }
            modifySearchButton();
        },
        error:function(jqXHR, textStatus, errorThrown){
            console.log(errorThrown);
        }
    });

    $.ajax({
        url: "/methods/getCompositionAttributes",
        type: 'POST',
        data: id,
        contentType: "text/plain",
        success:function(data, textStatus,jqXHR){
            addAttributes(JSON.parse(data), "composition");
        },
        error:function(jqXHR, textStatus, errorThrown){
            console.log(errorThrown);
        }
    });
}

//dummy to try out search function
function trySearch(){
    console.log("trying search");
    var data = [];
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

    $.ajax({
            url: "/methods/trySearch",
            type: 'POST',
            data: JSON.stringify({
                auth: {
                    userID: Cookies.get("userID"),
                    accessToken: Cookies.get("accessToken")
                },
                composition: {pieces}
            }),
            contentType: "text/plain",
            success:function(data, textStatus, jqXHR){
                console.log("Search results: " + data);
                var results = JSON.parse(data);
                var html = "";
                for (var x in results)
                {
                  console.log(results[x]);
                  html += "<a data-dismiss=\"modal\"> <img src=\"" + results[x].imgSource.substr(20, results[x].imgSource.length) + "\" style=\"width:27%; height:27%; padding:10px; margin:10px;\" class = \"img-thumbnail\" /> </a>";
                }
                console.log(html);
                $('#resultImages').append(html);
              },
            error:function(jqXHR, textStatus, errorThrown ){
                console.log(errorThrown);
            }
        });
}

function tempSaveImg(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      imagesAttributes.push(e.target.result);
      console.log(imagesAttributes);
    };
    reader.readAsDataURL(input.files[0]);
  }
}

function tempSave() {
    /*$("input[type = file]").each(function () {
        imagesAttributes.push($(this).val());
    });*/

    $("input[type = attr]").each(function () {
        imagesAttributes.push($(this).val());
    });

    alert("Image now in the images' list to save with this composition.")
    console.log(imagesAttributes);
}

function loadPhotos(){
    $("modalImages").each(function(){
        $(this).remove();
    });
    console.log("loading photos");
    $.ajax({
        url: "/methods/loadPhotos",
        type: 'POST',
        data: currentCompositionID,
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            $('#associatedImages').append(data)
        },
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}

$("#addPieceButton").click(function() {
    $("modalImage").each(function(){
    	$(this).remove();
	});
	console.log("addPieceButton clicked");
    $.ajax({
		url: "/methods/getImagesDataInDB",
		type: 'GET',
        data: JSON.stringify({collection: "pieces", filter: "{}"}),
		success:function(data, textStatus, jqXHR){
		    console.log(data);
		    console.log("getImagesDataInDB for pieces was successful");
			$('#images').append(data);
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
});

function modifySearchButton()
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

  console.log(sJSON);

  var link = document.getElementById("searchButton");
  link.setAttribute("href", "/searchResults?searchJSON=" + sJSON);
}

$('#btnSavedImages').on('click', function()
    {
        document.getElementById('typeAttr').selectedIndex = 0;
        waitImgs();
        registeredImages(false, 'new', 'All');
    }
);

$('#btnOwnedImages').on('click', function()
    {
        document.getElementById('typeAttrOwn').selectedIndex = 0;
        waitImgs();
        registeredImages(true, 'new', 'All');
    }
);

var slideIndex = 1;

function waitImgs() {
  
  setTimeout("plusDivs(0)", 3000);
}

function plusDivs(n) {

  showDivs(slideIndex += n);

}

function showDivs(n) {
    var i;
    var j;
    var x = $(".imagenBonita");
    //var y = document.getElementsByClassName("positivo"); //Botones positivos a la izquierda
    //var z = document.getElementsByClassName("negativo"); //Botones negativos a la derecha
    if (n > x.length) {slideIndex = 1}
    if (n < 1) {slideIndex = x.length}
    for (i = 0; i < x.length; i++) { //Siempre sería la misma cantidad de imagenes que numero de botones positivos o negativos
        x[i].style.display = "none"; //Se esconden las imagenes que no se quieran mostrar por el momento
        //y[i].style.display = "none"; //Igual con los botones
        //z[i].style.display = "none";
    }

     /*for (j = -1; j <= 4; j++){
     x[slideIndex+j].style.display = "block";
     y[slideIndex+j].style.display = "inline";
     z[slideIndex+j].style.display = "inline";
     }*/


    x[slideIndex-1].style.display = "inline-block"; //Se muestran las 6 imagenes que sí
    x[slideIndex].style.display = "inline-block";
    x[slideIndex+1].style.display = "inline-block";
    x[slideIndex+2].style.display = "inline-block";
    x[slideIndex+3].style.display = "inline-block";
    x[slideIndex+4].style.display = "inline-block";
    //x[slideIndex+5].style.display = "inline";

    /*y[slideIndex-1].style.display = "inline"; //Los 6 botones positivos
    y[slideIndex].style.display = "inline";
    y[slideIndex+1].style.display = "inline";
    y[slideIndex+2].style.display = "inline";
    y[slideIndex+3].style.display = "inline";
    y[slideIndex+4].style.display = "inline";

    z[slideIndex-1].style.display = "inline"; //Y los 6 botones negativos
    z[slideIndex].style.display = "inline";
    z[slideIndex+1].style.display = "inline";
    z[slideIndex+2].style.display = "inline";
    z[slideIndex+3].style.display = "inline";
    z[slideIndex+4].style.display = "inline";*/
}


function growImg(x) {
    x.style.height = "64px";
    x.style.width = "64px";
}

function normImg(x) {
    x.style.height = "32px";
    x.style.width = "32px";
}

$('#loadCompositionButton').on('click', function()
{
    waitImgs();
    showCompositions();
});


function uploadPhotograph(event){
  var input = event.target;
  var reader = new FileReader();
  reader.onload = function(){
      var dataURL = reader.result;
      var output = document.getElementById('imgToUpload');
      output.src = dataURL;
  };
  reader.readAsDataURL(input.files[0]);
}


function attachPhotographToComposition(){
  var newImageToAdd = new Image();
  newImageToAdd.src = $("#imgToUpload")[0].src;
  var size = photographCompostion.length;
  photographCompostion[size] = $("#imgToUpload")[0].src;
 
}

function updateVote(btn, votes, usuarioID, piezaID, type, updown){ //Recibe parámetros para crear relacion en Neo4J
    console.log("Updating votes");
    var envio = usuarioID+"/"+piezaID+"/"+type+"/"+updown; //Se envia con los valores separados por '/'
    $.ajax({
        url: "/methods/updateVote",
        type: 'POST',
        data: envio,
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log(data);
            alert("Your vote has been saved");
            btn.innerHTML = (parseInt(votes) + 1);
        },
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}