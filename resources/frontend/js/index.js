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
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
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
        switch (currentTableName) {
            case "groups":
                tRow.setAttribute("data-toggle", "modal");
                tRow.setAttribute("data-target", "#addModal");
                break;
            case "performers":
                break;
        }
        tRow.onclick = function() {
            console.log("click on table row");
            showTableRowInformation(tRow);
        };
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

function fillSelectForAddPerformer(select) {
    for (let performer of groupsData) {
        select.innerHTML += `<option>${performer.groupName}</option>`;
    }
}

function addNewElementMainOpen() {
    console.log(`addNewElementMain was called for ${currentTableName}`);
    const addElementBody = document.getElementById("addModalBody");
    const addElementTitle = document.getElementById("addModalTitleLabel");
    switch (currentTableName) {
        case "groups":
            console.log("Add new group triggered");
            addElementTitle.innerText = "Add new group";
            addElementBody.innerHTML = "<input type='text' class='form-control' id='enterGroupNameModal' placeholder='Group name' required>" +
                "<input type='date' class='form-control' id='enterGroupCreationModal' placeholder='Creation date' required>" +
                "<input type='text' class='form-control' id='enterGroupCountryModal' placeholder='Country' required>" +
                "<input type='number' class='form-control' id='enterGroupHitModal' placeholder='Hit parade' required>";
            break;
        case "performers":
            console.log("Add new performer triggered");
            addElementTitle.innerText = "Add new performer";
            addElementBody.innerHTML = "<select id='addModalSelect' class='form-control' required></select>" +
                "<input type='text' class='form-control' id='enterPerfNameModal' placeholder='Name' required>" +
                "<input type='date' class='form-control' id='enterPerfBirthModal' placeholder='Birthday' required>" +
                "<input type='text' class='form-control' id='enterPerfRoleModal' placeholder='Role' required>";
            const addElementSelect = document.getElementById("addModalSelect");
            fillSelectForAddPerformer(addElementSelect);
            break;
    }
}

function addNewElementRequest() {
    console.log("addNewElementRequest was called");
    switch (currentTableName) {
        case "groups":
            const groupName = document.getElementById("enterGroupNameModal");
            const groupCreationDate = document.getElementById("enterGroupCreationModal");
            const groupCountry = document.getElementById("enterGroupCountryModal");
            const groupHitParade = document.getElementById("enterGroupHitModal");
            if (groupName === null || groupCreationDate === null || groupCountry === null || groupHitParade === null ||
                groupName.value === "" || groupCreationDate.value === "" ||
                groupCountry.value === "" || groupHitParade.value === "") {
                alert("Fields can't be null!");
                return;
            }
            const dateSplit = groupCreationDate.value.split('-');
            const data = {
                groupName: groupName.value,
                creationTime: {year: dateSplit[0], month: dateSplit[1], day: dateSplit[2]},
                country: groupCountry.value,
                hitParadePlace: groupHitParade.value
            }
            sendRequest("POST", "/api/v1/group", data, true, (text) => {
                console.log("Callback for POST to /group");
                console.log(text);
            });
            groupsData = null;
            updateTable("groups");
            break;
        case "performers":
            const performerName = document.getElementById("enterPerfNameModal");
            const performerBirthday = document.getElementById("enterPerfBirthModal");
            const performerRole = document.getElementById("enterPerfRoleModal");
            const performerGroup = document.getElementById("addModalSelect");
            if (performerName === null || performerBirthday === null || performerRole === null || performerGroup === null ||
                performerName.value === "" || performerBirthday.value === "" ||
                performerRole.value === "" || performerGroup.value === "") {
                alert("Fields can't be null!");
                return;
            }
            break;
    }
}

function showTableRowInformation(row) {
    console.log("showTableRowInformation was called");
    const infoModalBody = document.getElementById("addModalBody");
    infoModalBody.innerHTML = "";
    const infoModalTitle = document.getElementById("addModalTitleLabel");
    switch (currentTableName) {
        case "groups":
            const groupName = row.cells[0].innerHTML;
            console.log(`for ${currentTableName} with name: ${groupName}`);
            infoModalTitle.innerText = `Information about ${groupName}`;
            const data = {
                groupName: groupName
            }
            sendRequest("GET", "/api/v1/lineup", data, true, (text) => {
                console.log("Callback for GET to /lineup");
                const lineupData = JSON.parse(text);
                console.log(lineupData);
            });
            break;
        case "performers":
            break;
    }
}

// default table
updateTable("groups");
