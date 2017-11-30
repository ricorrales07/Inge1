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
    <meta name="google-signin-client_id" content="86242289452-aj0nuiv6rgda9600phc9ao4qebppif1p.apps.googleusercontent.com">
</head>
<body>
<div id="fb-root"></div>

$loginModal$

<div class="mainContainer">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <nav class="navbar navbar-inverse">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="/">MorphoSpace</a>
                    </div>
                    <ul class="nav navbar-nav">
                        <li><a href="/">Home</a></li>
                        <li><a href="/createPiece" id="pieceEditorLink" style="display: none;">Piece editor</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li class="active"><a style="display: none;" href="/profile"><span class="glyphicon glyphicon-user"></span> My profile</a></li>
                        <li><a href="#" data-toggle="modal" data-target="#loginModal" data-dismiss="modal"><span id="logIcon" class="glyphicon glyphicon-log-out"></span><span id="LogButtonText">Log In<span></a></li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>

    <div class="row" style="max-height:80vh; overflow:hidden; height: 5000px;">
        <div class="col-lg-2 col-md-2" id="col1"> <!--No entiendo por qué no me deja poner 3-->
            <div class = "row">
                <img class="img-responsive img-circle" src=$picture$ alt="Profile picture">
            </div>

            <div class = "caption">
                <h3> <font color="white">$name$</font></h3>
            </div>

            <div class="row">
                <div class="panel panel-default" style="word-wrap:break-word">
                    <div class="panel-heading">Personal Information <div id="editInfoButton" class="btn btn-xs btn-primary" style="float: right;"><i id="drawing" class="fa fa-pencil" aria-hidden="true"></i></div></div>
                    <div class="panel-body">
                        Institution: <span id="university" class="editableInfo" contenteditable="false" spellcheck="false">$institution$</span><br>
                        <br>
                        Telephone: <span id="phone" class="editableInfo" spellcheck="false">$phone$</span><br>
                        <br>
                        Email: <span id="email" class="editableInfo" spellcheck="false">$email$</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-9 bhoechie-tab-container" id="col2" style="height:90%;">
            <div class="col-lg-2 col-md-2 col-sm-2 col-xs-2 bhoechie-tab-menu">
                <div class="list-group">
                    <a href="#" class="list-group-item active text-center">
                        <h4 class="fa fa-puzzle-piece fa-2x"></h4><br/>Pieces
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="fa fa-image fa-2x"></h4><br/>Compositions
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="fa fa-puzzle-piece fa-2x"></h4>
                        <h4 class="glyphicon glyphicon-menu-up"></h4><br/>Piece upvotes
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="fa fa-puzzle-piece fa-2x"></h4>
                        <h4 class="glyphicon glyphicon-menu-down"></h4><br/>Piece downvotes
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="fa fa-image fa-2x">
                        <h4 class="glyphicon glyphicon-menu-up"></h4><br/>Composition upvotes
                    </a>
                    <a href="#" class="list-group-item text-center">
                        <h4 class="fa fa-image fa-2x">
                        <h4 class="glyphicon glyphicon-menu-down"></h4><br/>Composition downvotes
                    </a>
                </div>
            </div>
            <div class="col-lg-10 col-md-10 col-sm-10 col-xs-10 bhoechie-tab" style="height:100%; max-height:100%">
                <!-- piece section -->
                <div class="bhoechie-tab-content active" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $pieces$
                            <!--div class="item">
                                <img src="http://lorempixel.com/425/299/nature">
                                <div class="glyphicon glyphicon-menu-up" align="right" style="color:#00a0da">7</div>
                                <div class="glyphicon glyphicon-menu-down" align="right" style="color:#00a0da">98</div>
                            </div-->
                        </div>
                    </center>
                </div>
                <!-- composition section -->
                <div class="bhoechie-tab-content" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $compositions$
                            <!--div class="item">
                                <img src="http://lorempixel.com/425/299/nature">
                            </div-->
                        </div>
                    </center>
                </div>

                <div class="bhoechie-tab-content voteContent" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $upvotePieceImages$
                        </div>
                    </center>
                </div>

                <div class="bhoechie-tab-content voteContent" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $downvotePieceImages$
                        </div>
                    </center>
                </div>

                <div class="bhoechie-tab-content voteContent" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $upvoteCompositionImages$
                        </div>
                    </center>
                </div>

                <div class="bhoechie-tab-content voteContent" style="height:100%; max-height:100%">
                    <center style="height:100%; max-height:100%">
                        <div class="images" style="">
                            $downvoteCompositionImages$
                        </div>
                    </center>
                </div>

                <!--<h1 style="font-size:10em;"></h1>-->
                <!--<h3>Pieces downvoted</h3>-->

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

</html>
