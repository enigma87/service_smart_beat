
$(document).ready(function () {
    $("#tabmenu").tabs();
    getAllUsers();
});

// Perl Style dump for de-bugging
function dump(arr, level) {
    var dumped_text = "";
    if (!level) level = 0;

    var level_padding = "";
    for (var j = 0; j < level + 1; j++) level_padding += "    ";

    if (typeof (arr) == 'object') {
        for (var item in arr) {
            var value = arr[item];
            if (typeof (value) == 'object') {
                dumped_text += level_padding + "'" + item + "'             ...\n";
                dumped_text += dump(value, level + 1);
            } else {
                dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
            }
        }
    } else {
        dumped_text = "===>" + arr + "<===(" + typeof (arr) + ")";
    }
    return dumped_text;
}

function LogoutSmartbeat() {
    FB.logout(function (response) { location.reload(); });
}

function InList(value, array) {

    if (!isArray(array)) {
        alert('Second argument to InList should be an array');
        return null;
    }

    for (var i = 0; i < array.length; i++) {
        if (value == array[i]) {
            return 1;
        }
    }

    return 0;
}

function isArray(a) {
    return isObject(a) && a.constructor == Array;
}

function isObject(a) {
    return (typeof a == 'object' && !!a) || isFunction(a);
}

function isFunction(a) {
    return typeof a == 'function';
}

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