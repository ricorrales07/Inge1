window.fbAsyncInit = function() {
	FB.init({
		appId		: '212006762635772',
		xfbml		: true,
		status 		: true,
		version		: 'v2.9'
	});
	FB.AppEvents.logPageView();

	FB.Event.subscribe('auth.authResponseChange', auth_response_change_callback);
	FB.Event.subscribe('auth.statusChange', auth_status_change_callback);
};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {return;}
		js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/es_LA/sdk.js#xfbml=1&version=v2.9&appId=212006762635772";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));

var auth_response_change_callback = function(response) {
	console.log("auth_response_change_callback");
	console.log(response);
}

var auth_status_change_callback = function(response) {
	console.log("auth_status_change_callback: " + response.status);
}