$("#btn").click(function() {
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
});