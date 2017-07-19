<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <title>Piece</title>
	    <link rel="stylesheet" type="text/css" href="assets/bootstrap-3.3.7/css/bootstrap.css">
	    <link rel="stylesheet" type="text/css" href="assets/bootstrap-3.3.7/css/bootstrap.min.css">
	    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
	    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
	    <link rel="stylesheet" type="text/css" href="assets/css/jquery-ui.css">
	</head>
	<body >
	    <script type="text/javascript" src="assets/jquery/jquery-3.2.1.min.js"></script>
	    <script type="text/javascript" src="assets/jquery/jquery-migrate-1.2.1.min.js"></script>
    	<div id="fb-root"></div>

        <!-- Menu to choose how to load an image to the canvas (from server publicly, from server privately or locally) -->
    	<div class = "modal fade" id = "opcionOpciones" tabindex = "-1" role = "dialog"
           aria-labelledby = "myModalLabel" aria-hidden = "true">
           <div class = "modal-dialog">
              <div class = "modal-content">
                 <div class = "modal-header">
                    <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                          &times;
                    </button>
                    <h4 class = "modal-title" id = "myModalLabel">
                       Adding an image
                    </h4>
                 </div>
                 <div class = "modal-body">
                    Please select how you want to add an image:
                 </div>
                 <div class = "modal-footer">
                     <button id="btnSavedImages" type = "button" class = "btn btn-primary" data-toggle = "modal" onclick="waitImgs(); btnSavedImages();" data-target = "#openModal" data-dismiss="modal">
                    <!-- <button id="btnSavedImages" type = "button" class = "btn btn-primary" data-toggle = "modal" data-target = "#openModal" data-dismiss="modal"> -->
                         Open saved public images
                    </button>
                    <button id="btnSavedOwnedImages" type = "button" class = "btn btn-primary" data-toggle = "modal" onclick="waitImgs(); registeredImages(true);" data-target = "#openOwned" data-dismiss="modal">
                    <!-- <button id="btnSavedImages" type = "button" class = "btn btn-primary" data-toggle = "modal" data-target = "#openModal" data-dismiss="modal"> -->
                         Open my images only
                    </button>
                    <button type = "button" class = "btn btn-primary" data-toggle = "modal" data-target = "#openLocal" data-dismiss="modal">
                       Search images locally
                    </button>
                    <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                       Close
                    </button>
                 </div>
              </div><!-- /.modal-content -->
           </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

       <!-- Menu to load an image on the canvas from the server (public images) -->
       <div class = "modal fade" id = "openModal" tabindex = "-1" role = "dialog"
        aria-labelledby = "myModalLabel" aria-hidden = "true">
            <div class = "modal-dialog">
                  <div class = "modal-content">
                        <div class = "modal-header">
                           <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                                 &times;
                           </button>
                           <h4 class = "modal-title" id = "myModalLabel">
                              Saved images
                           </h4>
                           Please select one of the following images:
                        </div>
                        <div class = "modal-body" align="center">
                           <div id="images"></div>

                           <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                           <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>

                        </div><br>
                        <div class = "modal-footer">
                        <!--
                        <div class="prev">
                             <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        </div>
                        <div class="next">
                             <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        </div>
                        -->
                            <button class="btn btn-default pull-left pull-down" data-toggle = "modal" data-target = "#opcionOpciones" data-dismiss = "modal">
                                Return
                             </button>
                           <button class = "btn btn-default pull-right pull-down" data-dismiss = "modal">
                               Close
                           </button>
                        </div>
                  </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
       </div><!-- /.modal -->


