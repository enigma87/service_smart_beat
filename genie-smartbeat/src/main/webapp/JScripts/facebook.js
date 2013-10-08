// globals, for a session

var uid = '';
var accessToken = '';

var Zone1Time = [1.0];
var Zone2Time = [2.0];
var Zone3Time = [2.0];
var Zone4Time = [3.0];
var Zone5Time = [4.0];
var line1 = [['2013-07-06 18:23:10', 4]];

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
                                uid = response3.authResponse.userID;
                                accessToken = response3.authResponse.accessToken;

                                $("#accessToken").val(accessToken);

                                $("#useridlink").val("http://localhost:8080/smartbeat/v1.0/trainee/email/" + email + "?accessToken=" + accessToken + "&accessTokenType=facebook");

                                //$("#shapeindexlink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + uid + "/shapeIndex?accessToken=" + accessToken + "&accessTokenType=facebook");
                                //$("#shapeindexrangelink").val("http://localhost:8080/smartbeat/v1.0/trainee/id/" + uid + "/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=2013-07-03 18:23:11&endTimeStamp=2013-08-05 18:23:10");

                            } else if (response.status === 'not_authorized') {
                                
				document.write('<p>app denied auth</p>');
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


function getAllUsers() {
	$.getJSON("http://localhost:8080/smartbeat/v1.0/trainee/all", 
		function (response) {
			for (var i=0; i < response.obj.traineeIds.length; i++) {
				$("#userlist").append('<div  class="dv_Navigation" onclick="ShowQuickView(' 
					+ "'" +response.obj.traineeIds[i].userid.toString() + "'" 
					+ ')" > <label class="navigation">' 
					+ response.obj.traineeIds[i].lastName 
					+ ", " 
					+ response.obj.traineeIds[i].firstName
					+ "</label></div>"
				);
			}
		} 
	);
}


function ShowQuickView(userid) {
	$("#detail").html(
		'<div id="accordion">'
		+ '<h3>Summary</h3>'
	  	+ '<div> <div id="qvsummary" >'
		+ '<div id="qvtraineeclassification"> </div>'		
		+ '<div id="qvtimetorecover"> </div>'
		+ '<div id="qvshapeindex"> </div>'			
		+ '</div></div>'
		+ '<h3>Training Session History</h3>'
		+ '<div id="qvtrainingsessionhistory"> </div>'
		+ '<h3>Shape Index History</h3>'
		+ '<div id="qvshapeindexhistory"></div>'
		+ '</div>');

	$( "#accordion" ).accordion({
  		heightStyle: "content",
  		autoHeight: false,
		collapsible: true 
 	});
	
	QVRecoveryTime(userid);
	QVShapeIndex(userid);
	QVTrainingSessionHistory(userid);
	QVShapeIndexHistory(userid);
}

function QVTrainingSessionHistory(userid) {
	// use global access token
	var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")
        var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

	$.getJSON("http://localhost:8080/smartbeat/v1.0/trainee/id/"+ userid +"/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp+ "&endTimeStamp=" +endTimestamp, function(response) {
		var html = '';
		for (var i=0; i< response.obj.trainingSessionBeans.length; i++) {
			var trainingSessionBean = response.obj.trainingSessionBeans[i];
			html += '<div class="trainingsessionhistoryrow">';
			
				for (key in trainingSessionBean) {
					if (key == 'userid') {
						continue;					
					}
					html += '<div class="trainingsessionhistorycolumn"> <label> '+ key + '</label></br>'
					+ trainingSessionBean[key] + '</div>';
				}
			
			html += '</div>';		
		}
		$('#qvtrainingsessionhistory').html(html);
	});
}

function QVShapeIndexHistory(userid) {
	// use global access token
	var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")
        var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

	$.getJSON("http://localhost:8080/smartbeat/v1.0/trainee/id/"+ userid +"/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp+ "&endTimeStamp=" +endTimestamp, function(response) {
		var html = '';
		for (var i=0; i< response.obj.shapeIndexes.length; i++) {
			var shapeIndexBean = response.obj.shapeIndexes[i];
			html += '<div class="shapeindexhistoryrow">';			
				for (key in shapeIndexBean) {
					html += '<div class="shapeindexhistorycolumn"> <label> ' + key + ' </label> </br>'
						+ shapeIndexBean[key] + '</div>';
				}
			
			html += '</div>';		
		}
		$('#qvshapeindexhistory').html(html);
	});
}

function QVShapeIndex (userid) {
	// use global access token
	$.getJSON("http://localhost:8080/smartbeat/v1.0/trainee/id/" + userid +"/shapeIndex?accessToken=" +accessToken+"&accessTokenType=facebook",
		function (response) {
			$("#qvsummary").find("#qvshapeindex").html(
				"<label> ShapeIndex </label> </br>"
				 + response.obj.shapeIndex		
			); 
		});
}

