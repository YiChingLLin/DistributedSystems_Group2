let res = []

$(function () {
    res = getRandomData();
    initRealTimeChart();
});

var realtime = 'on';
function initRealTimeChart() {
    //Real time ==========================================================================================
    var plot1 = $.plot('#real_time_chart_1', [res[0]], {
        series: {
            shadowSize: 0,
            color: 'rgb(0, 188, 212)'
        },
        grid: {
            borderColor: '#f3f3f3',
            borderWidth: 1,
            tickColor: '#f3f3f3'
        },
        lines: {
            fill: true
        },
        yaxis: {
            min: 0,
            max: 100
        },
        xaxis: {
            min: 0,
            max: 100
        }
    });

    var plot2 = $.plot('#real_time_chart_2', [res[1]], {
        series: {
            shadowSize: 0,
            color: 'rgb(0, 188, 212)'
        },
        grid: {
            borderColor: '#f3f3f3',
            borderWidth: 1,
            tickColor: '#f3f3f3'
        },
        lines: {
            fill: true
        },
        yaxis: {
            min: 0,
            max: 100
        },
        xaxis: {
            min: 0,
            max: 100
        }
    });

    var plot3 = $.plot('#real_time_chart_3', [res[2]], {
        series: {
            shadowSize: 0,
            color: 'rgb(0, 188, 212)'
        },
        grid: {
            borderColor: '#f3f3f3',
            borderWidth: 1,
            tickColor: '#f3f3f3'
        },
        lines: {
            fill: true
        },
        yaxis: {
            min: 0,
            max: 100
        },
        xaxis: {
            min: 0,
            max: 100
        }
    });

    function updateRealTime() {
        let res = getRandomData();
        plot1.setData([res[0]]);
        plot1.draw();
        plot2.setData([res[1]]);
        plot2.draw();        
        plot3.setData([res[2]]);
        plot3.draw();

        document.getElementById("bus1_num").innerHTML = res[3][res[3].length-1]
        document.getElementById("bus2_num").innerHTML = res[4][res[4].length-1]
        document.getElementById("bus3_num").innerHTML = res[5][res[5].length-1]

        var timeout;
        if (realtime === 'on') {
            timeout = setTimeout(updateRealTime, 500);
        } else {
            clearTimeout(timeout);
        }
    }

    updateRealTime();

    $('#realtime').on('change', function () {
        realtime = this.checked ? 'on' : 'off';
        updateRealTime();
    });
}


let data1 = [];
let data2 = [];
let data3 = [];
let totalPoints = 110;
count = 0
function getRandomData() {
    $.ajax({
        url: 'http://127.0.0.1:3001/data',
        method: 'GET',
        dataType: 'json',
        success: res =>{
            res.forEach((val)=>{
                tmp = JSON.parse(val)
                switch(tmp.carNo){
                    case "NCCU-001":
                        data1.push(tmp.currentNum);
                        if (data1.length > totalPoints){
                            data1.shift();
                        }
                        break;
                    case "NCCU-002":
                        data2.push(tmp.currentNum);
                        if (data2.length > totalPoints){
                            data2.shift();
                        }
                        break;
                    case "NCCU-003":
                        data3.push(tmp.currentNum);
                        if (data3.length > totalPoints){
                            data3.shift();
                        }
                        break;
                }
            })  
        },
        error: err =>{
            console.log(err)
        },
    });



    let res = [[],[],[],[],[],[]];
    for (let i = 0; i < data1.length; ++i) {
        res[0].push([i, data1[i]]);
    }
    for (let j = 0; j < data2.length; ++j) {
        res[1].push([j, data2[j]]);
    }
    for (let k = 0; k < data3.length; ++k) {
        res[2].push([k, data3[k]]);
    }
    res[3] = data1
    res[4] = data2
    res[5] = data3

    return res;
}



  
