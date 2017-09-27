<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>

<div class = "modal fade" id = "loginModal" tabindex = "-1" role = "dialog" aria-labelledby = "myModalLabel" aria-hidden = "true">
    <div class = "modal-dialog">
        <div class = "modal-content">
            <div class = "modal-header">
                <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                    &times;
                </button>
                <h4 class = "modal-title" id = "myModalLabel">
                    Login
                </h4>
            </div>
            <div class = "modal-body justify-content-center">
                <ul id="LogInOut">
                    <li class="g-signin2" data-onsuccess="onSignIn"> </li>
                    <li class="googleSignOut"> <a href="#" onclick="signOut();"><font color="blue"><u>Sign out</u></font></a></li><br><br>
                    <li class="fb-login-button" data-max-rows="1" data-size="large"
                        data-button-type="continue_with" data-show-faces="true"
                        data-auto-logout-link="true" data-use-continue-as="true"
                        scope="email"></li>
                </ul>
            </div>
            <div class = "modal-footer">
                <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                    Close
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</html>