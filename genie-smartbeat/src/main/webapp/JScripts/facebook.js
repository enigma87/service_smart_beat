// globals, for a session

var HOST_URL = 'http://ec2-54-229-146-226.eu-west-1.compute.amazonaws.com:8080/smartbeat/';
//var HOST_URL = 'http://localhost:8080/smartbeat/';

var uid = null;
var accessToken = null;
var graphs = []; // for replotting the graphs in accordion 

var Zone1Time = [1.0];
var Zone2Time = [2.0];
var Zone3Time = [2.0];
var Zone4Time = [3.0];
var Zone5Time = [4.0];
var line1 = [['2013-07-06 18:23:10', 4]];

var JQPLOT_COLORS = [
 "#ee8b49",
 "#4bb2c5",
 "#579575",
 "#c5b47f",
 "#958c12",
 "#EAA228",
 "#4b5de4",
 "#d8b83f",
 "#953579",
 "#ff5800",
 "#0085cc",
 "#839557"
];

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
                                $("#main").show(700);
                                $("img#logout").show(700);
                            } else if (response.status === 'not_authorized') {

                                document.write('<p>app denied auth</p>');
                                // the user is logged in to Facebook, 
                                // but has not authenticated your app
                            } else {
                                // the user isn't logged in to Facebook.
                                document.write('<p>user not logged in.</p>');
                            }
                        });

                    });
                } else {
                    alert('User cancelled login or did not fully authorize.');
                }
            });
        }
        else {
            document.write('<p>please log into facebook</p>');
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
    $.getJSON(HOST_URL + "v1.0/trainee/all",
		function (response) {
		    for (var i = 0; i < response.obj.traineeIds.length; i++) {
		        $("#userlist").find("ul").append('<li  onclick="ShowQuickView(this,'
					+ "'" + response.obj.traineeIds[i].userid.toString() + "'"
					+ ')"><div> <span class="ui-icon ui-icon-person"></span> <label class="navigation">'
					+ response.obj.traineeIds[i].lastName
					+ ", "
					+ response.obj.traineeIds[i].firstName
					+ "</label></div></li>"
				);
		    }
		    $("#userlist").find("li").first().click();
		}
	);
}

function HighlightSelectedUser(listitem) {

    $("#userlist").find("label.navigation").removeClass("selected");
    $(listitem).find("label.navigation").addClass("selected");
}

function ShowQuickView(listitem, userid) {

    //when quickview is changed to another user, reset plots
    graphs = [];

    if (userid != null
		&& accessToken != null) {

        HighlightSelectedUser(listitem)

        $("#detail").html(
			'<div id="accordion">'
			+ '<h3>Summary</h3>'
		  	+ '<div id="qvsummary" >'
			+ '<div id="qvsummarykeys">'
			+ '<ul class="qvsummary">'
			+ '<li id="qvtraineeclassification"> </li>'
			+ '<li id="qvtimetorecover"> </li>'
			+ '<li id="qvshapeindex"> </li>'
			+ '</ul></div>'
			+ '<div id="qvheartratedonut"></div>'
			+ '</div>'
			+ '<h3>Training Session History</h3>'
			+ '<div id="qvtrainingsessionhistory" > </div>'
			+ '<h3>Shape Index History</h3>'
			+ '<div id="qvshapeindexhistory" ></div>'
			+ '<h3>Heartrate Test History</h3>'
			+ '<div id="qvheartratetesthistory" ></div>'
            + '<h3>Health Perception Indices, Session Stress Perception Indices and Muscle State Perception Indices</h3>'
            + '<div  style="width:1000px; text-align:center;">'
            + '<div id="qvhealthPerceptionIndex" style="width:500px;"></div>'
            + '<div id="qvsessionStressPerceptionIndex" style="width:500px;"></div>'
            + '<br/><div id="qvmuscleStatePerceptionIndex" style="width:500px;"></div>'
			+ '</div>');

        $("#accordion").accordion({
            animate: 300,
            heightStyle: "content",
            autoHeight: true,
            collapsible: true,
            activate: function (event, ui) {
                for (var i = 0; i < graphs.length; i++) {
                    graphs[i].replot();
                }
            }
        });

        AddSummary(userid);
        QVTrainingSessionHistory(userid);
        QVShapeIndexHistory(userid);
        QVHeartrateTestHistory(userid);
    } else {
        window.setTimeout(function () { ShowQuickView(listitem, userid); }, 333);
    }
}