function QVRecoveryTime (userid) {
	// use global access token
	$.getJSON("http://localhost:8080/smartbeat/v1.0/trainee/id/"+ userid +"/recoveryTime?accessToken="+ accessToken + "&accessTokenType=facebook",
		function (response) {
			$("#qvsummary").find("#qvtraineeclassification").html(
				"<label> Trainee Classificaion </label></br>"
				+ response.obj.traineeClassification			
			);
			$("#qvsummary").find("#qvtimetorecover").html(
				"<label> Recovery Time </label> </br>"				
				+ response.obj.recoveryTime
			);
		}
	);

}


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
    line1.pop();
    $.getJSON($("#shapeindexrangelink").val(), function (response) {
        for (var i = 0; i < response.obj.shapeIndexes.length - 1; i++) {
            var item = response.obj.shapeIndexes[i];
            var temp = [item.timeOfRecord, item.shapeIndex];
            alert(item.timeOfRecord + " && " + item.shapeIndex);
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
    Zone1Time.pop();
    Zone2Time.pop();
    Zone3Time.pop();
    Zone4Time.pop();
    Zone5Time.pop();

    $.getJSON($("#lastWeekSessionHistorylink").val(), function (response) {
        var html = "<table style='border-spacing:0px; width:1000px;'>";
        html += "<tr style='background-color:#0094ff; color:#FFFFFF;text-align:center;'>";
        html += "<td style='border:1px solid #BBBBBB;'>Start Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>End Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz1 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz2 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz3 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz4 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz5 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz6 Time</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz1 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz2 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz3 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz4 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz5 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>hrz6 Speed</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Surface Index</td>";
        html += "<td style='border:1px solid #BBBBBB;'>vdot</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Health Perception Index</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Muscle State Perception Index</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Session Stress Perception Index</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Average Altitude</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Extra Load</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Validity Status</td>";
        html += "<td style='border:1px solid #BBBBBB;'>As Double Value Average Altitude</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Valid For Table Insert</td>";
        html += "<td style='border:1px solid #BBBBBB;'>Session Duration</td>";
        html += "<td style='border:1px solid #BBBBBB;'>As Double Value Extra Load</td>";
        html += "</tr>";

        //alert(response.obj.trainingSessionBeans.length);
        for (var i = 0; i < response.obj.trainingSessionBeans.length; i++) {
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

            Zone1Time.push(item.hrz1Time);
            Zone2Time.push(item.hrz2Time);
            Zone3Time.push(item.hrz3ime);
            Zone4Time.push(item.hrz4Time);
            Zone5Time.push(item.hrz5Time);
        }
        $("#dv_lastWeekSessionHistory").html(html);
    });
    //alert(hrz1Time.length);

    plot3 = $.jqplot('chart3', [Zone1Time, Zone2Time, Zone3Time, Zone4Time, Zone5Time], {
        // Tell the plot to stack the bars.
        stackSeries: true,
        captureRightClick: true,
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            rendererOptions: {
                // Put a 30 pixel margin between bars.
                barMargin: 30,
                // Highlight bars when mouse button pressed.
                // Disables default highlighting on mouse over.
                highlightMouseDown: true
            },
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer
            },
            yaxis: {
                // Don't pad out the bottom of the data range.  By default,
                // axes scaled as if data extended 10% above and below the
                // actual range to prevent data points right on grid boundaries.
                // Don't want to do that here.
                padMin: 0
            }
        },
        legend: {
            show: true,
            location: 'e',
            placement: 'outside'
        }
    });
    // Bind a listener to the "jqplotDataClick" event.  Here, simply change
    // the text of the info3 element to show what series and ponit were
    // clicked along with the data for that point.
    //$('#chart3').bind('jqplotDataClick',
    //  function (ev, seriesIndex, pointIndex, data) {
    //      $('#info3').html('series: ' + seriesIndex + ', point: ' + pointIndex + ', data: ' + data);
    //  }
    //);
}

// Perl Style dump for de-bugging
function dump(arr,level) {
        var dumped_text = "";
        if(!level) level = 0;

        var level_padding = "";
        for(var j=0;j<level+1;j++) level_padding += "    ";

                if(typeof(arr) == 'object') {
                        for(var item in arr) {
                                var value = arr[item];
                                if(typeof(value) == 'object') {
                                        dumped_text += level_padding + "'" + item + "'             ...\n";
                                        dumped_text += dump(value,level+1);
                                } else {
                                        dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
                                }
                        }
                } else {
                        dumped_text = "===>"+arr+"<===("+typeof(arr)+")";
                }
        return dumped_text;
}

$(document).ready(function () { 
	 $( "#tabmenu" ).tabs();
	 $( "img#icon" ).show( "bounce", { times: 3 }, "slow" );
	getAllUsers();
});
