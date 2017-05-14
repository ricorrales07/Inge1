<!--Solo funciona en Firefox!-->

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
    <div class = "container">

        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">MorphoSpace</a>
                </div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li onclick="getLoginThing();"><a href="#">Login</a></li>
                    <li><a href="#">Catálogo de piezas</a></li>
                    <li><a href="#">Buscar similares</a></li>
                </ul>
                <button id="changeViewButton" class="btn btn-basic navbar-btn navbar-right" onclick="changeEveryView();">Vista lateral</button>
                <button id="guidelinesButton" class="btn btn-basic navbar-btn navbar-right" onclick="guidelines();">Activar líneas de guía</button>
            </div>
        </nav>

        <div class = "row">

            <div class = "col-md-12">

                <canvas id="areaDeDibujo" width="1024" height="512">
                    Lo sentimos, su navegador no soporta nuestra aplicación.
                </canvas>
                *presionar 'i' para añadir imágenes (proxy)
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

</html>