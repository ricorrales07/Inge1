/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/

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

function registeredImages(owned){
    var filter;
	if(owned){
    	filter = "{_id: /^" + Cookies.get("userID") + "C/}";
	}else{
		filter = "{}";
	}
	$("modalImages").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getImagesDataInDB",
		type: 'POST',
		data: JSON.stringify({collection: "piece", filter: filter}),
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
			if(owned) {
                $('#ownedImages').append(data)
            }else{
                $('#images').append(data)
			}
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
}

function showCompositions(){
    var filter = "{_id: /^" + Cookies.get("userID") + "C/}";
    $("modalImages").each(function(){
        $(this).remove();
    });
    console.log("btn clicked");
    $.ajax({
        url: "/methods/getImagesDataInDB",
        type: 'POST',
        data: JSON.stringify({collection: "composition", filter: filter}),
        success:function(data, textStatus, jqXHR){
			$('#ownedCompositions').append(data)
        },
        error:function(jqXHR, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}