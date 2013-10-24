
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
            markerOptions: { style: "filledCircle" }
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
