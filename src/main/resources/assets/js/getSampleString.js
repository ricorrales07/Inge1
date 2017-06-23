//WTF CON EL NOMBRE DE ESTE ARCHIVO?!?

/*
In the event of a clickable object with the id "btn" being
clicked, it calls the method "getImages" from "AppResourcesMethods",
and the data obtained with it (string) will be loaded in a
tag with the id "images".
*/
$("#btn").click(function() { //NOMBRES SIGNIFICATIVOS?!?!
    $("modalImage").each(function(){
    	$(this).remove();
	});
	console.log("btn clicked");
    $.ajax({
		url: "/methods/getImages", //getImages hay que desmenuzarlo. ¿Cuáles imágenes?
		type: 'GET',
		success:function(data, textStatus, jqXHR){
			$('#images').append(data)
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
});
