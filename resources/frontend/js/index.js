let currentTableName = null;

let groupsData = null;
let performersData = null;
let songsData = null;
let repertoiresData = null;
let tourProgramsData = null;
let concertsData = null;

const groupTblHeader = ["Group", "Creation time", "Country", "Hit parade place"];
const performerTblHeader = ["Full name", "Birthday", "Group", "Role"];
const songTblHeader = ["Name", "Author", "Group", "Creation year", "Composer"];
const tourProgramTblHeader = ["Name", "Group", "Start", "End"];
const concertTblHeader = ["Tour name", "Group", "Date/Time", "Place", "Tickets sold", "Ticket cost", "Rent cost"];

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
 * Change active top navigation tab
 */
function changeActiveTopNav() {
    const navSectionNewActive = document.getElementById(currentTableName + "Nav");
    const navSectionOldActive = document.getElementsByClassName("active");
    // FIXME: theoretically we can have more than 1 element
    if (navSectionOldActive.length > 0) {
        navSectionOldActive[0].classList.remove("active");
    }
    if (navSectionNewActive !== null) navSectionNewActive.classList.add("active");
}

function changeAddBtnStatus() {
    const btn = document.getElementById("addBtnMain");
    switch (currentTableName) {
        case "groups":
            btn.style.display = "block";
            btn.textContent = "Add new group";
            break;
        case "performers":
            btn.style.display = "block";
            btn.textContent = "Add new performer";
            break;
        default:
            btn.style.display = "none";
    }
}

/**
 * Generate header of table
 * @param table
 * @param tableHeaderData
 */
