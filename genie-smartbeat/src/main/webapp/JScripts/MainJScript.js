//On page load
$(document).ready(function () {
    //Adjust Dimensions
    adjustPageDimensions();

     //$("#dv_dialog").dialog({
     //       autoOpen: false,
     //       height: 140,
     //       modal: true,
     //       buttons: {
     //           Ok: function () {
     //               $(this).dialog("close");
     //           }
     //       }
     //   });

    $("#btn_GetUserIDDetails").click(function () {
        getUserID();
    });
    $("#a_GetUserID").click(function (event) {
        event.preventDefault();
        getUserID();
        return false;
    });
});

//window.onresize = function () { alert("Resize"); };
window.onresize = adjustPageDimensions;

//function to dynamically adjust the page size
function adjustPageDimensions() {
    // alert("resize");
    //Get the window dimension
    var width = $(window).width();

    width = window.innerWidth || document.documentElement.clientWidth || document.body.offsetWidth;

    //height:window.innerHeight || document.documentElement.clientHeight || document.body.offsetHeight;
    var w = window, d = document, e = d.documentElement, g = d.getElementsByTagName('body')[0], x = w.innerWidth || e.clientWidth || g.clientWidth, y = w.innerHeight || e.clientHeight || g.clientHeight;
    width = x;

    if (width > 908) {
        $("html").css("overflow-x", "hidden");
        $("body").css("overflow-x", "hidden");

        //Setting page container dimension
        $("#dv_PageContainer").width(width - 12);

        //Setting main div dimension
        $("#dv_Main").width(width - 12);
    }
    else {
        $("html").css("overflow", "show");
        $("body").css("overflow", "show");

        //Setting page container dimension
        $("#dv_PageContainer").width(900);

        //Setting main div dimension
        $("#dv_Main").width(900);
    }
}
function subscribe() {
    $("#dv_dialog").html("You have successfully signed up to our monthly news letters.");
    $("#dv_dialog").dialog({ title: "Success!" });
    $("#dv_dialog").dialog("open");
}
function openPopUp(url) {
    window.open(url, '_blank', "");
}

function getUserID() {
    $("#img_Loading").show();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: $("#txt_URL").val(),
        success: function (responce) {
            var returnedData = responce.obj;

            $("#dv_UserID").html(returnedData.userid);
            $("#dv_FirstName").html(returnedData.firstName);
            $("#dv_LastName").html(returnedData.lastName);
            $("#dv_DOB").html(returnedData.dob);

            $("#img_Loading").hide();
        }
    });
}
function getShapeIndex() {
    $("#img_Loading").show();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: $("#txt_URL").val(),
        success: function (responce) {
            var returnedData = responce.obj;

            $("#dv_UserID").html(returnedData.userid);
            $("#dv_FirstName").html(returnedData.firstName);
            $("#dv_LastName").html(returnedData.lastName);
            $("#dv_DOB").html(returnedData.dob);

            $("#img_Loading").hide();
        }
    });
}