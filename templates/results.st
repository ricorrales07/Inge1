<!DOCTYPE html>

<!--<>-->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="../src/main/resources/assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../src/main/resources/assets/css/results.css">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0,
      maximum-scale = 1.0, user-scalable = no">
</head>
<body>
    <div id="fb-root"></div>

    <div class = "modal fade" id = "loginModal" tabindex = "-1" role = "dialog" aria-labelledby = "myModalLabel" aria-hidden = "true">
        <div class = "modal-dialog">
            <div class = "modal-content">
                <div class = "modal-header">
                    <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                        &times;
                    </button>
                    <h4 class = "modal-title" id = "myModalLabel">
                        Log in
                    </h4>
                </div>
                <div class = "modal-body justify-content-center">
                  <li class="fb-login-button" data-max-rows="1" data-size="large"
                     data-button-type="continue_with" data-show-faces="true"
                       data-auto-logout-link="true" data-use-continue-as="true"></li>
                </div>
                <div class = "modal-footer">
                    <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                        Close
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
   </div><!-- /.modal -->

    <div class="container">
      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">
          <nav class="navbar navbar-inverse">
              <div class="container-fluid">
                  <div class="navbar-header">
                      <a class="navbar-brand" href="#">MorphoSpace</a>
                  </div> <!--end navbar-header-->
                  <ul class="nav navbar-nav">
                      <li class="active"><a href="#">Home</a></li>
                      <li><a href="/createPiece" id="pieceEditorLink">Piece editor</a></li>
                      <li><a href="#" data-toggle = "modal" data-target = "#loginModal" data-dismiss="modal">Log in</a></li>
                  </ul>
              </div> <!--end container-fluid-->
          </nav>
        </div>
      </div>

      <div class="row">
        <div id="mainImage" class="col-lg-4 col-md-4 col-sm-4">

        </div>
        <div id="info" class="col-lg-8 col-md-8 col-sm-8">

        </div>
      </div>

      <div class="row">
        <div id="mainImage" class="col-lg-4 col-md-4 col-sm-4">

        </div>
        <div id="info" class="col-lg-8 col-md-8 col-sm-8">

        </div>
      </div>

      <div class="row">
        <!--div class="photobanner">
        	<img class="first" src="../src/main/resources/assets/images/alaAvispa.PNG" alt="" />
        	<img src="../src/main/resources/assets/images/abdomenLateral.PNG" alt="" />
        </div-->
        <div id="rg-gallery" class="rg-gallery">
        	<div class="rg-thumbs">
        		<!-- Elastislide Carousel Thumbnail Viewer -->
        		<div class="es-carousel-wrapper">
        			<div class="es-nav">
        				<span class="es-nav-prev">Previous</span>
        				<span class="es-nav-next">Next</span>
        			</div>
        			<div class="es-carousel">
        				<ul>
        					<li>
        						<a href="#">
        							<img src="images/thumbs/1.jpg" data-large="images/1.jpg" alt="image01" data-description="Some description" />
        						</a>
        					</li>
        					<li>...</li>
        				</ul>
        			</div>
        		</div>
        		<!-- End Elastislide Carousel Thumbnail Viewer -->
        	</div><!-- rg-thumbs -->
        </div><!-- rg-gallery -->
      </div>

    </div>

</body>

<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="../src/main/resources/assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/results.js" ></script>

</html>
