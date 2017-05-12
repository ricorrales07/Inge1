$(window).on("load",function(){
	var heightPer = $(window).height() - $(".top-nav").height();
	$(".toolset").height(heightPer); //Set up the canvas and it's menu to viewport's heght minus the top nav height. 
});



$( "#editor-menu-handle" ).click(function() {
  $( "#editor-menu-content " ).toggle( "slow", function() {
    // Animation complete.
  });
});

function init() {
    // code here.
}

var stage = new createjs.Stage("leCanvas");



createjs.Ticker.addEventListener("tick", handleTick);
createjs.Ticker.interval = 20; //50 FPS
 function handleTick(event) {
     // Actions carried out each tick (aka frame)
     if (!event.paused) {
         // Actions carried out when the Ticker is not paused.
     }
 }