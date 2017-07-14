/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/


//$("#btnSavedImages").click(function() {
function btnSavedImages(){
    $("a").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getImages",
		type: 'GET',
		success:function(data, textStatus, jqXHR){
			$('#images').append(data)
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
}

//$("#btnSavedPrivateImages").click(function() {
function btnSavedPrivateImages(usrId){
    $("a").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getPrivateImages",
		type: 'GET',
		data: usrId,
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
			$('#privateImages').append(data)
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
}

/*
$(document).ready(function(){
    $('.funSlick').slick({
        dots: false,
        infinite: true,
        speed: 700,
        autoplay: false,
        arrows: true,
        slidesToShow: 1,
        slidesToScroll: 1
        variableWidth: true
        adaptiveHeight: true
     });
 });
*/


