<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/createPiece.css">
    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
    <link rel="stylesheet" href="assets/css/homepage.css">
    <link rel="stylesheet" href="assets/mdl/material.min.css">
    <link rel="stylesheet" href="assets/css/searchBase.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
    <meta name="google-signin-client_id" content="86242289452-aj0nuiv6rgda9600phc9ao4qebppif1p.apps.googleusercontent.com">
    <style>
    .googleSignOut{display:none;}
    </style>
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
                                        <option value="Torso">Torso</option>
                                        <option value="Feet">Feet</option>
                                        <option value="Tail">Tail</option>
                                        <option value="Wing">Wing</option>
                                        <option value="Beak">Beak</option>
                                      </select>
                                  </div>
                            </div>
                            <div class = "modal-body" align="center">
                               <div id="images"><!--aquí van las imágenes (ver método registeredImages en miscelaneous.js)--></div>
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

            $loginModal$

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
                                      <option value="Leg">Leg</option>
                                      <option value="Torso">Torso</option>
                                      <option value="Tail">Tail</option>
                                      <option value="Wing">Wing</option>
                                      <option value="Beak">Beak</option>
                                    </select>
                                </div>
                           </div>
                           <div class = "modal-body" align="center">
                              <div id="ownedImages">
                                <!-- MDL Spinner Component with Single Color -->
                                  <div class="mdl-spinner mdl-spinner--single-color mdl-js-spinner is-active" id="loadingImagesPieces" ></div>
                              </div>
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

    <div class = "mainContainer" id="compositionMaker">

      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">
          <nav class="navbar navbar-inverse">
              <div class="container-fluid">
                  <div class="navbar-header">
                      <a class="navbar-brand" href="#">MorphoSpace</a>
                  </div>
                  <ul class="nav navbar-nav">
                      <li class="active"><a href="#">Home</a></li>
                      <li><a style="display: none;" href="/createPiece" id="pieceEditorLink">Piece editor</a></li>
                  </ul>
                  <ul class="nav navbar-nav navbar-right">
                      <li><a style="display: none;" id="profileLink" href="#"><span class="glyphicon glyphicon-user"></span> My profile</a></li>
                      <li><a  href="#" data-toggle="modal" data-target="#loginModal" data-dismiss="modal"><span id="logIcon" class="glyphicon glyphicon-log-in"></span> <span id="LogButtonText">Log In<span></a></li>
                  </ul>
                  </div>
          </nav>
        </div>
      </div>

      <div class = "row">
        <div class = "col-lg-2 col-md-2 col-sm-2 h-100" id = "sideBar">
          <div class = "h-100 btn-toolbar" role="toolbar">
            <div class = "col-lg-11 col-md-11 col-sm-11 btn-group-vertical">
              <button id="changeViewButton" class="btn btn-basic btn-responsive" onclick="changeView();">Side view</button>
              <button id="guidelinesButton" class="btn btn-basic btn-responsive" onclick="guidelines();">Grid Lines</button>
              <button id="btnSavedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openModal" data-dismiss="modal">Add piece</button>
              <button id="btnOwnedImages" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openOwned" data-dismiss="modal">Add owned piece</button>
              <button id="mirrorImageButton" class="btn btn-basic btn-responsive" onclick="mirrorImage();">Mirror image</button>
              <button id="deletePieceButton" class="btn btn-basic btn-responsive" onclick="deletePart();">Delete piece</button>
              <button id="changePieceButton" class="btn btn-basic btn-responsive" data-toggle = "modal" data-target = "#openModal" onclick="waitImgs(); registeredImages(false, 'change', 'All');" data-dismiss="modal">Change piece</button>
            </div>
            <div class = "col-lg-11 col-md-11 col-sm-11 btn-group-vertical align-bottom">
              <button id="newCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" onclick="newComposition();">New composition</button>
              <button id="loadCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target= "#openCompositions">Load composition</button>
              <button id="loadPhotosButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target= "#openAssociated" onclick="loadPhotos();">View Photos</button>
              <button id="saveCompositionButton" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" data-toggle = "modal" data-target = "#openLocal" >Save composition</button>
              <button id="searchSimilar" class="btn btn-primary btn-responsive mw-100 bg-inverse text-white" onclick="searchSimilar()">Search Similar</button>
            </div>

            <!--
            <div  id="properties-nav-btn">
                Properties
            </div>
            -->

          </div>
        </div>

        <div class = "col-lg-8 col-md-8 col-sm-8">
            <canvas id="areaDeDibujo" width="1024" height="1024">
            <canvas id="wildcard" />
                Lo sentimos, su navegador no soporta nuestra aplicacion.
            </canvas>
        </div>

        <span class="toolset col-lg-2 col-md-2 col-sm-2" id="attributes-menu">

          <div id="attributes-menu-content"> <!-- style="display:none" -->
            <div id="attribute-card-editor" class="row">
              <button class="col-md-offset-3 col-md-6 nopadding" id="addProp" onclick="addProperty()">
                Add Property
                <i class="fa fa-plus-circle"></i>
              </button>

            </div>
            <div id="attribute-card-list" class=" row">
              <!-- Non optional  Cards -->
              <!--div class="attribute-card col-md-offset-1 col-md-10">
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
              </div-->

              <div class="attribute-card col-md-10">
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
                    <input id="scientificNameVal" type="search" class="att-input form-control attribute-value" placeholder="Property value"/>
                  </div>
                </div>
              </div>

              <div class="attribute-card  col-md-10">
                <div class="row">
                  <div class="col-md-offset-2 col-md-8">
                    <label >Public: </label>
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-8 ">
                    <input type="checkbox" style="display:none" value="Public"/>
                    <label >Value: </label>
                    <input id="publicAttr" type="checkbox" class="att-input attribute-value" unchecked/>
                  </div>
                </div>
              </div>

              <!-- END Non optional  Cards -->

              <!-- Template  Card -->
              <div id = "optional-attribute-card" class="attribute-card col-md-10 cardTemplate">
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
                    <input name="opProperty" type="optional" class="att-input form-control attribute-name"
                      placeholder="Property name"/>
                  </div>
                </div>

                <div class="row">
                  <div class=" col-md-3 ">
                    <label >Value: </label>
                  </div>
                  <div class="card-input right-icon col-md-8 nopadding">
                    <i class="fa fa-pencil"></i>
                    <input name="opValue" type="optional" class="att-input form-control attribute-value" placeholder="Property value"/>
                  </div>
                </div>
              </div>
              <!-- End of Template  Card -->
            </div>
          </div>
          <!-- End of attributes-menu-content -->
        </span>
      </div>
    </div>
    <div class = "container" id="searchSimilarContainer" style="display: none;">
      <div class="row" id="searchControlArea">
          <div class="col-md-12 searchControls">
              <div class="row">
                  <div class="col-md-2">

                      <div title="Go back to making the composition" id="goBack" onclick="changeContainers()" >
                          <i class="fa fa-chevron-circle-left fa-2x" aria-hidden="true"></i>
                      </div>

                  </div>
                  <div class="col-md-2">
                      <div class="row">
                          <div class="searchImage">
                          <img src="https://nusavifauna.files.wordpress.com/2012/08/bird_anatomy_1.jpeg?w=1400">
                          </div>
                      </div>
                      
                  </div>
                  <div class="col-md-2 currentCompositionInfo hideInfo">
                      <label for="currentAuthorField">Author of current composition: </label>
                      <span id="currentAuthorField">Irvin</span>     
                  </div>
                  <div class="col-md-offset-1 col-md-3">
                      <span id="resultInformationBlock" class="hideInfo">
                          <h3> Result information </h3>
                          <div class="row" id="compositionResultInfomration">
                              <div class="col-md-12">
                                  <label for="authorField">Author: </label>
                                  <span id="authorField">Irvin</span>     
                              </div>
                              <div class="col-md-12">
                                  <label for="scieNameField">Scientific Name: </label>
                                  <span id="sciNameField">Pikachu</span>  
                              </div>
                              <div class="col-md-12">
                                  <label for="similarityScoreField">Similarity Score: </label>
                                  <span id="similarityScorField">100%</span>  
                              </div>


                          </div>
                      </span>
                  </div>
              </div>
              
          </div>
      </div>

      <div class="row" id="searchResults">
          <div class="col-md-offset-1 col-md-10 resultsBlock">
                  <div class="row resultsBlockRow">
                      <div class="col-md-3 resultsCardSpace resultsCardSpaceTemplate hideInfo">
                          <div class="resultsCard">
                              <div class="resultsCardTop">
                                  <div class="resultsCardImage">
                                      <img src="http://data.whicdn.com/images/34473435/original.jpg">
                                  </div>
                              </div>
                              <div class="resultsCardInfo">
                                  <div class="hideInfo">
                                      <label for="authorFieldCard">Author: </label>
                                      <span class="authorFieldCard">Ricardo</span>     
                                  </div>
                                  <div class="hideInfo" >
                                      <label for="scieNameFieldCard">Scientific Name: </label>
                                      <span class="sciNameFieldCard">Pichoto</span>  
                                  </div>
                                  <div>
                                      <label for="similarityScoreFieldCard">Similarity Score: </label>
                                      <span class="similarityScorFieldCard">33%</span>  
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
          </div>
      </div>
    </div>

    <!--button onclick="window.location.reload()">reset</button-->

</body>
<script src="assets/mdl/material.min.js"></script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/EaselJS-0.8.2/easeljs-0.8.2.min.js"></script>
<script src="assets/js/loadAttributes.js" ></script>
<script src="assets/js/homepage.js" defer></script>
<script src="assets/js/login.js" ></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/miscelaneous.js"></script>
<script src="assets/js/createPieceAttributes.js"></script>
<script src="assets/js/searchBase.js"></script>

</html>
