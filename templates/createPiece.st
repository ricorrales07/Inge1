<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <title>login</title>
	    <link rel="stylesheet" type="text/css" href="assets/bootstrap-3.3.7/css/bootstrap.css">
	    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
	    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
	</head>
	<body>
	<div class="row">
		<div class="col-md-12 top-nav">
			
		</div>
	</div>

	<div class="row ">
		<div class="col-md-12 toolset">
			<div class="toolset" id="editor" onload="init();">
				<canvas id="leCanvas" ></canvas>
			</div>
			<div class="editor-menu toolset">
				<div class="toolset" id="editor-menu-content" style="display:none">
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
			</div>
		</div>
	</div>

	</body>
	<script src="assets/jquery/jquery-3.2.1.js"></script>
	<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
	<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
	<script src="assets/js/createPiece.js"></script>


</html>