function generateTableHead(table, tableHeaderData) {
    console.log("generateTableHead was called");
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
 * Fill cells of table based on this name
 * @param cells
 * @param element
 */
function fillCells(cells, element) {
    console.log("fillCells was called");
    switch (currentTableName) {
        case "groups":
            cells[0].innerHTML = element.groupName;
            cells[1].innerHTML = `${element.creationTime.day}/${element.creationTime.month}/${element.creationTime.year}`;
            cells[2].innerHTML = element.country;
            cells[3].innerHTML = element.hitParadePlace;
            break;
        case "performers":
            cells[0].innerHTML = element.fullName;
            cells[1].innerHTML = `${element.birthday.day}/${element.birthday.month}/${element.birthday.year}`;
            cells[2].innerHTML = element.groupName;
            cells[3].innerHTML = element.role;
            break;
        case "songs":
            cells[0].innerHTML = element.name;
            cells[1].innerHTML = element.author;
            cells[2].innerHTML = element.groupName;
            cells[3].innerHTML = element.creationYear.year;
            cells[4].innerHTML = element.composer;
            break;
        case "tour-programs":
            cells[0].innerHTML = element.name;
            cells[1].innerHTML = element.groupName;
            cells[2].innerHTML = `${element.startDate.day}/${element.startDate.month}/${element.startDate.year}`;
            cells[3].innerHTML = `${element.endDate.day}/${element.endDate.month}/${element.endDate.year}`;
            break;
        case "concerts":
            const date = new Date(0);
            console.log(`Seconds for concert: ${element.dateTime.seconds}`);
            date.setUTCSeconds(element.dateTime.seconds);

            cells[0].innerHTML = element.tourProgramName;
            cells[1].innerHTML = element.groupName;
            cells[2].innerHTML = date.toUTCString();
            cells[3].innerHTML = element.place;
            cells[4].innerHTML = element.ticketsCount;
            cells[5].innerHTML = element.ticketCost;
            cells[6].innerHTML = element.hallRentalCost;
            break;
    }
}

function generateTableBody(table, tableInfo, columnCount) {
    console.log("generateTableBody was called");
    for (let element of tableInfo) {
        let tRow = table.insertRow();
        const cells = []
        for (let j = 0; j < columnCount; ++j) {
            cells[j] = tRow.insertCell(j);
        }
        fillCells(cells, element);
    }
}

/**
 * Delete table header and body
 * @param table
 */
function cleanTable(table) {
    if (table !== null) {
        const theadRef = table.getElementsByTagName("thead");
        const bodyRef = table.getElementsByTagName("tbody");
        if (theadRef.length > 0 && bodyRef.length > 0) {
            console.log("Recreating of table header and body");
            table.removeChild(theadRef[0]);
            table.removeChild(bodyRef[0]);
            table.appendChild(document.createElement('thead'));
            table.appendChild(document.createElement('tbody'));
        }
    }
}

/**
 * Redraw table based on menu option
 * @param tableName: string
 */
function updateTable(tableName) {
    console.log("updateTable was called");
    currentTableName = tableName;
    const table = document.getElementById('dataTable');
    if (table === null || currentTableName == null) return;
    changeActiveTopNav();
    changeAddBtnStatus();
    cleanTable(table);
    switch (currentTableName) {
        case "groups":
            if (groupsData === null) {
                sendRequest("GET", "/api/v1/group", "", true, (text) => {
                    console.log("Callback for GET to /group");

                    groupsData = JSON.parse(text);
                    console.log(groupsData);

                    // FIXME: delete duplicate lines
                    generateTableBody(table, groupsData, 4);
                    generateTableHead(table, groupTblHeader);
                });
            } else {
                generateTableBody(table, groupsData, 4);
                generateTableHead(table, groupTblHeader);
            }
            break;
        case "performers":
            if (performersData === null) {
                sendRequest("GET", "/api/v1/performer", "", true, (text) => {
                    console.log("Callback for GET to /performer");

                    performersData = JSON.parse(text);
                    console.log(performersData);

                    generateTableBody(table, performersData, 4);
                    generateTableHead(table, performerTblHeader, 4);
                });
            } else {
                generateTableBody(table, performersData, 4);
                generateTableHead(table, performerTblHeader, 4);
            }
            break;
        case "songs":
            if (songsData === null) {
                sendRequest("GET", "/api/v1/song", "", true, (text) => {
                    console.log("Callback for GET to /song");

                    songsData = JSON.parse(text);
                    console.log(songsData);

                    generateTableBody(table, songsData, 5);
                    generateTableHead(table, songTblHeader, 5);
                });
            } else {
                generateTableBody(table, songsData, 5);
                generateTableHead(table, songTblHeader, 5);
            }
            break;
        case "tour-programs":
            if (tourProgramsData === null) {
                sendRequest("GET", "/api/v1/tour-program", "", true, (text) => {
                    console.log("Callback for GET to /song");

                    tourProgramsData = JSON.parse(text);
                    console.log(tourProgramsData);

                    generateTableBody(table, tourProgramsData, 4);
                    generateTableHead(table, tourProgramTblHeader, 4);
                });
            } else {
                generateTableBody(table, tourProgramsData, 4);
                generateTableHead(table, tourProgramTblHeader, 4);
            }
            break;
        case "concerts":
            if (concertsData === null) {
                sendRequest("GET", "/api/v1/concert", "", true, (text) => {
                    console.log("Callback for GET to /concert");

                    concertsData = JSON.parse(text);
                    console.log(concertsData);

                    generateTableBody(table, concertsData, 7);
                    generateTableHead(table, concertTblHeader, 7);
                });
            } else {
                generateTableBody(table, concertsData, 7);
                generateTableHead(table, concertTblHeader, 7);
            }
            break;
    }
}

function addNewElementMain() {
    const addElementTitle = document.getElementById("addModalTitleLabel");
    const addElementBody = document.getElementById("addModalBody");
    const addElementDropdown = document.getElementById("addModalBody");
    addElementBody.innerHTML = "";
    switch (currentTableName) {
        case "groups":
            addElementTitle.innerText = "Add new group";
            addElementDropdown.style.display = "none";
            break;
        case "performers":
            addElementTitle.innerText = "Add new performer";
            addElementDropdown.style.display = "block";
            break;
    }
}

// default table
updateTable("groups");
