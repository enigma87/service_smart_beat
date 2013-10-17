// globals, for a session

//var HOST_URL='http://ec2-54-229-146-226.eu-west-1.compute.amazonaws.com:8080/smartbeat/';
var HOST_URL='http://localhost:8080/smartbeat/';

var uid = null;
var accessToken = null;
var graphs = []; // for replotting the graphs in accordion 

var Zone1Time = [1.0];
var Zone2Time = [2.0];
var Zone3Time = [2.0];
var Zone4Time = [3.0];
var Zone5Time = [4.0];
var line1 = [['2013-07-06 18:23:10', 4]];

var JQPLOT_COLORS = ["#ee8b49", "#4bb2c5", "#c5b47f", "#EAA228", "#579575", "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc"]; 

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
			for (var i=0; i < response.obj.traineeIds.length; i++) {
				$("#userlist").find("ul").append('<li  onclick="ShowQuickView(this,' 
					+ "'" +response.obj.traineeIds[i].userid.toString() + "'" 
					+ ')"><div> <span class="ui-icon ui-icon-person" style="display:inline-block"></span> <label class="navigation">' 
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
	graphs=[];

	if (userid != null
		&& accessToken != null) {

		HighlightSelectedUser(listitem)

		$("#detail").html(
			'<div id="accordion">'
			+ '<h3>Summary</h3>'
		  	+ '<div> <div id="qvsummary" ><ul>'
			+ '<li id="qvtraineeclassification"> </li>'		
			+ '<li id="qvtimetorecover"> </li>'	
			+ '<li id="qvshapeindex"> </li>'			
			+ '</ul></div>'
			+ '</div>'
			+ '<h3>Training Session History</h3>'
			+ '<div id="qvtrainingsessionhistory" > </div>'
			+ '<h3>Shape Index History</h3>'
			+ '<div id="qvshapeindexhistory" ></div>'	
			+ '</div>');

		$( "#accordion" ).accordion({
			animate: 300,
	  		heightStyle: "content",
	  		autoHeight: true,
			collapsible: true,
			activate: function(event, ui) {
				for(var i=0; i < graphs.length; i++) {
					graphs[i].replot();	
				}
			}
	 	});
	
		QVRecoveryTime(userid);
		QVShapeIndex(userid);
		QVTrainingSessionHistory(userid);
		QVShapeIndexHistory(userid);
	} else {
		window.setTimeout(function() { ShowQuickView(listitem, userid); }, 333);	
	}
}

function QVTrainingSessionHistory(userid) {
	// use global access token
	//var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS");
        
	var startTimestamp = '2013-10-01 00:00:00.000';
	var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS");

	$.getJSON(HOST_URL + "v1.0/trainee/id/"+ userid +"/trainingSession/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp+ "&endTimeStamp=" +endTimestamp, function(response) {
		
		for (var i=0; i< response.obj.trainingSessionBeans.length; i++) {

			var trainingSessionBean = response.obj.trainingSessionBeans[i];

			$('#qvtrainingsessionhistory').append(
				'<div style="height:240px;" id="trainingsessionrow-'+ trainingSessionBean.trainingSessionId.toString() + '">'   
				+'</div>'
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
				var listitemhtml = '<li class="trainingsessionhistorycolumn"> <label>'+ key + ' : </label>'
				+ trainingSessionBean[key] + '</li>';
				html += listitemhtml;
			}
			html += '</ul></div>';

			$(trainingSessionrow).append(html);


			var heartratezones = [1, 2, 3, 4, 5, 6];
			var timedistrodata = [];
			var speeddistrodata = [];

			//WARNING: index doesn't start with 0 because we deliberately mark the zones from 1-6
			for (var j=1; j <= trainingSessionBean.timeDistributionOfHRZ.length; j++) {
				timedistrodata[j-1] = trainingSessionBean.timeDistributionOfHRZ[j];
				speeddistrodata[j-1] =  trainingSessionBean.speedDistributionOfHRZ[j]; 
			}
			
			$(trainingSessionrow).append(
				'<div style="padding-left:15px;float:right;width:300px;height:240px;" class="speeddistrograph"  id="timedistrograph-' 
				+ trainingSessionBean.trainingSessionId.toString() 
				+ '"></div>'
				+ '<div style="padding-left:15px;float:right;width:300px;height:240px;" class="speeddistrograph"  id="speeddistrograph-' 
				+ trainingSessionBean.trainingSessionId.toString() 
				+ '"></div>'
			);	
			
			BarGraph('timedistrograph-' + trainingSessionBean.trainingSessionId, [timedistrodata], heartratezones, "Time Distribution");
			BarGraph('speeddistrograph-' + trainingSessionBean.trainingSessionId, [speeddistrodata], heartratezones, "Speed Distribution");

			if (response.obj.trainingSessionBeans.length > (i+1) ) {
				$('#qvtrainingsessionhistory').append('<hr>');			
			}		
		}
		
	});
}

