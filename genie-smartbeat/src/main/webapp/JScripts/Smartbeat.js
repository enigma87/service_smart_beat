﻿//Method to get the User list
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

//Method to handle individual User selection
function HighlightSelectedUser(listitem) {

    $("#userlist").find("label.navigation").removeClass("selected");
    $(listitem).find("label.navigation").addClass("selected");
}

//Setup Accordion for the selected User
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
            + '<h3>Perception Indices</h3>'
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

//Summary element of the Accordion 
function AddSummary(userid) {
    QVRecoveryTime(userid);
    QVShapeIndex(userid);
    QVHeartrateZones(userid);
}

//QVHeartrateZones element of the Accordion
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

//QVHeartrateTestHistory  element of the Accordion
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

//QVTrainingSessionHistory element of the Accordion
function QVTrainingSessionHistory(userid) {
    // use global access token
    //var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS");

    var startTimestamp = '2013-10-01 00:00:00.000';
    var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS");

    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp, function (response) {

	 var arrHealthPerceptionIndex = [];
         var arrSessionStressPerceptionIndex = [];
         var arrMuscleStatePerceptionIndex = [];

	var csvdata = arrayToCSVString(beanArrayToCSVArray(response.obj.trainingSessionBeans));

	/*
		qvtrainingsessionhistory: attach download to the hyperlink
	*/	
	$('#qvtrainingsessionhistory').append('<a id="exporttrainingsesisonslink" href="#"><img class="download" src="Images/download_icon.png" title="download as csv" /></a>');

	$('#exporttrainingsesisonslink').bind("click" ,function() {
		exportDataToCSV(this, csvdata,  userid + '_trainingsesisonhistory_' + moment().format("YYYY-MM-DD HH:mm:ss.SSS") + '.csv');	
	});

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

		if (arrHealthPerceptionIndex[trainingSessionBean.healthPerceptionIndex] == null) {
			arrHealthPerceptionIndex[trainingSessionBean.healthPerceptionIndex]= [];
		}
		arrHealthPerceptionIndex[trainingSessionBean.healthPerceptionIndex].push(trainingSessionBean.vdot);
		if (arrSessionStressPerceptionIndex[trainingSessionBean.sessionStressPerceptionIndex] == null) {
			arrSessionStressPerceptionIndex[trainingSessionBean.sessionStressPerceptionIndex] = [];
		}
		arrSessionStressPerceptionIndex[trainingSessionBean.sessionStressPerceptionIndex].push(trainingSessionBean.vdot);
		if (arrMuscleStatePerceptionIndex[trainingSessionBean.muscleStatePerceptionIndex] == null) {
			arrMuscleStatePerceptionIndex[trainingSessionBean.muscleStatePerceptionIndex] = [];
		}		
		arrMuscleStatePerceptionIndex[trainingSessionBean.muscleStatePerceptionIndex].push(trainingSessionBean.vdot);
	}

	var a = arrHealthPerceptionIndex.length;
	var b = arrSessionStressPerceptionIndex.length;
	var c = arrMuscleStatePerceptionIndex.length;

	var how_many_plot_arrays = a > b ?  (a > c ? a : c) : (b > c ? b : c);

	var healthPerceptionPlotArray = [];
	var sessionStressPerceptionPlotArray = [];
	var muscleStatePerceptionPlotArray = [];



	for (var k = 0; k < how_many_plot_arrays; k++) {
		for (var l=1; l <= 9; l++) {
			
			if (arrHealthPerceptionIndex[l] != null && arrHealthPerceptionIndex[l][k] != null) {
				if (healthPerceptionPlotArray[k] == null) {
					healthPerceptionPlotArray[k] = [];
				}
				healthPerceptionPlotArray[k][l-1] = arrHealthPerceptionIndex[l][k];
			}
			
			if (arrSessionStressPerceptionIndex[l] != null && arrSessionStressPerceptionIndex[l][k] != null) {
				if (sessionStressPerceptionPlotArray[k] == null) {
					sessionStressPerceptionPlotArray[k] = [];
				}				
				sessionStressPerceptionPlotArray[k][l-1] = arrSessionStressPerceptionIndex[l][k];
			}

			if (arrMuscleStatePerceptionIndex[l] != null && arrMuscleStatePerceptionIndex[l][k] != null) {
				
				if (muscleStatePerceptionPlotArray[k] == null) {
					muscleStatePerceptionPlotArray[k] = [];
				}

				muscleStatePerceptionPlotArray[k][l-1] = arrMuscleStatePerceptionIndex[l][k];
			}

			

		}
	}

        ScatterGraph('qvhealthPerceptionIndex', healthPerceptionPlotArray, [1, 2, 3, 4, 5, 6, 7, 8, 9], "Health Perception Index");
        ScatterGraph('qvsessionStressPerceptionIndex', sessionStressPerceptionPlotArray, [1, 2, 3, 4, 5, 6, 7, 8, 9], "Stress Perception Index");
        ScatterGraph('qvmuscleStatePerceptionIndex',  muscleStatePerceptionPlotArray, [1, 2, 3, 4, 5, 6, 7, 8, 9] , "Muscle State Perception Index");

    });

    //$("#dv_Test").html($("#dv_Test").html() + "##########" + HOST_URL + "v1.0/trainee/id/" + userid + "/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp);

}

//QVShapeIndexHistory element of the Accordion
function QVShapeIndexHistory(userid) {
    // use global access token
    //var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")

    var startTimestamp = '2013-10-01 00:00:00.000';
    var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

    $.getJSON(HOST_URL + "v1.0/trainee/id/" + userid + "/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp + "&endTimeStamp=" + endTimestamp, function (response) {


        var plotdata = [];
        for (var i = 0; i < response.obj.shapeIndexes.length; i++) {
            var shapeIndexBean = response.obj.shapeIndexes[i];
            plotdata[i] = [moment(shapeIndexBean.timeOfRecord).format("YYYY-MM-DD HH:MM"), shapeIndexBean.shapeIndex];
        }

        $("#qvshapeindexhistory").append('<div id="shapeindexhistorygraph"></div>');

        DateGraph("shapeindexhistorygraph", [plotdata]);
    });
}

//QVShapeIndex element of the Accordion
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

//QVRecoveryTime element of the Accordion
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
