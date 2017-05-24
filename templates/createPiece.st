<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <title>login</title>
	    <link rel="stylesheet" type="text/css" href="assets/bootstrap-3.3.7/css/bootstrap.css">
	    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
	    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
	    <link rel="stylesheet" type="text/css" href="assets/css/jquery-ui.css">
	</head>
	<body >
		<div class="container-fluid">
		<div class="row top-nav">
			<div class="col-md-2">
				<div class="main-nav-button nav-button" id="saveButton">
					<i class="fa fa-floppy-o fa-2x"></i>
					</br>
					<p>Save</p>
				</div>
			</div>
			<div class="col-md-8 ">
				<div id="changeView">
						<label>View: </label>
						<select class="viewInfo" > <!--Supplement an id here instead of using 'name'-->
						  <option value="1" selected>Front</option> 
						  <option value="2" >Right Side</option>
						  <option value="3">Back</option>
						  <option value="3">Left Side</option>
						</select>	
				</div>
			</div>
			<div class="col-md-2 ">
				<div class="main-nav-button nav-button" id="cancelButton">
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
							<div class="tool col-md-offset-2 col-md-8 selectedTool" id="pencil">
								Pencil
								<i class="fa fa-pencil fa-2x" aria-hidden="true"></i>
							</div>
						</div>

						<div class="row">
							<div class="tool col-md-offset-2 col-md-8" id="eraser">
								Eraser
								<i class="fa fa-eraser fa-2x" aria-hidden="true"></i>
							</div>
						</div>

						<div class="row">
							<button class="col-md-offset-2 col-md-8" id="addImage">
								Add Image
								<i class="fa fa-plus-circle fa-2x" aria-hidden="true"></i>
							</button>
						</div>

					</div>
					<div class="toolset" id="editor-menu-handle">
						<i class="fa fa-bars fa-2x" aria-hidden="true"></i>
					</div>
				</span>
				<div class="" id="editor" >
					<canvas id="leCanvas" onload="init();"></canvas>
				</div>
				<span id="attributes-menu">
					<div class="toolset" id="attributes-menu-handle" >
						<i class="fa fa-list-alt fa-2x" aria-hidden="true"></i>
					</div>

					<div class="toolset" id="attributes-menu-content" style="display:none" >
						<div id="attribute-card-editor" class="row">
							<button class="col-md-offset-3 col-md-6 nopadding" id="addProp" onclick="addProperty()">
								Add Property
								<i class="fa fa-plus-circle"></i>
							</button>

						</div>
						<div id="attribute-card-list" class="toolset row">

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

</html>