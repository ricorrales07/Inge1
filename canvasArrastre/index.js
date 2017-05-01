var size = prompt('enter your brush size');
var md = false;
var canvas = document.getElementById('areaDeDibujo');

var context = canvas.getContext("2d");
context.strokeStyle = "#DF4B26";
context.lineJoin = "round";
context.lineWidth = size;

$('#areaDeDibujo').mouseleave(function(e){
    md= false;
    canvas.style.cursor = "default";
});

$('#areaDeDibujo').click(function(evt){
    var mousePos = getMousePos(canvas, evt);
    var posx = mousePos.x;
    var posy = mousePos.y;
    context.fillStyle = "#DF4B26";
    context.fillRect(posx, posy, size, size);
});

canvas.addEventListener('mousedown', function(evt) {
    canvas.style.cursor = "pointer";
    var mousePos = getMousePos(canvas, evt);
    md = true;
    context.beginPath();
    context.moveTo(mousePos.x, mousePos.y);
});

canvas.addEventListener('mouseup', function() {
    md = false;
    canvas.style.cursor = "default";
});


canvas.addEventListener('mousemove', function(evt) {
    var mousePos = getMousePos(canvas, evt);
    var posx = mousePos.x;
    var posy = mousePos.y;
    draw(canvas, posx, posy);
});

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x:evt.clientX - rect.left - $(this).parent().offset().left,
        y:evt.clientY - rect.top - $(this).parent().offset().top
        //x:evt.clientX - $(this).parent().offset().left,
        //y:evt.clientY - $(this).parent().offset().top
    }
}

function draw(canvas, posx, posy) {
    if (md) {
        //context.fillRect(posx, posy, size, size);
        context.lineTo(posx,posy);
        context.stroke();
    }
}