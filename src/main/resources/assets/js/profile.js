$(document).ready(function() {
    $("div.bhoechie-tab-menu>div.list-group>a").click(function(e) {
        e.preventDefault();
        $(this).siblings('a.active').removeClass("active");
        $(this).addClass("active");
        var index = $(this).index();
        $("div.bhoechie-tab>div.bhoechie-tab-content").removeClass("active");
        $("div.bhoechie-tab>div.bhoechie-tab-content").eq(index).addClass("active");
    });
});

$(function () {

    var $container = $('#container').masonry({
        itemSelector: '.item',
        columnWidth: 200
    });

    // reveal initial images
    $container.masonryImagesReveal($('.images').find('.item'));
});

$.fn.masonryImagesReveal = function ($items) {
    var msnry = this.data('masonry');
    var itemSelector = msnry.options.itemSelector;
    // hide by default
    $items.hide();
    // append to container
    this.append($items);
    $items.imagesLoaded().progress(function (imgLoad, image) {
        // get item
        // image is imagesLoaded class, not <img>, <img> is image.img
        var $item = $(image.img).parents(itemSelector);
        // un-hide item
        $item.show();
        // masonry does its thing
        msnry.appended($item);
    });

    return this;
};

/*function init()
{
    var right=document.getElementById('col2').style.height;
    var left=document.getElementById('col1').style.height;
    if(right>left)
    {
        document.getElementById('col2').style.height=left;
    }
}*/

$("#editInfoButton").bind('click',
    function(){
        if($("#university").attr('contenteditable') == "false")
        {
            $("#editInfoButton").addClass('active');
            $("#drawing").removeClass('fa-pencil');
            $("#drawing").addClass('fa-check');
            $("#university").attr('contenteditable', "true");
            $("#university").css('background-color', "aquamarine");
            $("#phone").attr('contenteditable', "true");
            $("#phone").css('background-color', "aquamarine");
            $("#email").attr('contenteditable', "true");
            $("#email").css('background-color', "aquamarine");
        }
        else
        {
            $("#editInfoButton").removeClass('active');
            $("#drawing").removeClass('fa-check');
            $("#drawing").addClass('fa-pencil');
            $("#university").attr('contenteditable', "false");
            $("#university").css('background-color', "white");
            $("#phone").attr('contenteditable', "false");
            $("#phone").css('background-color', "white");
            $("#email").attr('contenteditable', "false");
            $("#email").css('background-color', "white");

            console.log("updating info with userId: " + Cookies.get("userID"));

            $.ajax({
                url: "/methods/updateUserInfo",
                type: 'POST',
                data: JSON.stringify({
                    id: Cookies.get("userID"),
                    institution: $("#university").text(),
                    phone: $("#phone").text(),
                    email: $("#email").text()
                }),
                contentType: "text/plain",
                success:function(data, textStatus, jqXHR){
                    console.log("info successfully updated")},
                error:function(jqXHR, textStatus, errorThrown ){
                    console.log(errorThrown);
                }
            });
        }
    }
);