var stage = new createjs.Stage("areaDeDibujo");
var diffX, diffY;
var view = "front";
var lineasDeGuia = new createjs.Bitmap("./cuadricula.png");

function drag(evt)
{
    evt.target.x = evt.stageX - diffX;
    evt.target.y = evt.stageY - diffY;
    stage.update();
}

function calculateDifference(evt)
{
    diffX = evt.stageX - evt.target.x;
    diffY = evt.stageY - evt.target.y;
}

function manageKey(evt)
{
    var key = evt.which || evt.keyCode || evt.charCode; //alguna de todas va a servir
    console.log(evt.which, evt.keyCode, evt.CharCode, key);
    if (key == 105) //letra i
    {
        addImg();
    }
}

function addImg() //proxy
{
  console.log("creating sprite");
  var img1 = new Image();
  img1.src = "./odo-head2.png";
  //img1.crossOrigin="Anonymous"; //EaselJS sugiere algo así para saltarse la seguridad de Chrome, no lo entiendo muy bien aún

  var img2 = new Image();
  img2.src = "./odo-zyg-head2.png";

  var imgs = {
      images: [img1, img2],
      frames: [
          [0,0,img1.width,img1.height,0],
          [0,0,img2.width,img2.height,1]
      ],
      animations: {front: 0, side: 1}
  };
  var partSheet = new createjs.SpriteSheet(imgs);
  var partSprite = new createjs.Sprite(partSheet, view);

  partSprite.x = Math.floor(Math.random() * (document.getElementById("areaDeDibujo").width - Math.max(img1.width, img2.width)));
  partSprite.y = Math.floor(Math.random() * (document.getElementById("areaDeDibujo").height - Math.max(img1.height, img2.height)));

  partSprite.name = "" + createjs.UID.get();

  console.log("Sprite " + partSprite.name + " created. visible: " + partSprite.visible);

  partSprite.on("mousedown", calculateDifference);
  partSprite.on("pressmove", drag);

  stage.addChild(partSprite);
}

function handleTick(evt)
{
    stage.update(evt);
}

function changeEveryView()
{
    var btn = document.getElementById("changeViewButton");
    btn.textContent = (btn.textContent == "Vista frontal") ? "Vista lateral" : "Vista frontal";

    view = (view == "front") ? "side" : "front";
    stage.children.forEach(changeView);
}

function changeView(element)
{
    console.log("element: " + element.toString() + "object type :" + Object.prototype.toString.call(element));
    if (element.toString().substring(1,7) == "Sprite")
    {
            element.gotoAndStop(view);
    }
}

function init()
{
    console.log("init");
    createjs.Touch.enable(stage, false, true);

    lineasDeGuia.visible = false;
    stage.addChild(lineasDeGuia).set({x:0,y:0});

    stage.update();

    createjs.Ticker.timingMode = createjs.Ticker.RAF;
    createjs.Ticker.addEventListener("tick", handleTick);

    window.onkeypress = manageKey;
}

function guidelines()
{
    lineasDeGuia.visible = !lineasDeGuia.visible;
}