function AddSummary(userid) {
    QVRecoveryTime(userid);
    QVShapeIndex(userid);
    QVHeartrateZones(userid);
}

function QVHeartrateZones(userid) {
    // use global access token
    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateZones?accessToken=" + accessToken + "&accessTokenType=facebook",
	function (response) {
	    var donutdata = [];
	    for (var i = 1; i <= 6; i++) {
	        var start = response.obj['heartrateZone' + i + 'Start'];
	        var end = response.obj['heartrateZone' + i + 'End'];
	        var label = 'Zone ' + i + ': ' + start + ' - ' + end;
	        var value = end - start;
	        donutdata[i - 1] = [label, value];
	    }

	    DonutGraph("qvheartratedonut", [donutdata], "Heartrate Zones");
	});
}

function QVHeartrateTestHistory(userid) {
    // use global access token
    //var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")    
    var startTimestamp = '2013-10-01 00:00:00.000';
    var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

    //HTML for otherhearrate types
    var restingheartrate;
    var thresholdheartrate;
    var maximalheartrate;

    $("#qvheartratetesthistory").append('<div id="heartratetesthistorykeys"><ul></ul></div>');
    $("#qvheartratetesthistory").append('<div id="heartratetesthistorygraph"></div>');

    //resting
    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateTest/resting/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp)
	 .done(function (restingresponse) {
	     if (restingresponse.obj.heartrateTests.length > 0) {
	         restingheartrate = restingresponse.obj.heartrateTests[0].heartrate;
	     }
	     else {
	         restingheartrate = "none yet";
	     }

	     $("#heartratetesthistorykeys").find("ul").append('<li> Resting Heartrate : ' + restingheartrate + '</li>');

	     //threshold
	     $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateTest/threshold/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp)
         .done(function (thresholdresponse) {

             if (thresholdresponse.obj.heartrateTests.length > 0) {
                 thresholdheartrate = thresholdresponse.obj.heartrateTests[0].heartrate;
             }
             else {
                 thresholdheartrate = "none yet";
             }

             $("#qvheartratetesthistory").find("ul").append('<li>Threshold Heartrate : ' + thresholdheartrate + '</li>');

             //maximal
             $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateTest/maximal/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp)
             .done(function (maximalresponse) {
                 if (maximalresponse.obj.heartrateTests.length > 0) {
                     maximalheartrate = maximalresponse.obj.heartrateTests[0].heartrate;
                 }
                 else {
                     maximalheartrate = "none yet";
                 }

                 $("#qvheartratetesthistory").find("ul").append('<li>Maximal Heartrate : ' + maximalheartrate + '</li>');

                 //orthostatic
                 $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateTest/orthostatic/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp)
                 .done(function (orthostaticresponse) {
                     var plotdata = [];

                     for (var i = 0; i < orthostaticresponse.obj.heartrateTests.length; i++) {
                         var heartrateBean = orthostaticresponse.obj.heartrateTests[i];
                         plotdata[i] = [moment(heartrateBean.timeOfRecord).format("YYYY-MM-DD HH:MMA"), heartrateBean.heartrate];
                     }

                     DateGraph("heartratetesthistorygraph", [plotdata], "Orthostatic Heartrate");
                 });
             });
         });
	 });

    //    $("#dv_Test").html($("#dv_Test").html() + "##########" + HOST_URL + "v1.0/trainee/id/" + userid + "/heartrateTest/orthostatic/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp);
}

