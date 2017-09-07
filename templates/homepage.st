<!DOCTYPE html>

<!--Probado en:
      Firefox (no funciona el touch)
      Chrome
-->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
          <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
    <link rel="stylesheet" href="assets/css/homepage.css">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
    <meta name="google-signin-client_id" content="709424385084-659mmq5v3rqar8ufa96ns369ljauf25v.apps.googleusercontent.com">
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
                               <br/><br/>
                                   Search filter by type:
                                   <div id="searchType">
                                      <select class="typeAttr" id = "typeAttr" onchange = "waitImgs(); registeredImages(false, 'previous', this);">
                                        <option value="All" selected>All</option>
                                        <option value="Head">Head</option>
                                        <option value="Thorax">Thorax</option>
                                        <option value="Leg">Leg</option>
                                        <option value="Antenna">Antenna</option>
                                        <option value="Wing">Wing</option>
                                      </select>
                                  </div>
                            </div>
                            <div class = "modal-body" align="center">
                               <div id="images"></div>
                               <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                               <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>
                            </div><br>
                            <div class = "modal-footer">
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
                              <br/><br/>
                                 Search filter by type:
                                 <div id="searchType">
                                    <select class="typeAttr" id = "typeAttrOwn" onchange = "waitImgs(); registeredImages(true, 'previous', this);">
                                      <option value="All" selected>All</option>
                                      <option value="Head">Head</option>
                                      <option value="Thorax">Thorax</option>
                                      <option value="Leg">Leg</option>
                                      <option value="Antenna">Antenna</option>
                                      <option value="Wing">Wing</option>
                                    </select>
                                </div>
                           </div>
                           <div class = "modal-body" align="center">
                              <div id="ownedImages"></div>
                              <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                              <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>
                           </div><br>
                           <div class = "modal-footer">
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
                <ul>
                 <li class="g-signin2" data-onsuccess="onSignIn"> </li>
                 <li <a href="#" onclick="signOut();">Sign out</a></li>
                 <li class="fb-login-button" data-max-rows="1" data-size="large"
                    data-button-type="continue_with" data-show-faces="true"
                      data-auto-logout-link="true" data-use-continue-as="true"></li>
                </ul>
               </div>
               <div class = "modal-footer">
                   <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                       Close
                   </button>
               </div>
           </div><!-- /.modal-content -->
       </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

   <!-- Menu to load images to associate with the composition to save -->
          <div class = "modal fade" id = "openLocal" tabindex = "-1" role = "dialog"
             aria-labelledby = "myModalLabel" aria-hidden = "true">
             <div class = "modal-dialog">
                <div class = "modal-content">
                   <div class = "modal-header">
                      <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                            &times;
                      </button>
                      <h4 class = "modal-title" id = "myModalLabel">
                         Associate images to the composition
                      </h4>
                   </div>
                   <div class = "modal-body">
                      Click the button below to search for an image
                      <input type="file" accept='image/*'><br>
                        <label >Name: </label>
                        <input type="attr" class="att-in form-control attribute-name"
                        placeholder="Property name"/>
                        <br>

                        <label >Type: </label>
                        <input type="attr" class="att-in form-control attribute-name"
                        placeholder="Property name"/>
                        <br>

                        <button type = "button" class = "btn btn-default" onclick = "tempSave()">
                            Use image
                        </button>
                        <button type = "button" class = "btn btn-default" onclick = "saveComp()">
                            Save composition
                        </button>
                   </div>
                   <div class = "modal-footer">
                       <button type = "button" class = "btn btn-default" data-dismiss = "modal">
                            Close
                       </button>
                   </div>
                </div><!-- /.modal-content -->
             </div><!-- /.modal-dialog -->
          </div><!-- /.modal -->

    <!-- Menu to view the composition's associated images -->
       <div class = "modal fade" id = "openAssociated" tabindex = "-1" role = "dialog"
        aria-labelledby = "myModalLabel" aria-hidden = "true">
            <div class = "modal-dialog">
                <div class = "modal-content">
                        <div class = "modal-header">
                           <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                                 &times;
                           </button>
                           <h4 class = "modal-title" id = "myModalLabel">
                              Associated images
                           </h4>
                           These are the images associated with the composition:
                        </div>
                        <div class = "modal-body" align="center">
                           <div id="associatedImages"></div>
                           <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                           <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>
                        </div><br>
                        <div class = "modal-footer">
                           <button class = "btn btn-default pull-right pull-down" data-dismiss = "modal">
                               Close
                           </button>
                        </div>
                  </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
       </div><!-- /.modal -->

    <!-- Menu to load an owned composition to continue editing it -->
      <div class = "modal fade" id = "openCompositions" tabindex = "-1" role = "dialog"
       aria-labelledby = "myModalLabel" aria-hidden = "true">
           <div class = "modal-dialog">
             <div class = "modal-content">
               <div class = "modal-header">
                  <button type = "button" class = "close" data-dismiss = "modal" aria-hidden = "true">
                        &times;
                  </button>
                  <h4 class = "modal-title" id = "myModalLabel">
                     My compositions
                  </h4>
                  Please select one of the following compositions:
               </div>
               <div class = "modal-body" align="center">
                  <div id="ownedCompositions"></div>
                  <button class="btn pull-left pull-up" onclick="plusDivs(-6)">&#10094;</button>
                  <button class="btn pull-right pull-up" onclick="plusDivs(6)">&#10095;</button>
               </div><br>
               <div class = "modal-footer">
                  <button class = "btn btn-default pull-right pull-down" data-dismiss = "modal">
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
              <button id="guidelinesButton" class="btn btn-basic btn-responsive" onclick="guidelines();">Grid Lines</button>
              <button id="btnSavedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openModal" onclick="document.getElementById('typeAttr').selectedIndex = 0; waitImgs(); registeredImages(false, 'new', 'All');" data-dismiss="modal">Add piece</button>
              <button id="btnOwnedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openOwned" onclick="document.getElementById('typeAttrOwn').selectedIndex = 0; waitImgs(); registeredImages(true, 'new', 'All');" data-dismiss="modal">Add owned piece</button>
              <button id="mirrorImageButton" class="btn btn-basic btn-responsive" onclick="mirrorImage();">Mirror image</button>
              <button id="deletePieceButton" class="btn btn-basic btn-responsive" onclick="deletePart();">Delete piece</button>
              <button id="changePieceButton" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openModal" onclick="waitImgs(); registeredImages(false, 'change', 'All');" data-dismiss="modal">Change piece</button>
            </div>
            <div class = "btn-group-vertical align-bottom">
              <button id="loadCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target= "#openCompositions" onclick="waitImgs(); showCompositions();">Load composition</button>
              <button id="loadPhotosButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target= "#openAssociated" onclick="loadPhotos();">View Photos</button>
              <button id="saveCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target = "#openLocal" >Save composition</button>
              <a id="searchButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" href="/searchResults">Search</a>
            </div>

            <div  id="properties-nav-btn">
                Properties
            </div>

          </div>
        </div>

        <div class = "col-lg-10 col-md-10 col-sm-10">
            <canvas id="areaDeDibujo" width="1024" height="512">
            <canvas id="wildcard" />
                Lo sentimos, su navegador no soporta nuestra aplicación.
            </canvas>
        </div>

        <span class="toolset" id="attributes-menu">

          <div id="attributes-menu-content" style="display:none">
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
                  <div class="col-md-8 ">
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

    function onSignInGmail()
    {
        document.getElementById("pieceEditorLink").style.visibility = "visible";
        document.getElementById("saveCompositionButton").disabled = false;
    }

    function onSignOutGmail()
    {
        document.getElementById("pieceEditorLink").style.visibility = "hidden";
        document.getElementById("saveCompositionButton").disabled = true;
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

<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
<script src="assets/js/homepage.js" defer></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/getSampleString.js"></script>
<script src="assets/js/createPieceAttributes.js"></script>

</html>
