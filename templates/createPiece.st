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
	    <script src="assets/jquery/jquery-3.2.1.js"></script>
        <script src="assets/bootstrap-3.3.7/js/bootstrap.min.js"></script>
	</head>
	<body >
    	<div id="fb-root"></div>

        <!-- Menu to choose how to load an image to the canvas (from server or locally) -->
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
                    <p><a id="btn" href="#openModal" class="button">Open saved images</a></p>
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


        <!-- Menu to load an image on the canvas from the server -->
        <div id="openModal" class="modalDialog">
           <div class="modal-content">
               <a href="#close" title="Close" class="close">X</a>
               <h2>Saved images</h2>
               <p>Please select one of the following images:</p><br>
                   <div id="images"></div>
           </div>
        </div>

        <!--
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
                         </div>
                         <div class = "modal-body">
                            Please select one of the following images:
                         </div>
                         <div class = "modal-footer">
                             <div id="images"></div>
                         </div>
                      </div>
                   </div>
                </div>
                -->

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
                    <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                         Close
                    </button>
                 </div>
              </div><!-- /.modal-content -->
           </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

		<div class="container-fluid">
		<div class="row top-nav">
			<div class="col-md-2">
				<div class="main-nav-button nav-button" id="saveButton" onclick="saveCreation();saveAttributes();">
					<i class="fa fa-floppy-o fa-2x"></i>
					</br>
					<p>Save</p>
				</div>
			</div>
			<div class="col-md-4 ">
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
			<div class="col-md-4">
				<div class="facebook-login-container">
					<div class="fb-login-button" data-max-rows="1" data-size="medium" data-button-type="login_with" data-show-faces="false" data-auto-logout-link="true" data-use-continue-as="false"></div>
				</div>
			</div>
			<div class="col-md-2 ">
				<div class="main-nav-button nav-button" id="cancelButton" onclick="cancelSprite()">
					<i class="fa fa-times  fa-2x"></i>
					</br>
					<p>Cancel</p>
				</div>
			</div>
		</div>

		<div class="row ">
			<div class="col-md-12 toolset">
				<span class="toolset"  id="editor-menu">
					<div class="toolset" id="editor-menu-content" style="display:none">

						<div class="row">
							<div class=" col-md-offset-2 col-md-8" id="pointerRadius">
								Thickness
								<div id="slider"></div>
							</div>
						</div>

						<div class="row">
							<div class=" col-md-offset-2 col-md-8 " >
								<div class="tool selectedTool" id="pencil" >
									<i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
								</div>

								<div class="tool" id="eraser" >
									<i class="fa fa-eraser fa-2x" aria-hidden="true"></i>
								</div>
							</div>
						</div>


						<!--div class="row">
							<div class="tool col-md-offset-2 col-md-8" id="eraser">
								<div>
									<i class="fa fa-eraser fa-2x" aria-hidden="true"></i>
								</div>
							</div>
						</div-->

						<div class="row">
							<button class="col-md-offset-2 col-md-8" id="addImage" data-toggle = "modal" data-target = "#opcionOpciones">
								Add Image
								<i class="fa fa-plus-circle fa-2x" aria-hidden="true"></i>
							</button>
						</div>

					</div>
					<div class="toolset" id="editor-menu-handle">
						<i class="fa fa-bars fa-2x" aria-hidden="true"></i>
					</div>
				</span>

				<!-- FOUR DIFFERENT VIEWS -->
				<div class="col-md-offset-1 col-md-10" id="editor" onload="init();"">
					<canvas id="leCanvas" ></canvas>
				</div>
				<!-- END FOUR DIFFERENT VIEWS -->

				<span class="toolset" id="attributes-menu">
					<div  id="attributes-menu-handle" >
						<i class="fa fa-list-alt fa-2x" aria-hidden="true"></i>
					</div>

					<div id="attributes-menu-content" style="display:none" >
						<div id="attribute-card-editor" class="row">
							<button class="col-md-offset-3 col-md-6 nopadding" id="addProp" onclick="addProperty()">
								Add Property
								<i class="fa fa-plus-circle"></i>
							</button>

						</div>
						<div id="attribute-card-list" class=" row">

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

	</body>
	<script src="assets/jquery/jquery-3.2.1.js"></script>
	<script src="assets/jquery/jquery-ui.js"></script>
	<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
	<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
	<script src="assets/js/createPiece.js"></script>
	<script src="assets/js/createPieceAttributes.js"></script>
	<script src="assets/js/getSampleString.js"></script>
	<script src="assets/js/js.cookie.js"></script>
	<script src="assets/js/login.js"></script>

</html>