function QVTrainingSessionHistory(userid) {
    // use global access token
    //var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS");

    var startTimestamp = '2013-10-01 00:00:00.000';
    var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS");

    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp, function (response) {
        //Arrays used to display Health Perception Index, Session Stress Perception Index and Muscle State Perception Index
        var arrVdot = [];
        var arrhealthPerceptionIndex = [];
        var arrsessionStressPerceptionIndex = [];
        var arrmuscleStatePerceptionIndex = [];

        for (var i = 0; i < response.obj.trainingSessionBeans.length; i++) {

            var trainingSessionBean = response.obj.trainingSessionBeans[i];

            $('#qvtrainingsessionhistory').append(
				'<div class="trainingsessionrow" id="trainingsessionrow-' + trainingSessionBean.trainingSessionId.toString() + '">'
				+ '</div>'
			);

            var trainingsessionselector = 'div#trainingsessionrow-' + trainingSessionBean.trainingSessionId.toString();

            var trainingSessionrow = $('#qvtrainingsessionhistory').find(trainingsessionselector)[0];

            var html = '<div class="trainingsessionkeys"><ul>';
            for (key in trainingSessionBean) {
                if (!InList(key, ['startTime',
						  'endTime',
						  'surfaceIndex',
						  'vdot',
						  'healthPerceptionIndex',
						  'muscleStatePerceptionIndex',
						  'sessionStressPerceptionIndex',
						  'averageAltitude',
						  'extraLoad',
						  'validForTableInsert',
						  'sessionDuration'])) {

                    continue;
                }
                var listitemhtml = '<li class="trainingsessionhistorycolumn"> <label>' + key + ' : </label>'
				+ trainingSessionBean[key] + '</li>';
                html += listitemhtml;
            }
            html += '</ul></div>';

            $(trainingSessionrow).append(html);


            var heartratezones = [1, 2, 3, 4, 5, 6];
            var timedistrodata = [];
            var speeddistrodata = [];

            //WARNING: index doesn't start with 0 because we deliberately mark the zones from 1-6
            for (var j = 1; j <= trainingSessionBean.timeDistributionOfHRZ.length; j++) {
                timedistrodata[j - 1] = trainingSessionBean.timeDistributionOfHRZ[j];
                speeddistrodata[j - 1] = trainingSessionBean.speedDistributionOfHRZ[j];
            }

            $(trainingSessionrow).append(
				'<div class="speeddistrograph"  id="timedistrograph-'
				+ trainingSessionBean.trainingSessionId.toString()
				+ '"></div>'
				+ '<div class="speeddistrograph"  id="speeddistrograph-'
				+ trainingSessionBean.trainingSessionId.toString()
				+ '"></div>'
			);

            BarGraph('timedistrograph-' + trainingSessionBean.trainingSessionId, [timedistrodata], heartratezones, "Time Distribution");
            BarGraph('speeddistrograph-' + trainingSessionBean.trainingSessionId, [speeddistrodata], heartratezones, "Speed Distribution");

            if (response.obj.trainingSessionBeans.length > (i + 1)) {
                $('#qvtrainingsessionhistory').append('</br>');
            }

            //Logic to display Health Perception Index, Session Stress Perception Index and Muscle State Perception Index
            arrVdot[i] = trainingSessionBean.vdot;
            arrhealthPerceptionIndex[i] = trainingSessionBean.healthPerceptionIndex;
            arrsessionStressPerceptionIndex[i] = trainingSessionBean.sessionStressPerceptionIndex;
            arrmuscleStatePerceptionIndex[i] = trainingSessionBean.muscleStatePerceptionIndex;
        }

        ScatterGraph('qvhealthPerceptionIndex', [arrhealthPerceptionIndex], arrVdot, "Health Perception Indices");
        ScatterGraph('qvsessionStressPerceptionIndex', [arrsessionStressPerceptionIndex], arrVdot, "Stress Perception Indices");
        ScatterGraph('qvmuscleStatePerceptionIndex', [arrmuscleStatePerceptionIndex], arrVdot, "Muscle State Perception Indices");

    });

    //$("#dv_Test").html($("#dv_Test").html() + "##########" + HOST_URL + "v1.0/trainee/id/" + userid + "/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp);

}

function QVShapeIndexHistory(userid) {
    // use global access token
    //var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")

    var startTimestamp = '2013-10-01 00:00:00.000';
    var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp, function (response) {


        var plotdata = [];
        for (var i = 0; i < response.obj.shapeIndexes.length; i++) {
            var shapeIndexBean = response.obj.shapeIndexes[i];
            plotdata[i] = [moment(shapeIndexBean.timeOfRecord).format("YYYY-MM-DD HH:MMA"), shapeIndexBean.shapeIndex];
        }

        $("#qvshapeindexhistory").append('<div id="shapeindexhistorygraph"></div>');

        DateGraph("shapeindexhistorygraph", [plotdata]);
    });
}

function QVShapeIndex(userid) {
    // use global access token
    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/shapeIndex?accessToken=" + accessToken + "&accessTokenType=facebook",
		function (response) {
		    $("#qvsummary").find("#qvshapeindex").html(
				"<label> Shape index : </label>"
				 + response.obj.shapeIndex
			);
		});
}

