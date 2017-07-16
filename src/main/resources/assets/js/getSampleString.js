/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/


//$("#btnSavedImages").click(function() {
function btnSavedImages(){
    $("modalImages").each(function(){
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
function savedOwnedImages(){
    $("modalImages").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getOwnedImages",
		type: 'POST',
		data: Cookies.get("userID"),
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
			$('#ownedImages').append(data)
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
}
