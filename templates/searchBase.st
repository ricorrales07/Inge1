<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <link href="assets/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/font-awesome-4.7.0/css/font-awesome.css">
    <link rel="stylesheet" href="assets/css/searchBase.css">
    <title>MorphoSpace</title>
    <meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no">
</head>
<body>

<div class="row" id="searchControlArea">
    <div class="col-md-12 searchControls">
        <div class="row">
            <div class="col-md-1">

                <div title="Go back to making the composition" id="goBack" >
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
            <!--div class="col-md-2 currentCompositionInfo">
                <label for="currentAuthorField">Author of current composition: </label>
                <span id="currentAuthorField">Irvin</span>     
            </div-->
            <div class="col-md-3">
                <span id="resultInformationBlock" class="hideInfo">
                    <h3> Result information </h3>
                    <div class="row" id="compositionResultInfomration">
                        <div class="col-md-12 hideInfo">
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
                <div class="col-md-3 resultsCardSpace">
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

</body>

<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="assets/jquery/jquery-3.2.1.js"></script>
<script src="assets/bootstrap-3.3.7/js/bootstrap.js"></script>
<script src="assets/js/js.cookie.js" ></script>
<script src="assets/js/searchBase.js"></script>

</html>