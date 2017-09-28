/**
 * Created by J.A Rodríguez on 27/09/2017.
 */

function addAttributes(JSONData, from){
    document.getElementById("scientificNameVal").value = JSONData["Scientific Name"];
    if(from == "piece"){
        document.querySelector('[class = "typeAttr"]').selectedIndex = JSONData["Type"];
    }
    document.getElementById("publicAttr").checked = JSON.parse(JSONData["Public"]);

    var JSONOptional = JSONData["optional"];
    var index = 1;
    for(i in JSONOptional){
        console.log($(".cardTemplate").length);
        var newCard = $(".cardTemplate").clone().attr("display","inline-block").removeClass("cardTemplate");
        $("#attribute-card-list").append(newCard);
        document.getElementsByName("opProperty")[index].value = i;
        document.getElementsByName("opValue")[index].value = JSONOptional[i];
        index++;
    }
}