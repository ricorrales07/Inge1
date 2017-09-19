<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
    <link rel="stylesheet" href="assets/css/homepage.css">
    <link rel="stylesheet" href="assets/css/profile.css">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
    <meta name="google-signin-client_id" content="709424385084-659mmq5v3rqar8ufa96ns369ljauf25v.apps.googleusercontent.com">
</head>
<body onload="init();">
<div id="fb-root"></div>

<div class="container">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <nav class="navbar navbar-inverse">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="/">MorphoSpace</a>
                    </div>
                    <ul class="nav navbar-nav">
                        <li><a href="/">Home</a></li>
                        <li><a href="#" data-toggle = "modal" data-target = "#loginModal" data-dismiss="modal">Log in</a></li>
                        <li><a href="/createPiece" id="pieceEditorLink">Piece editor</a></li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>

    <div class="row" style="max-height:80vh; overflow:hidden; height: 5000px;">
        <div class="col-lg-2 col-md-2" id="col1"> <!--No entiendo por qué no me deja poner 3-->
            <div class = "row">
                <img class="img-responsive img-circle" src="assets/images/yo.jpg" alt="Profile picture">
            </div>

            <div class = "caption">
                <h3> <font color="white">Ricardo Corrales</font></h3>
            </div>

            <div class="row">
                <div class="panel panel-default" style="word-wrap:break-word">
                    <div class="panel-heading">Personal Information</div>
                    <div class="panel-body">
                        University: UCR<br>
                        <br>
                        Telephone: +50686402009<br>
                        <br>
                        Email: ricardo.corralesbarquero@ucr.ac.cr
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 bhoechie-tab-container" id="col2" style="height:80%;">
            <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2 bhoechie-tab-menu">
                <div class="list-group">
                    <a href="#" class="list-group-item active text-center">
                        <h4 class="glyphicon glyphicon-plane"></h4><br/>Pieces
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="glyphicon glyphicon-road"></h4><br/>Compositions
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="glyphicon glyphicon-menu-up"></h4><br/>Upvotes
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="glyphicon glyphicon-menu-down"></h4><br/>Downvotes
                    </a>
                </div>
            </div>
            <div class="col-lg-10 col-md-10 col-sm-10 col-xs-10 bhoechie-tab" style="height:100%; max-height:100%">
                <!-- flight section -->
                <div class="bhoechie-tab-content active" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div id="images" style="overflow-y:scroll; max-height:100%; height:100%">
                            <div class="item">
                                <img src="http://lorempixel.com/425/299/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/190/292/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/520/329/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/404/176/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/530/290/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/228/200/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/207/316/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/435/162/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/376/175/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/508/338/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/404/389/nature">
                            </div>
                            <div class="item">
                                <img src="http://lorempixel.com/216/304/nature">
                            </div>
                        </div>
                    </center>
                </div>
                <!-- train section -->
                <div class="bhoechie-tab-content">
                    <center>
                        <h1 class="glyphicon glyphicon-road" style="font-size:12em;color:#55518a"></h1>
                        <h2 style="margin-top: 0;color:#55518a">Cooming Soon</h2>
                        <h3 style="margin-top: 0;color:#55518a">Train Reservation</h3>
                    </center>
                </div>

                <!-- hotel search -->
                <div class="bhoechie-tab-content">
                    <center>
                        <h1 class="glyphicon glyphicon-menu-up" style="font-size:12em;color:#55518a"></h1>
                        <h2 style="margin-top: 0;color:#55518a">Cooming Soon</h2>
                        <h3 style="margin-top: 0;color:#55518a">Upvotes</h3>
                    </center>
                </div>
                <div class="bhoechie-tab-content">
                    <center>
                        <h1 class="glyphicon glyphicon-menu-down" style="font-size:12em;color:#55518a"></h1>
                        <h2 style="margin-top: 0;color:#55518a">Cooming Soon</h2>
                        <h3 style="margin-top: 0;color:#55518a">Downvotes</h3>
                    </center>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
<script src="assets/js/profile.js" defer></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/getSampleString.js"></script>
<script src="assets/js/createPieceAttributes.js"></script>

</html>
