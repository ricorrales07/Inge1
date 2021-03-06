window.fbAsyncInit = function() {
	FB.init({
		appId		: '212006762635772',
		xfbml		: true,
		status 		: true,
		version		: 'v2.9'
	});
	FB.AppEvents.logPageView();
	FB.getLoginStatus(function(response) {
	  console.log("login/out (facebook)");
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
		Cookies.set("userType", "facebook");
		Cookies.set("userID", response.authResponse.userID);
		Cookies.set("accessToken", response.authResponse.accessToken);

		$("#pieceEditorLink").show();
		$("#profileLink").show();
		$("#LogButtonText").html("Log Out");
		$("#logIcon").removeClass("glyphicon-log-in").addClass("glyphicon-log-out");
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
    $("#pieceEditorLink").hide();
    $("#profileLink").hide();
    $("#LogButtonText").html("Log In");
    $("#logIcon").removeClass("glyphicon-log-out").addClass("glyphicon-log-in");
    console.log(response.status);
    document.location.href = '/';
}


function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
  var id_token = googleUser.getAuthResponse().id_token; // The ID token you need to pass to your backend:
  console.log("ID Token: " + id_token);
  onSignInGmail();

  		$.ajax({
  			url: "/methods/sendTokenGmail",
  			type: 'POST',
  			data: JSON.stringify({
  			    accessToken: id_token
  			}),
  			contentType: "text/plain",
  			success:function(data, textStatus, jqXHR){
  				console.log("token sent")},
  			error:function(jqXHR, textStatus, errorThrown ){
  				console.log(errorThrown);
  			}
  		});
  		Cookies.set("userType", "gmail");
  		Cookies.set("accessToken", id_token);
  		Cookies.set("userID", profile.getId());
  		$("#pieceEditorLink").show();
  		$("#profileLink").show();
  		$("#LogButtonText").html("Log Out");
  		$("#logIcon").removeClass("glyphicon-log-in").addClass("glyphicon-log-out");
  	}

/*
  var xhr = new XMLHttpRequest();
  //xhr.open('POST', 'https://yourbackend.example.com/tokensignin');
  xhr.open('POST', '/methods/sendToken');
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  //xhr.setRequestHeader('Content-Type', 'multipart/form-data');
  xhr.onload = function() {
    console.log('Signed in as: ' + xhr.responseText);
  };
  xhr.send('idtoken=' + id_token);
*/

function signOut() {
    onSignOutGmail();
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
    $("#pieceEditorLink").hide();
    $("#LogButtonText").html("Log in");
    $("#logIcon").removeClass("glyphicon-log-out").addClass("glyphicon-log-in");
    //document.location.href = '/';
    $("#profileLink").hide();
    $("#loginModal").modal('toggle');
}

$('#profileLink').on('click', function(){
    window.location.href = "./profile?"
        + "userType=" + Cookies.get("userType")
        + "&access_token=" + Cookies.get("accessToken")
        + "&userId=" + Cookies.get("userID");
});

function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    console.log(response.status);
    if (response.status === 'connected') {
      document.getElementById("pieceEditorLink").style.visibility = "visible";
      try
      {
          document.getElementById("saveCompositionButton").disabled = false;
      }
      catch (e)
      {
          console.log("saveCompositionButton not found");
      }
      document.getElementById("LogInOut").children[0].style.display = "none";
      document.getElementById("LogInOut").children[1].style.display = "none";
    } else {
      console.log("not connected to facebook");
      document.getElementById("pieceEditorLink").style.visibility = "hidden";
      try
      {
          document.getElementById("saveCompositionButton").disabled = false;
      }
      catch (e)
      {
          console.log("saveCompositionButton not found");
      }
      document.getElementById("LogInOut").children[0].style.display = "initial";
      document.getElementById("LogInOut").children[1].style.display = "initial";
    }
}

function onSignInGmail()
{
    console.log("onSignInGmail");
    document.getElementById("pieceEditorLink").style.visibility = "visible";
    try
    {
        document.getElementById("saveCompositionButton").disabled = false;
    }
    catch (e)
    {
        console.log("saveCompositionButton not found");
    }
    document.getElementById("LogInOut").children[1].style.display = "inline";
    document.getElementById("LogInOut").children[4].style.display = "none";
}

function onSignOutGmail()
{
    document.getElementById("pieceEditorLink").style.visibility = "hidden";
    try
    {
        document.getElementById("saveCompositionButton").disabled = true;
    }
    catch (e)
    {
        console.log("saveCompositionButton not found");
    }
    document.getElementById("LogInOut").children[1].style.display = "none";
    document.getElementById("LogInOut").children[4].style.display = "inline";
}