function QVShapeIndexHistory(userid) {
	// use global access token
	//var startTimestamp = moment().subtract('days', 7).format("YYYY-MM-DD HH:mm:ss.SSS")
        
	var startTimestamp = '2013-10-01 00:00:00.000';
	var endTimestamp = moment().format("YYYY-MM-DD HH:mm:ss.SSS")

	$.getJSON(HOST_URL + "v1.0/trainee/id/"+ userid +"/shapeIndex/inTimeInterval?accessToken=" + accessToken + "&accessTokenType=facebook&startTimeStamp=" + startTimestamp+ "&endTimeStamp=" +endTimestamp, function(response) {

		
		var plotdata = [];
		for (var i=0; i< response.obj.shapeIndexes.length; i++) {
			var shapeIndexBean = response.obj.shapeIndexes[i];
			plotdata[i] = [moment(shapeIndexBean.timeOfRecord).format("YYYY-MM-DD HH:MMA"), shapeIndexBean.shapeIndex];
		}
		
		DateGraph("qvshapeindexhistory", [plotdata]);
	});
}

function QVShapeIndex (userid) {
	// use global access token
	$.getJSON(HOST_URL + "v1.0/trainee/id/" + userid +"/shapeIndex?accessToken=" +accessToken+"&accessTokenType=facebook",
		function (response) {
			$("#qvsummary").find("#qvshapeindex").html(
				"<label> Shape index : </label>"
				 + response.obj.shapeIndex		
			); 
		});
}

function QVRecoveryTime (userid) {
	// use global access token
	$.getJSON(HOST_URL + "v1.0/trainee/id/"+ userid +"/recoveryTime?accessToken="+ accessToken + "&accessTokenType=facebook",
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
	getAllUsers();
});

function LogoutSmartbeat() {
	FB.logout(function(response) { location.reload(); });
}

function InList( value, array ){

        if( !isArray(array) ){
                alert( 'Second argument to InList should be an array' );
                return null;
        }
 
        for( var i = 0; i < array.length; i++ ){
                if( value == array[i] ){
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
	      title:{ 
		text:graphtitle,
		show:true
		},
		grid: {
			borderWidth:0.0
		},
	      axes:{
	        xaxis:{
	          renderer:$.jqplot.DateAxisRenderer,
	          tickOptions:{
		    showGridline: false,
	            formatString:'%b&nbsp%#d',
		    markSize:0
	          } 
	        },
	        yaxis:{
	          tickOptions:{
	            formatString:'%.2f &nbsp',
		    markSize:0
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
		seriesDefaults:{
                	renderer:$.jqplot.BarRenderer,
                	pointLabels: { show: true }
            	},
		seriesColors: JQPLOT_COLORS,
		title:{ 
			text:graphtitle,
			show:true
		},
		grid: {
			borderWidth:0.0
		},
		axes:{
	        	xaxis:{
				ticks: xaxisarray,
				renderer: $.jqplot.CategoryAxisRenderer,
	        		tickOptions:{
				    showGridline: false,
				    markSize:0
		        	  } 
		        },
			yaxis:{
				tickOptions:{
					markSize:0
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
