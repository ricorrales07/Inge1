/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/

var imagesToLoad = "new";

function generateTypeIndex(value){
    if (value == "Head"){
        return 0;
    }else if (value == "Thorax"){
        return 1;
    }else if (value == "Leg"){
        return 2;
    }else if (value == "Antenna"){
        return 3;
    }else if (value == "Wing"){
        return 4;
    }
}

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

function registeredImages(owned, type, pieceType){
    if(type != "previous"){
    	imagesToLoad = type;
	}

	if(pieceType == "All" || pieceType.value == "All"){
    	pieceType = "";
	}else{
		pieceType = ", Type: \"" + generateTypeIndex(pieceType.value) + "\"";
	}

	var filter;
	if(owned){
    	filter = "{_id: /^" + Cookies.get("userID") + "C/" + pieceType + "}";
	}else{
		filter = "{" + pieceType.substr(2) + "}";
	}
	$("modalImages").each(function(){
    	$(this).remove();
	});
    $("#loadingImagesPieces").show();
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getImagesDataInDB",
		type: 'POST',
		data: JSON.stringify({collection: "piece", filter: filter, type: imagesToLoad}),
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
			if(owned) {
                $("#loadingImagesPieces").hide();
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