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
 * Generate header of table
 * @param table
 * @param tableHeaderData
 */
function generateTableHead(table, tableHeaderData) {
    let thead = table.createTHead();
    let row = thead.insertRow();
    for (let key of tableHeaderData) {
        let th = document.createElement('th');
        th.classList.add('text-left');
        let text = document.createTextNode(key);
        th.appendChild(text);
        row.appendChild(th);
    }
}

/**
 * Generate body of Group table
 * @param table
 * @param tableInfo
 */
function generateGroupTable(table, tableInfo) {
    for (let element of tableInfo) {
        let tRow = document.createElement("tr");
        const cells = []
        for (let j = 0; j < columnCount; ++j) {
            cells[j] = tRow.insertCell(j);
        }
        cells[0].innerHTML = tableInfo[i].groupName
        cells[1].innerHTML = `${tableInfo[i].creationTime.day}/${tableInfo[i].creationTime.month}/${tableInfo[i].creationTime.year}`
        cells[2].innerHTML = tableInfo[i].country
        cells[3].innerHTML = tableInfo[i].hitParadePlace
    }
}

/**
 * Redraw table based on menu option
 * @param tableName: string
 */
function updateTable(tableName) {
    let groupTblHeader = ["Group", "Creation time", "Country", "Hit parade place"];
    let columnCount = 0
    switch (tableName) {
        case "groups":
            columnCount = 4
            sendRequest("GET", "/api/v1/group", "", true, (text) => {
                console.log("Callback for GET to /group");
                const table = document.getElementById('dataTable')

                const tableInfo = JSON.parse(text);
                console.log(tableInfo);
                generateGroupTable(table, tableInfo);
                generateTableHead(table, groupTblHeader);
            })
    }
}