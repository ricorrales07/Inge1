var stage = new createjs.Stage("areaDeDibujo");
var diffX, diffY;
var changeViewButton = new createjs.Shape();

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
    if (evt.keyCode == 13)
    {
        console.log("creating sprite");
        var img1 = new Image();
        img1.src = "./odo-head1.png";

        var img2 = new Image();
        img2.src = "./odo-zyg-head1.png";

        var imgs = {
            images: [img1, img2],
            frames: [
                [0,0,img1.width,img1.height,0],
                [0,0,img2.width,img2.height,1]
            ],
            animations: {front: 0, side: 1}
        };
        var partSheet = new createjs.SpriteSheet(imgs);
        var partSprite = new createjs.Sprite(partSheet, "front");
        partSprite.x = 100;
        partSprite.y = 100;
        
        partSprite.name = "" + createjs.UID.get();
        
        console.log("Sprite " + partSprite.name + " created. visible: " + partSprite.visible);
        
        partSprite.on("mousedown", calculateDifference);
        partSprite.on("pressmove", drag);
        
        stage.addChild(partSprite);
    }
}

function handleTick(evt)
{
    stage.update(evt);
}

function changeView(element, index, array)
{
    console.log("element: " + element.toString() + "object type :" + Object.prototype.toString.call(element));
    if (element.toString().substring(1,7) == "Sprite")
    {
        if (element.currentAnimation == "front")
            element.gotoAndStop("side");
        else
            element.gotoAndStop("front");
    }
}

function init()
{
    console.log("init");
    createjs.Touch.enable(stage, false, true);
    changeViewButton.graphics.beginFill("green").drawCircle(20,20,10);
    changeViewButton.set({name: "changeViewButton"});
    stage.addChild(changeViewButton);
    
    changeViewButton.on("click", function()
    {
        stage.children.forEach(changeView);
    });
    
    stage.update();
    
    createjs.Ticker.timingMode = createjs.Ticker.RAF;
    createjs.Ticker.addEventListener("tick", handleTick);
    
    window.onkeypress = manageKey;
}


