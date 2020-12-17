function sendRequest(requestType, URL, data = "", sync = true,
                     callback = (text) => {
                         console.log(text);
                         updateTable();
                     }) {

    console.log("Sending", requestType, "request at URL:", URL, "with data", data);

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
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

function fillCells(tableName, cells, element) {
    switch (tableName) {
        case "groups":
            cells[0].innerHTML = element.groupName;
            cells[1].innerHTML = `${element.creationTime.day}/${element.creationTime.month}/${element.creationTime.year}`;
            cells[2].innerHTML = element.country;
            cells[3].innerHTML = element.hitParadePlace;
            break;
        case "performers":
            cells[0].innerHTML = element.fullName;
            cells[1].innerHTML = `${element.birthday.day}/${element.birthday.month}/${element.birthday.year}`;
            cells[2].innerHTML = element.groupId;
            cells[3].innerHTML = element.role;
            break;
        case "songs":
            break;
        case "tour-programs":
            break;
        case "concerts":
            break;
    }
}

function generateTableBody(tableName, table, tableInfo, columnCount) {
    for (let element of tableInfo) {
        let tRow = table.insertRow();
        const cells = []
        for (let j = 0; j < columnCount; ++j) {
            cells[j] = tRow.insertCell(j);
        }
        fillCells(tableName, cells, element);
    }
}

/**
 * Change active top navigation tab
 * @param tableName
 */
function changeActiveTopNav(tableName) {
    const navSectionNewActive = document.getElementById(tableName + "Nav");
    const navSectionOldActive = document.getElementsByClassName("active");
    console.log(`navSectionNewActive: ${navSectionNewActive}; navSectionOldActive: ${navSectionOldActive}, ${navSectionOldActive.classList}`);
    // FIXME: theoretically we can have more than 1 element
    navSectionOldActive.classList[0].remove("active");
    navSectionNewActive.classList.add("active");
}

/**
 * Redraw table based on menu option
 * @param tableName: string
 */
function updateTable(tableName) {
    changeActiveTopNav(tableName);
    switch (tableName) {
        case "groups":
            const groupTblHeader = ["Group", "Creation time", "Country", "Hit parade place"];
            sendRequest("GET", "/api/v1/group", "", true, (text) => {
                console.log("Callback for GET to /group");
                const table = document.getElementById('dataTable')

                const tableInfo = JSON.parse(text);
                console.log(tableInfo);
                generateTableBody(tableName, table, tableInfo, 4);
                generateTableHead(table, groupTblHeader);
            })
            break;
        case "performers":
            const performerTblHeader = ["Full name", "Birthday", "Group", "Role"];
            sendRequest("GET", "/api/v1/performer", "", true, (text) => {
                console.log("Callback for GET to /performer");
                const table = document.getElementById('dataTable')

                const tableInfo = JSON.parse(text);
                console.log(tableInfo);
                generateTableBody(tableName, table, tableInfo, 4);
                generateTableHead(table, performerTblHeader, 4);
            })
            break;
        case "songs":
            break;
        case "tour-programs":
            break;
        case "concerts":
            break;
    }
}

// FIXME: default table show
updateTable("groups");