$("#btn").click(function() {
    console.log("btn clicked");
    $.ajax({
		url: "/methods/createPiece",
		type: 'POST',
		data: JSON.stringify({
			auth: {
				userID: Cookies.get("userID"),
				accessToken: Cookies.get("accessToken")
			},
			piece: {
				owner_id: Cookies.get("userID"),
				name: "Ricardo"

			}
		}),
		contentType: "text/plain",
		success:function(data, textStatus, jqXHR){
			console.log("piece created")},
		error:function(jqXHR, textStatus, errorThrown ){
			console.log(errorThrown);
		}
	});
});