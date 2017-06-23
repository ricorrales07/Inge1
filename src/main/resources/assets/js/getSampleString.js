//WTF CON EL NOMBRE DE ESTE ARCHIVO?!?

/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/
$("#btnSavedImages").click(function() { 

    $("a").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getImages", //getImages hay que desmenuzarlo. �Cu�les im�genes?
		type: 'GET',
		success:function(data, textStatus, jqXHR){
			$('#images').append(data)
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});

});

/*$(document).ready(function(){
    $('.funSlick').slick({
        dots: false,
        infinite: true,
        speed: 700,
        autoplay: false,
        arrows: true,
        slidesToShow: 2,
        slidesToScroll: 1
     });
 });*/


/*
$('.funSlick').slick({
      dots: false,
    	prevArrow: $('.prev'),
    	nextArrow: $('.next'),
      infinite: true,
      speed: 300,
      slidesToShow: 4,
      slidesToScroll: 4
      });
      */