<!-- Menu to load an image on the canvas from the server (owned images from each user)-->
       <div class = "modal fade" id = "openOwned" tabindex = "-1" role = "dialog"
        aria-labelledby = "myModalLabel" aria-hidden = "true">
            <div class = "modal-dialog">
                  <div class = "modal-content">
                        <div class = "modal-header">
                           <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                                 &times;
                           </button>
                           <h4 class = "modal-title" id = "myModalLabel">
                              My images
                           </h4>
                           Please select one of the following images:
                        </div>
                        <div class = "modal-body" align="center">
                           <div id="ownedImages"></div>

                           <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                           <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>

                        </div><br>
                        <div class = "modal-footer">
                            <button class="btn btn-default pull-left pull-down" data-toggle = "modal" data-target = "#opcionOpciones" data-dismiss = "modal">
                                Return
                             </button>
                           <button class = "btn btn-default pull-right pull-down" data-dismiss = "modal">
                               Close
                           </button>
                        </div>
                  </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
       </div><!-- /.modal -->


        <!-- Menu to load an image on the canvas locally -->
        <div class = "modal fade" id = "openLocal" tabindex = "-1" role = "dialog"
           aria-labelledby = "myModalLabel" aria-hidden = "true">
           <div class = "modal-dialog">
              <div class = "modal-content">
                 <div class = "modal-header">
                    <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                          &times;
                    </button>
                    <h4 class = "modal-title" id = "myModalLabel">
                       Local Search
                    </h4>
                 </div>
                 <div class = "modal-body">
                    Click the button below to search for an image
                 </div>
                 <div class = "modal-footer">
                    <input type="file" accept='image/*' onchange='openFile(event)'><br>
                    <button class="btn btn-default pull-left" data-toggle = "modal" data-target = "#opcionOpciones" data-dismiss = "modal">
                         Return
                    </button>
                    <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                         Close
                    </button>
                 </div>
              </div><!-- /.modal-content -->
           </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

		<div class="container-fluid">
		<div class="row top-nav">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-4">
						<div class="facebook-login-container">
							<div class="fb-login-button" data-max-rows="1" data-size="medium" data-button-type="login_with" data-show-faces="false" data-auto-logout-link="true" data-use-continue-as="false"></div>
						</div>
					</div>
					<div class="col-md-offset-6  col-md-2">
						<div title="Cancel" class=" nav-button" id="cancelButton" onclick="cancelSprite()">
							<i class="fa fa-times  fa-2x"></i>
							</br>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2">
						<div title="Save" class="nav-tool-button nav-button" id="saveButton" onclick="savePiece();">
							<i class="fa fa-floppy-o fa-2x"></i>
						</div>

						<div title="Pencil Tool" class="nav-tool-button tool selectedTool nav-button" id="pencil" >
							<i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
						</div>

						<div title="Eraser Tool" class="nav-tool-button tool nav-button" id="eraser" >
							<i class="fa fa-eraser fa-2x" aria-hidden="true"></i>
						</div>

						<div title="Add Image" class="nav-tool-button tool nav-button" id="addImage" data-toggle = "modal" data-target = "#opcionOpciones">
							<i class="fa fa-picture-o fa-2x" aria-hidden="true"></i>
						</div>

					</div>
					<div class="col-md-4">
						<div class="row tool-options  " id="pointerRadius">
							<p class="col-md-4" >Thickness</p>
							<div class=" col-md-8 rotation" title="Thickness">
								<div id="slider"></div>
							</div>
						</div>
						<div class="row tool-options tool-options-hidden" id="rotationRadius">
							<div class="col-md-8" >
								<div class="row">
									<p class="col-md-4" >Rotation</p>
									<div class=" col-md-8 rotation" title="Rotation">
										<div id="rotation-slider"></div>
									</div>
								</div>
							</div>

							<div class=" col-md-2">
								<div class="nav-tool-button nav-button" id="acceptImage" onclick="stickImageToSurface()">
									<i class="fa fa-check fa-2x" aria-hidden="true"></i>
								</div>
							</div>
							<div class=" col-md-2">
								<div class="nav-tool-button nav-button" id="cancelImage" onclick="cancelImageToSurface()">
									<i class="fa fa-times fa-2x" aria-hidden="true"></i>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div id="changeView">
								<label>View: </label>
								<select class="viewInfo" onchange="updateView(this)">
								  <option value="canvasFront" selected>Front</option> <!--Spacing out in a nook and cranny-->
								  <option value="canvasSide" >Side</option> <!--It's oficially uncanny-->
								  <!--option value="canvasRightSide" >Right Side</option> <!--It's a fish in the sea of granite-->
								  <!--option value="canvasBack">Back</option>
								  <option value="canvasLeftSide">Left Side</option-->

								</select>
						</div>
					</div>

					<div class="col-md-2">
						<div id="properties-nav-btn">
							Properties
						</div>
					</div>
				</div>
			</div>

		</div>

		<div class="row ">
			<div class="col-md-12 toolset">


				<!-- FOUR DIFFERENT VIEWS  ui-widget-content-->
				<div id="editor-container" class=" col-md-10">
					<div class="" id="editor" onload="init();"">
						<canvas id="canvasBackGround"></canvas>
						<canvas id="leCanvas" ></canvas>

					</div>
				</div>
				<!-- END FOUR DIFFERENT VIEWS -->

				<span class="toolset" id="attributes-menu">

					<div id="attributes-menu-content"  style="display:none" >
						<div id="attribute-card-editor" class="row">
							<button class="col-md-offset-3 col-md-6 nopadding" id="addProp" onclick="addProperty()">
								Add Property
								<i class="fa fa-plus-circle"></i>
							</button>

						</div>
						<div id="attribute-card-list" class=" row">
							<!-- Non optional  Cards -->
							<div class="attribute-card col-md-offset-1 col-md-10">
								<div class="row">
									<div class="col-md-offset-2 col-md-8">
										<label >Author: </label>
										<input style="display:none" value="Author"/>
									</div>
								</div>

								<div class="row">
									<div class=" col-md-3 ">
										<label >Value: </label>
									</div>
									<div class="card-input right-icon col-md-8 nopadding">
										<i class="fa fa-pencil"></i>
										<input type="search" class="att-input form-control attribute-value" placeholder="Property value"/>
									</div>
								</div>
							</div>
							<div class="attribute-card col-md-offset-1 col-md-10">
								<div class="row">
									<div class="col-md-offset-2 col-md-8">
										<label >Scientific Name: </label>
										<input style="display:none" value="Scientific Name"/>
									</div>
								</div>

								<div class="row">
									<div class=" col-md-3 ">
										<label >Value: </label>
									</div>
									<div class="card-input right-icon col-md-8 nopadding">
										<i class="fa fa-pencil"></i>
										<input type="search" class="att-input form-control attribute-value" placeholder="Property value"/>
									</div>
								</div>
							</div>

							<div class="attribute-card col-md-offset-1 col-md-10">
								<div class="row">
									<div class="col-md-offset-2 col-md-8">
										<label >Public: </label>
									</div>
								</div>

								<div class="row">
									<div class=" col-md-8 ">
										<input style="display:none" value="Public"/>
										<label >Value: </label>
										<input type="checkbox" class="att-input attribute-value" unchecked/>
									</div>
								</div>
							</div>
							<!-- END Non optional  Cards -->

							<!-- Template  Card -->
							<div class="attribute-card col-md-offset-1 col-md-10 cardTemplate">
								<div class="row attribute-card-header">
									<div class="col-md-1 " style="float: right" onclick="deleteCard(this)">
										<i class="fa fa-times"></i>
									</div>
								</div>
								<div class="row">
									<div class=" col-md-3">
										<label >Name: </label>
									</div>
									<div class="card-input right-icon col-md-8 nopadding">
										<i class="fa fa-pencil"></i>
										<input type="search" class="att-input form-control attribute-name"
											placeholder="Property name"/>
									</div>
								</div>

								<div class="row">
									<div class=" col-md-3 ">
										<label >Value: </label>
									</div>
									<div class="card-input right-icon col-md-8 nopadding">
										<i class="fa fa-pencil"></i>
										<input type="search" class="att-input form-control attribute-value" placeholder="Property value"/>
									</div>
								</div>
							</div>
							<!-- End of Template  Card -->
						</div>
					</div>
				</span>
			</div>
		</div>
	</div>

    <script type="text/javascript" src="assets/slick/slick.min.js"></script>

    <script>
        var slideIndex = 1;

        function waitImgs() {
          setTimeout("plusDivs(0)", 200);
        }

        function plusDivs(n) {
          showDivs(slideIndex += n);
        }

        function showDivs(n) {
          var i;
          var x = document.getElementsByClassName("img-thumbnail");
          if (n > x.length) {slideIndex = 1}
          if (n < 1) {slideIndex = x.length}
          for (i = 0; i < x.length; i++) {
             x[i].style.display = "none";
          }
          x[slideIndex-1].style.display = "inline";
          x[slideIndex].style.display = "inline";
          x[slideIndex+1].style.display = "inline";
          x[slideIndex+2].style.display = "inline";
          x[slideIndex+3].style.display = "inline";
          x[slideIndex+4].style.display = "inline";
        }

        function growImg(x) {
            x.style.height = "64px";
            x.style.width = "64px";
        }

        function normImg(x) {
            x.style.height = "32px";
            x.style.width = "32px";
        }

    </script>

        <script type="text/javascript">

        function statusChangeCallback(response) {
                console.log('statusChangeCallback');
                console.log(response);
        }

        function savePiece() {
            var saved = false;
            saved = saveAttributes();

            if(saved){
                saveCreation();
            }
        }
        </script>

	</body>

	<script src="assets/jquery/jquery-ui.js"></script>
	<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
	<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
	<script src="assets/js/createPiece.js"></script>
	<script src="assets/js/createPieceAttributes.js"></script>
	<script src="assets/js/getSampleString.js"></script>
	<script src="assets/js/js.cookie.js"></script>
	<script src="assets/js/login.js"></script>

</html>
