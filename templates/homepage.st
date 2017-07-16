<!DOCTYPE html>

<!--Probado en:
      Firefox (no funciona el touch)
      Chrome
-->

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

   <div class = "modal fade" id = "resultsModal" tabindex = "-1" role = "dialog" aria-labelledby = "myModalLabel" aria-hidden = "true">
       <div class = "modal-dialog">
           <div class = "modal-content">
               <div class = "modal-header">
                   <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                       &times;
                   </button>
                   <h4 class = "modal-title" id = "myModalLabel">
                       Search results
                   </h4>
               </div>
               <div class = "modal-body">
                   Here are your search results:
                   <div id="resultImages"></div>
               </div>
               <div class = "modal-footer">
                   <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                       Close
                   </button>
               </div>
           </div><!-- /.modal-content -->
       </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

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

    <div class = "container">

      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">

        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">MorphoSpace</a>
                </div>
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li><a href="#" data-toggle = "modal" data-target = "#loginModal" data-dismiss="modal">Log in</a></li>
                    <li><a href="/createPiece" id="pieceEditorLink">Piece editor</a></li>
                </ul>
                </div>
        </nav>
        </div>
        </div>

        <div class = "row">

          <div class = "col-lg-2 col-md-2 col-sm-2 h-100" id = "sideBar">
            <div class = "h-100 btn-toolbar" role="toolbar">
              <div class = "btn-group-vertical">
                <button id="changeViewButton" class="btn btn-basic btn-responsive" onclick="changeView();">Side view</button>
                <button id="guidelinesButton" class="btn btn-basic btn-responsive" onclick="guidelines();">Guidelines</button>
                <button id="btnSavedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openModal" onclick="waitImgs(); btnSavedImages();" data-dismiss="modal">Add piece</button>
                <button id="btnOwnedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openOwned" onclick="waitImgs(); savedOwnedImages();" data-dismiss="modal">Add owned piece</button>
                <button id="mirrorImageButton" class="btn btn-basic btn-responsive" onclick="mirrorImage();">Mirror image</button>
              </div>
              <div class = "btn-group-vertical align-bottom">
                <button id="loadCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" onclick="loadComposition();">Load composition</button>
                <button id="saveCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" onclick="saveComp();">Save composition</button>
                <button id="searchButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" href="/searchResults">Search</button>
              </div>
            </div>
          </div>

            <div class = "col-lg-10 col-md-10 col-sm-10">

                <canvas id="areaDeDibujo" width="1024" height="512">
                    Lo sentimos, su navegador no soporta nuestra aplicación.
                </canvas>
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
<script language="javascript">

    function statusChangeCallback(response) {
        console.log('statusChangeCallback');
        console.log(response);
        if (response.status === 'connected') {
          document.getElementById("pieceEditorLink").style.visibility = "visible";
          document.getElementById("saveCompositionButton").disabled = false;
        } else {
          document.getElementById("pieceEditorLink").style.visibility = "hidden";
          document.getElementById("saveCompositionButton").disabled = true;
        }
      }

</script>

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

</body>

<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
<script src="assets/js/homepage.js" defer></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/getSampleString.js"></script>

</html>
