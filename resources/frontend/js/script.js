function sendRequest(requestType, URL, data = "", sync = true,
                     callback = (text) =>
                     {
                         console.log(text);
                         updateTable();
                     }) {

    console.log("Sending", requestType, "request at URL:", URL, "with data", data);

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            callback(this.responseText);
        }
    }

    xhttp.open(requestType, URL, sync);
    xhttp.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    xhttp.send(JSON.stringify(data));
}

/**
 * Redraw table based on menu option
 * @param tableName: string
 */
function updateTable(tableName) {
    switch (tableName) {
        case "groups":
            sendRequest("GET", "/api/v1/group", "", true, (text) => {
                console.log("Callback for GET to /group");
                console.log(`TEXT:\n${text}`)
            })
    }
}