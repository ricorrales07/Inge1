var stage = new createjs.Stage("areaDeDibujo");
var diffX, diffY;
var view = "front";
var lineasDeGuia = new createjs.Bitmap("assets/images/cuadricula.png");
var partsList = [];
var selected = null;
var grapher = new createjs.Shape();

function selectPart(part)
{
  grapher.graphics.clear();

  selected = part;
  var b = part.getBounds();

  grapher.graphics.beginStroke("black").drawRect(b.x + part.x, b.y + part.y, b.width, b.height);
}

function unselectPart()
{
  selected = null;
  grapher.graphics.clear();
}

function drag(evt)
{
    unselectPart();
    evt.target.x = evt.stageX - diffX;
    evt.target.y = evt.stageY - diffY;
    stage.update();
}

function calculateDifference(evt)
{
    selectPart(evt.target);
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

  partSprite.on("mousedown", calculateDifference);
  partSprite.on("pressmove", drag);

  stage.addChild(partSprite);

  partsList.push(partSprite);

  selectPart(partSprite);
}

function handleTick(evt)
{
    stage.update(evt);
}

function changeView()
{
    var btn = document.getElementById("changeViewButton");
    btn.textContent = (btn.textContent == "Vista frontal") ? "Vista lateral" : "Vista frontal";

    view = (view == "front") ? "side" : "front";
    partsList.forEach(function(element){element.gotoAndStop(view)});
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

function getLoginThing()
{
    var ajaxRequest = new XMLHttpRequest();

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
