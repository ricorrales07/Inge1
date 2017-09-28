<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/results4.css">
    <title>MorphoSpace - Search results</title>
		<meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
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

	<div class="row">
		<div class="col-md-6 col-sm-6 col-lg-6">
			$mainResult$
			<!--img id="mainResult" src="http://placehold.it/512x512" alt="Image" style="max-width:100%;" /-->
		</div>
		<div class="col-md-6 col-sm-6 col-lg-6">
			<div id="texto">
				$texto$
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12 col-sm-12 col-lg-12">
    	    <div class="well">
                <div id="myCarousel" class="carousel slide">

                <ol class="carousel-indicators">
									$bolitas$
                    <!--li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#myCarousel" data-slide-to="1"></li>
                    <li data-target="#myCarousel" data-slide-to="2"></li-->
                </ol>

                <!-- Carousel items -->
                <div class="carousel-inner">

								$extraResults$
                <!--div class="item active">
                	<div class="row-fluid">
                	  <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                	  <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                	  <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                	  <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                	</div><!--/row-fluid-->
                <!--/div><!--/item-->

                </div><!--/carousel-inner-->

                <a class="left carousel-control" href="#myCarousel" data-slide="prev">‹</a>
                <a class="right carousel-control" href="#myCarousel" data-slide="next">›</a>
                </div><!--/myCarousel-->

            </div><!--/well-->
		</div>

		<div class="col-md-12 col-sm-12 col-lg-12">
            <div class="well">
                <div id="photosCarousel" class="carousel slide">

                <ol class="carousel-indicators">
                                    $bolitas2$
                    <!--li data-target="#photosCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#photosCarousel" data-slide-to="1"></li>
                    <li data-target="#photosCarousel" data-slide-to="2"></li-->
                </ol>

                <!-- Carousel items -->
                <div class="carousel-inner">

                                $compositionPhotos$
                <!--div class="item active">
                    <div class="row-fluid">
                      <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                      <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                      <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                      <div class="col-md-3 col-sm-3 col-lg-3"><a href="#x" class="thumbnail"><img src="http://placehold.it/250x250" alt="Image" style="max-width:100%;" /></a></div>
                    </div><!--/row-fluid-->
                <!--/div><!--/item-->

                </div><!--/carousel-inner-->

                <a class="left carousel-control" href="#photosCarousel" data-slide="prev">‹</a>
                <a class="right carousel-control" href="#photosCarousel" data-slide="next">›</a>
                </div><!--/photosCarousel-->

            </div><!--/well-->
        </div>

	</div>
</div>

</body>

<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/js/results4.js"></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/getSampleString.js"></script>

</html>
