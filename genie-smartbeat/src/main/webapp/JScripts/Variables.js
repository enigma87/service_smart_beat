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
