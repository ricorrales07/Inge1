var stage = new createjs.Stage("areaDeDibujo");
var diffX, diffY;
var view = "front";
var lineasDeGuia = new createjs.Bitmap("assets/images/cuadricula.png");
var composicionActual = {partIds: [], partsList: [], matrices: [[],[]]};
var selected = null;
var grapher = new createjs.Shape();
var lastTouchPos = [[-1,-1],[-1,-1]]

<<<<<<< HEAD

function selectPart(part)
=======
function selectPart(index)
>>>>>>> c855cc88eea0fa80e141e248bdd98e2ac9c86af7
{
  grapher.graphics.clear();

  selected = index;
  var part = composicionActual.partsList[index];

  var b = part.getTransformedBounds();

  grapher.graphics.beginStroke("black").drawRect(b.x, b.y, b.width, b.height);

  var link = document.getElementById("pieceEditorLink");
  link.setAttribute("href", "/editPiece?pieceId=" + composicionActual.partIds[selected]);
}

function unselectPart()
{
  selected = null;
  grapher.graphics.clear();
  var link = document.getElementById("pieceEditorLink");
  link.setAttribute("href", "/editPiece");
}

function drag(evt)
{
    evt.target.x = evt.stageX - diffX;
    evt.target.y = evt.stageY - diffY;
    console.log("dragging; x: " + evt.target.x + ", y: " + evt.target.y);
    stage.update();
}

function calculateDifference(evt)
{
    var index;
    for (index = 0; composicionActual.partsList[index] != evt.target; index++);
    selectPart(index);
    diffX = evt.stageX - evt.target.x;
    diffY = evt.stageY - evt.target.y;
}

function manageKey(evt)
{
    var key = evt.which || evt.keyCode || evt.charCode; //alguna de todas va a servir
    console.log(evt.which, evt.keyCode, evt.CharCode, key);
    if (key == 105) //letra i
    {
        addPart();
    }
}

function addPart() //proxy
{
  console.log("creating sprite");
  var img1 = new Image();
  img1.src = "assets/images/odo-head2.png";
  //img1.crossOrigin="Anonymous"; //EaselJS sugiere algo así para saltarse la seguridad de Chrome, no lo entiendo muy bien aún

  var img2 = new Image();
  img2.src = "assets/images/odo-zyg-head2.png";

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

  partSprite.x = Math.floor(Math.random() * document.getElementById("areaDeDibujo").width);
  partSprite.y = Math.floor(Math.random() * document.getElementById("areaDeDibujo").height);

  partSprite.name = "" + createjs.UID.get();

  console.log("Sprite " + partSprite.name + " created. visible: " + partSprite.visible);

  partSprite.set({scaleX: 2, scaleY: 2});

  addListeners(partSprite);

  stage.addChild(partSprite);

  composicionActual.partIds.push(partSprite.name);
  composicionActual.partsList.push(partSprite);
  composicionActual.matrices[0].push(partSprite.getMatrix());
  composicionActual.matrices[1].push(partSprite.getMatrix());

  selectPart(composicionActual.partsList.length-1);
}

function addListeners(item)
{
  item.on("mousedown", handleMouseDown);
  item.on("pressmove", handlePressMove);
  item.on("pressup", handlePressUp);
}

function handlePressUp(evt)
{
  lastTouchPos = [[-1,-1],[-1,-1]];
}

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

function adjustCoordinateX(coord)
{
  var e = document.getElementById("areaDeDibujo");
  var b = e.getBoundingClientRect();
  return (coord - b.left) * 1024 / (b.right - b.left);
}

function adjustCoordinateY(coord)
{
  var e = document.getElementById("areaDeDibujo");
  var b = e.getBoundingClientRect();
  return (coord - b.top) * 512 / (b.bottom - b.top);
}

//NADIE TOQUE ESTO, FUNCIONA PERO NO SÉ POR QUÉ
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

  //las divisiones anteriores solo dan cero cuando los dos dedos están en el mismo punto.

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

function handleTick(evt)
{
    stage.update(evt);
}

function changeView()
{
    var s = selected;
    unselectPart();
    var btn = document.getElementById("changeViewButton");
    btn.textContent = (btn.textContent == "Vista frontal") ? "Vista lateral" : "Vista frontal";

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

    window.onkeypress = manageKey;
}



function guidelines()
{
    lineasDeGuia.visible = !lineasDeGuia.visible;
}

function callEditPiecePage(){
    var url = "/editPiece";

    if (selected != null)
    {
        console.log("adding pieceId for call to editPiece");
        url += "?pieceId=" + partIds[selected];
    }

    var link = document.getElementById("pieceEditorLink");
    link.setAttribute("href", url);

    return false;
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
        data: JSON.stringify({ image: comp.toDataURL(), type: "Composition" }),
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
        pieces += "\"x\": \"" + composicionActual.partsList[i].x + "\", ";
        pieces += "\"y\": \"" + composicionActual.partsList[i].y + "\"\n}";
        if(i < composicionActual.partsList.length - 1){
            pieces += ",\n"
        }
    }
    pieces += "\n}"
    $.ajax({
        url: "/methods/saveCompositionData",
        type: 'POST',
        data: pieces,
        contentType: "text/plain",
        success:function(data, textStatus, jqXHR){
            console.log("image saved in server directory")},
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}