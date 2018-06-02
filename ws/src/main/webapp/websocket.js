/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var websocket = null;
var parsers = [];
            
function connect() {
    var wsProtocol = document.location.protocol === "https:" ? "wss" : "ws";
    var wsURI = wsProtocol + '://' + document.location.host  + document.location.pathname + 'ws';
    websocket = new WebSocket(wsURI);
    displayMessage('wsuri: ' + wsURI);
    
    websocket.onopen = function() {
        displayStatus('Open');
        document.getElementById('confirm').disabled = false;
        document.getElementById('detail').disabled = false;
        displayMessage('Connection is now open.');
    };
    websocket.onmessage = function(event) {
    	var parser = JSON.parse(event.data);
    	for (var item in parser) {
    		parsers.push(item);
    	}
        // log the event
        displayMessage('The response was received! ' + parser, 'success');
    };
    websocket.onerror = function(event) {
        // log the event
        displayMessage('Error! ' + event.data, 'error');
    };
    websocket.onclose = function() {
        displayStatus('Closed');
        displayMessage('The connection was closed or timed out. Please click the Open Connection button to reconnect.');
        document.getElementById('confirm').disabled = true;
        document.getElementById('detail').disabled = true;
    };
}
function disconnect() {
    if (websocket !== null) {
        websocket.close();
        websocket = null;
    }
    message.setAttribute("class", "message");
    message.value = 'WebSocket closed.';
    // log the event
}
function sendMessage(id, command) {
    if (websocket !== null) {
    	var msg;
		msg=JSON.stringify({ id: id, command: command });
        websocket.send(msg);
    } else {
        displayMessage('WebSocket connection is not established. Please click the Open Connection button.', 'error');
    }
}
function displayMessage(data, style) {
    var message = document.getElementById('hellomessage');
    message.setAttribute("class", style);
    message.value = data;
}
function displayStatus(status) {
    var currentStatus = document.getElementById('currentstatus');
    currentStatus.value = status;
}