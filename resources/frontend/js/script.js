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
    let tableHeader = []
    let columnCount = 0
    switch (tableName) {
        case "groups":
            tableHeader[0] = "Group"
            tableHeader[1] = "Creation time"
            tableHeader[2] = "Country"
            tableHeader[3] = "Hit parade place"
            columnCount = 4
            sendRequest("GET", "/api/v1/group", "", true, (text) => {
                console.log("Callback for GET to /group");
                const table = document.getElementById('dataTable')

                const header = table.createTHead();
                const row = header.insertRow(0);
                for (let i = 0; i < columnCount; ++i) {
                    let cell = row.insertCell(i);
                    cell.innerHTML = `<th class='text-left'>${tableHeader[i]}</th>>`
                }

                const tableInfo = JSON.parse(text);
                for (let i = 0; i < tableInfo.length; ++i) {
                    let tRow = table.insertRow(i);
                    const cells = []
                    for (let j = 0; j < columnCount; ++j) {
                        cells[j] = tRow.insertCell(j);
                    }
                    cells[0].innerHTML = tableInfo[i].groupName
                    cells[1].innerHTML = `${tableInfo[i].creationTime.day}-${tableInfo[i].creationTime.month}-${tableInfo[i].creationTime.year}`
                    cells[2].innerHTML = tableInfo[i].country
                    cells[3].innerHTML = tableInfo[i].hitParadePlace
                }
            })
    }
}