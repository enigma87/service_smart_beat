window.fbAsyncInit = function () {
    FB.init({
        appId: '333643156765163', // App ID
        //channelUrl: 'http://htmlpreview.github.io/?https://github.com/KarunakaranRaju/FacebookLoginHTML/blob/master/HTMLPage.htm', // Channel File
        status: true, // check login status
        cookie: true, // enable cookies to allow the server to access the session
        xfbml: true  // parse XFBML
    });

    FB.Event.subscribe('auth.authResponseChange', function (response) {
        if (response.status === 'connected') {
            FB.login(function (response1) {
                if (response1.authResponse) {
                    FB.api('/me', function (response2) {
                        var email = response2.email;
                        $("#name").val(response2.name);
                        $("#lbl_UserName").val(response2.name);
                        FB.getLoginStatus(function (response3) {
                            if (response3.status === 'connected') {
                                var uid = response3.authResponse.userID;
                                var accessToken = response3.authResponse.accessToken;

                                $("#accessToken").val(accessToken);

                                $("#useridlink").val("http://localhost:8080/smartbeat/v1.0/trainee/email/" + email + "?accessToken=" + accessToken + "&accessTokenType=facebook");

                                //$("#shapeindexlink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + uid + "/shapeIndex?accessToken=" + accessToken + "&accessTokenType=facebook");
                                //$("#shapeindexrangelink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + uid + "/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=2013-07-03 18:23:11&endTimeStamp=2013-08-05 18:23:10");

                            } else if (response.status === 'not_authorized') {
                                // the user is logged in to Facebook, 
                                // but has not authenticated your app
                            } else {
                                // the user isn't logged in to Facebook.
                            }
                        });

                    });
                } else {
                    alert('User cancelled login or did not fully authorize.');
                }
            });
        }
        else {
            FB.login();
        }
    });
};


// Load the SDK asynchronously
(function (d) {
    var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
    if (d.getElementById(id)) { return; }
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "//connect.facebook.net/en_US/all.js";
    ref.parentNode.insertBefore(js, ref);
}(document));

function getShapelinks() {
    //alert($("#useridlink").val());
    $.getJSON($("#useridlink").val(), function (response) {
        var userID = response.obj.userid;
        var accessToken1 = $("#accessToken").val();
        var dateTime1 = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")
        var dateTime2 = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

        $("#shapeindexlink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + userID + "/shapeIndex?accessToken=" + accessToken1 + "&accessTokenType=facebook");
        $("#shapeindexrangelink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + userID + "/shapeIndex/inTimeInterval?accessToken=" + accessToken1 + "&accessTokenType=facebook&startTimeStamp=2013-07-03 18:23:11&endTimeStamp=2013-08-05 18:23:10");
        $("#lastWeekSessionHistorylink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + userID + "/trainingSession/inTimeInterval?startTimeStamp=" + dateTime1 + "&endTimeStamp=" + dateTime2 + "&accessToken=" + accessToken1 + "&accessTokenType=facebook");
    });
}

function getShapeChart() {
    var line1 = [['2013-07-06 18:23:10', 4]];
    line1.pop();
    $.getJSON($("#shapeindexrangelink").val(), function (response) {
        for (var i = 0; i < response.obj.shapeIndexes.length - 1; i++) {
            var item = response.obj.shapeIndexes[i];
            var temp = [item.timeOfRecord, item.shapeIndex];
            alert(item.timeOfRecord +" && "+ item.shapeIndex);
            line1.push(temp)
        }
    });

    //line1.push(['2013-07-06 18:23:10', 4]);
    //line1.push(['2013-07-07 18:23:10', 4]);
    //line1.push(['2013-07-08 18:23:10', 4]);
    //line1.push(['2013-07-13 18:23:10', 4]);
    //line1.push(['2013-07-15 18:23:10', 4]);
    var plot2 = $.jqplot('chart1', [line1], {
        title: 'Customized Date Axis',
        axes: {
            xaxis: {
                renderer: $.jqplot.DateAxisRenderer,
                tickOptions: { formatString: '%Y-%m-%d %H:%M:%S' },
                tickInterval: '1 day'
            }
        },
        series: [{ lineWidth: 4, markerOptions: { style: 'square' } }]
    });
}


function getlastWeekSessionHistory() {
    $.getJSON($("#lastWeekSessionHistorylink").val(), function (response) {
        var html = "<table style='border-spacing:0px;'>";
        html += "<tr style='background-color:#0094ff; color:#FFFFFF;text-align:center;'>";
        html += "<td>startTime</td>";
        html += "<td>endTime</td>";
        html += "<td>hrz1Time</td>";
        html += "<td>hrz2Time</td>";
        html += "<td>hrz3Time</td>";
        html += "<td>hrz4Time</td>";
        html += "<td>hrz5Time</td>";
        html += "<td>hrz6Time</td>";
        html += "<td>hrz1Speed</td>";
        html += "<td>hrz2Speed</td>";
        html += "<td>hrz3Speed</td>";
        html += "<td>hrz4Speed</td>";
        html += "<td>hrz5Speed</td>";
        html += "<td>hrz6Speed</td>";
        html += "<td>surfaceIndex</td>";
        html += "<td>vdot</td>";
        html += "<td>healthPerceptionIndex</td>";
        html += "<td>muscleStatePerceptionIndex</td>";
        html += "<td>sessionStressPerceptionIndex</td>";
        html += "<td>averageAltitude</td>";
        html += "<td>extraLoad</td>";
        html += "<td>validityStatus</td>";
        html += "<td>asDoubleValueAverageAltitude</td>";
        html += "<td>validForTableInsert</td>";
        html += "<td>sessionDuration</td>";
        html += "<td>asDoubleValueExtraLoad</td>";
        html += "</tr>";

        for (var i = 0; i < response.obj.trainingSessionBeans.length - 1; i++) {
            var item = response.obj.trainingSessionBeans[i];
            html += "<tr>";
            html += "<td style='border:1px solid #0094ff;'> " + item.startTime + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.endTime + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz1Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz2Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz3Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz4Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz5Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.hrz6Time + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[0] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[1] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[2] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[3] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[4] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.speedDistributionOfHRZ[5] + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.surfaceIndex + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.vdot + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.healthPerceptionIndex + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.muscleStatePerceptionIndex + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.sessionStressPerceptionIndex + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.averageAltitude + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.extraLoad + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.validityStatus + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.asDoubleValueAverageAltitude + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.validForTableInsert + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.sessionDuration + "</td>";
            html += "<td style='border:1px solid #0094ff;'> " + item.asDoubleValueExtraLoad + "</td>";
            html += "</tr>";
        }
        $("#dv_lastWeekSessionHistory").html(html);
    });
}