$(document).ready(function ()
{
    /*********************************** Team Building Games Script ******************************/
    $(".info").hide();

    $("body").on("click", ".viewDetails", function()
    {
        $(this).parent().parent().find(".info").fadeIn("slow");
        $(this).text("Hide Details");
        $(this).removeClass("viewDetails").addClass("hideDetails");
    });

    $("body").on("click", ".hideDetails", function()
    {
        $(this).parent().parent().find(".info").fadeOut("slow");
        $(this).text("View Details");
        $(this).removeClass("hideDetails").addClass("viewDetails");
    });

    /*********************************** Unity Built Experence Script ******************************/

    $("form").on("submit", function(e)
    {
        e.preventDefault();
    });

    $("#postComment").on("click", function()
    {
        var companyName = $("#companyName").val();
        var comment = $("#comment").val();

        $.ajax({
            type: "POST",
            url: "send-comment.php",
            data: {companyName:companyName, comment:comment},
            success: function(data)
            {
                alert(data);

            }
        });
    });


    /*********************************** Gallery Script ******************************/

    $(".fullScreen").hide();

    $(".galleryImages img.img-thumbnail").click(function ()
    {
        var image = $(this).attr("src");
        $(".fullScreen img.img-thumbnail").attr("src", image);
        window.location.href = "#";
        $(".galleryImages").fadeOut("slow");

        $("#pageHeading").fadeOut("slow");
        $(".fullScreen").fadeIn("slow");
    });

    $("body").on("click", "#closeImageToggle", function ()
    {
        $(".fullScreen").fadeOut("slow");
        $("#pageHeading").fadeIn("slow");
        $(".galleryImages").fadeIn("slow");

    });

    /*********************************** Gallery Script ******************************/

    $("#sendMessageBtn").on("click", function()
    {
        console.log("hi there");
        var name = $("#name").val();
        var email = $("#email").val()
        var number = $("#number").val()
        var message = $("#message").val()

        $.ajax({
            type: "POST",
            url: "send-message.php",
            data: {name:name, email:email, number:number, message:message},
            success: function(data)
            {
                alert(data);

            }
        });
    });
    
});


    



