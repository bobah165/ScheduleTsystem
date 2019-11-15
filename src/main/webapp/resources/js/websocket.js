// INIT WEB SOCKET
// var ws = new WebSocket("ws://localhost:8080/schedule-1.0-SNAPSHOT/websocket");
var ws = new WebSocket("ws://localhost:8080/schedule-1.0-SNAPSHOT/websocket");

// HANDLERS
ws.onopen = function (event) {
    console.log('WS : Connection opened');
};
ws.onerror = function (event) {
    console.log('WS : Error occurred');
    var divErrorMessage = document.getElementById('ws-error-message');
    divErrorMessage.innerHTML = 'ERROR : Railway Timetable server was disconnected. Timetable will not be updated online!';

};
ws.onclose = function (event) {
    console.log('WS : Connection closed');
    var divErrorMessage = document.getElementById('ws-error-message');
    divErrorMessage.innerHTML = 'WARNING : Railway Timetable server was disconnected. Timetable will not be updated online!';
};
ws.onmessage = function (event) {
    onMessage(event);
};

// FUNCTIONS
function sendMessage() {    // WAS USED FOR TESTING
    var text = 'Hello Server!';
    ws.send(text);
    console.log(text);
    return false;
}

function onMessage(event) {

    console.log('WS : Message received : ' + event.data);

    // GOT TIMETABLE DTO -- NEED TO INJECT IT INTO TABLE

    var TimeTable = JSON.parse(event.data);

    var jsonArrArrivals = TimeTable;
    var jsonArrDepartures = TimeTable;

    console.log('Timetable = ' + TimeTable);
  //  console.log('Station = ' + jsonEndStationName);
    console.log('Arrivals = ' + jsonArrArrivals);
    console.log('Departures = ' + jsonArrDepartures);

    var tableArrivals = document.getElementById('table-ws-timetable-arrival');
    var tableDepartures = document.getElementById('table-ws-timetable-departure');
  //  var divTimetableStationName = document.getElementById('timetable-station-name');

    // UPDATE STATION NAME
  //  divTimetableStationName.innerText = 'Timetable::' + jsonStation.stationName;

    // ERASE OLD INFO
    tableArrivals.innerHTML =
        '<tr>' +
        '<th>' + 'Train' + '</th>' +
        '<th>' + 'Station Name' + '</th>' +
        '<th>' + 'Arrival time' + '</th>' +
        '</tr>'
    ;
    tableDepartures.innerHTML =
        '<tr>' +
        '<th>' + 'Train' + '</th>' +
        '<th>' + 'Station Name' + '</th>' +
        '<th>' + 'Departure time' + '</th>' +
        '</tr>'
    ;

    // FILL NEW INFO - ARRIVALS
    for (var i = 0; i < jsonArrArrivals.length; i++) {
        var json = jsonArrArrivals[i];
        console.log(json);

        tableArrivals.innerHTML +=
            '<tr>' +
            '<td>' + json.idTrain + '</td>' +
            '<td>' + json.nameStation + '</td>' +
            '<td>' + json.arrivalTime+ '</td>' +
            '</tr>'
        ;
    }

    // FILL NEW INFO - DEPARTURES
    for (var i = 0; i < jsonArrDepartures.length; i++) {
        var json = jsonArrDepartures[i];
        console.log(json);

        tableDepartures.innerHTML +=
            '<tr>' +
            '<td>' + json.idTrain + '</td>' +
            '<td>' + json.nameStation + '</td>' +
            '<td>' + json.departureTime + '</td>' +
            '</tr>'
        ;
    }

}

// TEST
if ('WebSocket' in window) {
    console.log('test : WebSocket in window');
}