function QVRecoveryTime(userid) {
    // use global access token
    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/recoveryTime?accessToken=" + accessToken + "&accessTokenType=facebook",
		function (response) {
		    $("#qvsummary").find("#qvtraineeclassification").html(
				"<label> Trainee classificaion : </label>"
				+ response.obj.traineeClassification
			);
		    $("#qvsummary").find("#qvtimetorecover").html(
				"<label> Time at full recovery : </label> "
				+ response.obj.recoveryTime
			);
		}
	);

}


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

$(document).ready(function () {
    $("#tabmenu").tabs();
    getAllUsers();
});

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


function DateGraph(divid, plotarrays, graphtitle) {

    if (!(isArray(plotarrays)
		&& plotarrays[0].length > 0
		&& isArray(plotarrays[0])
		&& plotarrays[0][0].length == 2)) {

        return;
    }

    var graph1 = $.jqplot(divid, plotarrays, {
        seriesColors: JQPLOT_COLORS,
        title: {
            text: graphtitle,
            show: true
        },
        grid: {
            borderWidth: 0.0
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.DateAxisRenderer,
                tickOptions: {
                    showGridline: false,
                    formatString: '%b&nbsp%#d',
                    markSize: 0
                }
            },
            yaxis: {
                tickOptions: {
                    formatString: '%.2f &nbsp',
                    markSize: 0
                }
            }
        },
        highlighter: {
            show: true,
            sizeAdjust: 7
        },
        cursor: {
            show: false
        }
    });

    graphs[graphs.length] = graph1;
}

function BarGraph(divid, bararrays, xaxisarray, graphtitle) {

    if (!(isArray(bararrays)
		&& bararrays[0].length > 0
		&& isArray(xaxisarray)
		&& xaxisarray.length > 0)) {

        return;
    }

    var graph1 = $.jqplot(divid, bararrays, {
        animate: !$.jqplot.use_excanvas,
        seriesDefaults: {
            renderer: $.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        seriesColors: JQPLOT_COLORS,
        title: {
            text: graphtitle,
            show: true
        },
        grid: {
            borderWidth: 0.0
        },
        axes: {
            xaxis: {
                ticks: xaxisarray,
                renderer: $.jqplot.CategoryAxisRenderer,
                tickOptions: {
                    showGridline: false,
                    markSize: 0
                }
            },
            yaxis: {
                tickOptions: {
                    markSize: 0
                }
            }
        },
        /*highlighter: {
		        show: true,
		        sizeAdjust: 5
		},*/
        cursor: {
            show: false
        }
    });

    graphs[graphs.length] = graph1;
}

function DonutGraph(divid, donutarrays, title) {
    if (!(isArray(donutarrays)
		&& donutarrays[0].length > 0)) {

        return;
    }

    var graph = $.jqplot(divid, donutarrays, {
        animate: !$.jqplot.use_excanvas,
        title: {
            text: title,
            show: true
        },
        grid: {
            borderWidth: 0.0
        },
        seriesColors: JQPLOT_COLORS,
        seriesDefaults: {
            renderer: $.jqplot.DonutRenderer,
            rendererOptions: {
                showDataLabels: false,
                sliceMargin: 0,
                diameter: 100,
                innerDiameter: 60,
                startAngle: -90,
                dataLabels: 'value'
            }
        },
        legend: { show: true, location: 'e' }
    });
    graphs[graphs.length] = graph;
}

function ScatterGraph(divid, bararrays, xaxisarray, graphtitle) {

    if (!(isArray(bararrays)
		&& bararrays[0].length > 0
		&& isArray(xaxisarray)
		&& xaxisarray.length > 0)) {

        return;
    }

    var graph1 = $.jqplot(divid, bararrays, {
        title: graphtitle,
        // Series options are specified as an array of objects, one object
        // for each series.
        series: [{
            // Use (open) circlular markers.
            showLine: false,
            markerOptions: { style: "circle" }
        }],
        axes: {
            xaxis: {
                ticks: xaxisarray,
                renderer: $.jqplot.CategoryAxisRenderer,
                tickOptions: {
                    markSize: 0
                }
            },
            yaxis: {
                tickOptions: {
                    markSize: 0
                }
            }
        },
        highlighter: {
            show: true,
            sizeAdjust: 7.5
        },
        cursor: {
            show: false
        }
    }
  );

    graphs[graphs.length] = graph1;
}

