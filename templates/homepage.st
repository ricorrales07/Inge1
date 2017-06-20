<!--Probado en:
      Firefox (no funciona el touch)
      Chrome
-->

<!DOCTYPE html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/homepage.css">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
</head>
<body onload="init();">
    <div id="fb-root"></div>

    <!-- Menu to load an image on the canvas from the server -->
    <div class = "modal fade" id = "openModal" tabindex = "-1" role = "dialog" aria-labelledby = "myModalLabel" aria-hidden = "true">
        <div class = "modal-dialog">
            <div class = "modal-content">
                <div class = "modal-header">
                    <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                        &times;
                    </button>
                    <h4 class = "modal-title" id = "myModalLabel">
                        Saved images
                    </h4>
                </div>
                <div class = "modal-body">
                    Please select one of the following images:
                    <div id="images"></div>
                </div>
                <div class = "modal-footer">
                    <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                        Close
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
   </div><!-- /.modal -->

    <div class = "container">

        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">MorphoSpace</a>
                </div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li class = "dropdown">
                        <a href = "#" class = "dropdown-toggle" data-toggle = "dropdown">
                           Login
                           <b class = "caret"></b>
                        </a>

                        <ul class = "dropdown-menu">
                          <li class="fb-login-button" data-max-rows="1" data-size="medium"
                             data-button-type="login_with" data-show-faces="false"
                               data-auto-logout-link="true" data-use-continue-as="false"></li>
                        </ul>
                     </li>
                    <li><a href="#">Piece catalog</a></li>
                    <li><a href="#">Search similar compositions</a></li>
                    <li><a href="/createPiece" id="pieceEditorLink">Piece editor</a></li>
                </ul>
                <button id="changeViewButton" class="btn btn-basic navbar-btn navbar-right" onclick="changeView();">Side view</button>
                <button id="guidelinesButton" class="btn btn-basic navbar-btn navbar-right" onclick="guidelines();">Guidelines</button>
                <button id="loadCompositionButton" class="btn btn-basic navbar-btn navbar-right" onclick="loadComposition();">Load saved composition (test)</button>
                <button id="saveCompositionButton" class="btn btn-basic navbar-btn navbar-right" onclick="saveCompositionImage();saveCompositionData();">Save composition (test)</button>
                <button id="btn" class="btn btn-basic navbar-btn navbar-right" data-toggle = "modal" data-target = "#openModal" data-dismiss="modal">Add piece</button>
            </div>
        </nav>

        <div class = "row">

            <div class = "col-md-12">

                <canvas id="areaDeDibujo" width="1024" height="512">
                    Lo sentimos, su navegador no soporta nuestra aplicación.
                </canvas>
                <!--*presionar 'i' para añadir imágenes (proxy)-->
            </div>

            <!--div class = "col-md-1">

                <ul class = "nav nav-pills nav-stacked">
                   <li class = "active"><a href = "#">Home</a></li>
                   <li><a href = "#">SVN</a></li>
                   <li><a href = "#">iOS</a></li>
                   <li><a href = "#">VB.Net</a></li>
                   <li><a href = "#">Java</a></li>
                   <li><a href = "#">PHP</a></li>
                </ul>
            </div-->
        </div>
    </div>

    <!--button onclick="window.location.reload()">reset</button-->

</body>

<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
<script src="assets/js/homepage.js" defer></script>
<script src="assets/js/getSampleString.js" defer></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>

</html>
