$(document).ready(function() {
    $('#myCarousel').carousel({
	    interval: 10000
	})

    $('#photosCarousel').carousel({
        interval: 10000
    })
});

function setMainResult(compositionId, source)
{
  document.getElementById("mainResult").src = source;
  getCompositionData(compositionId);
}

function getCompositionData(compositionId){
    $.ajax({
		url: "/methods/getCompositionData",
		type: 'GET',
        data: {id: compositionId},
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
      console.log("data received: " + data);
			$("#texto").html(data);
		},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
}
