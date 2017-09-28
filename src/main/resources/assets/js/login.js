window.fbAsyncInit = function() {
	FB.init({
		appId		: '212006762635772',
		xfbml		: true,
		status 		: true,
		version		: 'v2.9'
	});
	FB.AppEvents.logPageView();
	FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
     });

	FB.Event.subscribe('auth.statusChange', auth_status_change_callback);
	FB.Event.subscribe('auth.logout', logout_event);
};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {return;}
		js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/en/sdk.js#xfbml=1&version=v2.9&appId=212006762635772";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));

var auth_status_change_callback = function(response) {
    FB.getLoginStatus(function(response) {
          statusChangeCallback(response);
    });
	if(response.status == "connected") {
		console.log("auth_status_change_callback: " + response.status);
		$.ajax({
			url: "/methods/sendToken",
			type: 'POST',
			data: JSON.stringify({
				userID: response.authResponse.userID,
				accessToken: response.authResponse.accessToken
			}),
			contentType: "text/plain",
			success:function(data, textStatus, jqXHR){
				console.log("token sent")
			},
			error:function(jqXHR, textStatus, errorThrown ){
				console.log(errorThrown);
			}
		});
		Cookies.set("userID", response.authResponse.userID);
		Cookies.set("accessToken", response.authResponse.accessToken);

		$("#profileLink").attr('href', "/profile?access_token="
		+ response.authResponse.accessToken + "&userId="
		+ response.authResponse.userID);
	}
	else {
		console.log("disconnected")
	}

}

var logout_event = function(response)
{
    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
    console.log("logout_event");
    console.log(response.status);
    document.location.href = '/';
}

