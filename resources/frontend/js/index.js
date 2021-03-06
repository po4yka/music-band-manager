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
            case "performers":
                tRow.setAttribute("data-toggle", "modal");
                tRow.setAttribute("data-target", "#addModal");
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
    if(currentTableName !== "extra") {
        changeAddBtnStatus();
        changeActiveTopNav();
        cleanTable(table);
    }
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
        case "extra":
            const addBtn = document.getElementById("addBtnInModal");
            addBtn.style.display = "none";

            const infoModalBody = document.getElementById("addModalBody");
            infoModalBody.innerHTML = "";
            const infoModalTitle = document.getElementById("addModalTitleLabel");
            infoModalTitle.innerText = "Extra information";

            sendRequest("GET", "/api/v1/anniversary", "", true, (text) => {
                console.log("Callback for GET to /anniversary");
                const anniversaryGroups = JSON.parse(text);
                console.log(anniversaryGroups);
                const header = document.createElement('h5');
                header.innerText = "Happy Anniversary Groups";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of anniversaryGroups) {
                    let li = document.createElement('li');
                    li.textContent = el.name;
                    ul.append(li);
                }
            });
            sendRequest("GET", "/api/v1/youngestartist", "", true, (text) => {
                console.log("Callback for GET to /youngestartist");
                const youngestArtist = JSON.parse(text)[0];
                console.log(youngestArtist);
                const header = document.createElement('h5');
                header.innerText = "Yougest singer in all groups";
                infoModalBody.append(header);
                const p = document.createElement('p');
                p.innerText = `${youngestArtist.fullName} from ${youngestArtist.groupName}`;
                infoModalBody.append(p);
            });
            sendRequest("GET", "/api/v1/avgageless", "", true, (text) => {
                console.log("Callback for GET to /avgageless");
                const avgAgeLessGroups = JSON.parse(text);
                console.log(avgAgeLessGroups);
                const header = document.createElement('h5');
                header.innerText = "Groups with average age < 45";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of avgAgeLessGroups) {
                    let li = document.createElement('li');
                    li.textContent = el.name;
                    ul.append(li);
                }
            });
            break;
    }
}

function fillSelectForAddPerformer(select) {
    if (groupsData === null) return;
    for (let performer of groupsData) {
        select.innerHTML += `<option>${performer.groupName}</option>`;
    }
}

function addNewElementMainOpen() {
    console.log(`addNewElementMain was called for ${currentTableName}`);

    const addBtn = document.getElementById("addBtnInModal");
    addBtn.style.display = "block";

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
            const birthdaySplit = performerBirthday.value.split('-');
            const performerData = {
                fullName: performerName.value,
                birthday: {year: birthdaySplit[0], month: birthdaySplit[1], day: birthdaySplit[2]},
                role: performerRole.value.toLowerCase(),
                groupName: performerGroup.value
            }
            sendRequest("POST", "/api/v1/performer", performerData, true, (text) => {
                console.log("Callback for POST to /performer");
                console.log(text);
            });
            performersData = null;
            updateTable("performers");
            break;
    }
}

function showTableRowInformation(row) {
    console.log("showTableRowInformation was called");

    const addBtn = document.getElementById("addBtnInModal");
    addBtn.style.display = "none";

    const infoModalBody = document.getElementById("addModalBody");
    infoModalBody.innerHTML = "";
    const infoModalTitle = document.getElementById("addModalTitleLabel");
    const groupName = row.cells[0].innerHTML;
    console.log(`for ${currentTableName} with name: ${groupName}`);
    const data = {
        name: groupName
    }
    switch (currentTableName) {
        case "groups":
            infoModalTitle.innerText = `Information about ${groupName}`;
            sendRequest("POST", "/api/v1/lineup", data, true, (text) => {
                console.log("Callback for POST to /lineup");
                const lineupData = JSON.parse(text);
                console.log(lineupData);
                const header = document.createElement('h5');
                header.innerText = "Band lineup";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of lineupData) {
                    let li = document.createElement('li');
                    const today = new Date();
                    const age = today.getFullYear() - el.birthday.year;
                    console.log(`today: ${today.getFullYear()}; date: ${el.birthday.year}`);
                    li.textContent = `${el.fullName}, ${el.role}, age: ${age}`;
                    ul.append(li);
                }
            });
            sendRequest("POST", "/api/v1/repertoire", data, true, (text) => {
                console.log("Callback for POST to /repertoire");
                const repertoireData = JSON.parse(text);
                console.log(repertoireData);
                const header = document.createElement('h5');
                header.innerText = "Band repertoire";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of repertoireData) {
                    let li = document.createElement('li');
                    li.textContent = `${el.name} - ${el.author}`;
                    ul.append(li);
                }
            });
            sendRequest("POST", "/api/v1/lastconcertcost", data, true, (text) => {
                console.log("Callback for POST to /lastconcertcost");
                const lastConcertTickerCostData = JSON.parse(text)[0];
                console.log(lastConcertTickerCostData);
                const header = document.createElement('h5');
                header.innerText = "Last concert ticket cost";
                infoModalBody.append(header);
                const paragraph = document.createElement('p');
                paragraph.innerText = `Last concert at ${lastConcertTickerCostData.place} cost ${lastConcertTickerCostData.ticketCost}`;
                infoModalBody.append(paragraph);
            })
            sendRequest("POST", "/api/v1/lasttourinfo", data, true, (text) => {
                console.log("Callback for POST to /lasttourinfo");
                const lastTourData = JSON.parse(text)[0];
                console.log(lastTourData);
                const header = document.createElement('h5');
                header.innerText = "Last tour information";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                const liTourName = document.createElement('li');
                liTourName.innerText = `Tour name: ${lastTourData.name}`;
                const liDates = document.createElement('li');
                liDates.innerText = `Timeline: from ${lastTourData.startDate.day}-${lastTourData.startDate.month}-${lastTourData.startDate.year} to ${lastTourData.endDate.day}-${lastTourData.endDate.month}-${lastTourData.endDate.year}`;
                const liRevenue = document.createElement('li');
                liRevenue.textContent = `Revenu for tour is: ${lastTourData.revenue}`;
                const liTickets = document.createElement('li');
                liTickets.innerText = `Was sold: ${lastTourData.sumTicketsSold} tickets at all with avg cost: ${lastTourData.avgTicketCost}`;
                ul.append(liTourName);
                ul.append(liDates);
                ul.append(liRevenue);
                ul.append(liTickets);
            })
            sendRequest("POST", "/api/v1/lasttourplacedate", data, true, (text) => {
                console.log("Callback for POST to /lasttourplacedate");
                const lastTourPlaceTimeData = JSON.parse(text);
                console.log(lastTourPlaceTimeData);
                const header = document.createElement('h5');
                header.innerText = "Places and dates of last tour";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of lastTourPlaceTimeData) {
                    const date = new Date(0);
                    date.setUTCSeconds(el.dateTime.seconds);
                    let li = document.createElement('li');
                    li.textContent = `${el.place}, ${date.toUTCString()}`;
                    ul.append(li);
                }
            })
            sendRequest("POST", "/api/v1/lasttourrep", data, true, (text) => {
                console.log("Callback for POST to /lasttourrep");
                const lastTourRepData = JSON.parse(text);
                console.log(lastTourRepData);
                const header = document.createElement('h5');
                header.innerText = "Repertoire of the last tour";
                infoModalBody.append(header);
                const ul = document.createElement('ul');
                infoModalBody.append(ul);
                for (let el of lastTourRepData) {
                    let li = document.createElement('li');
                    li.textContent = `${el.name}, ${el.author}`;
                    ul.append(li);
                }
            })
            break;
        case "performers":
            infoModalTitle.innerText = `Change performer from ${groupName} group`;
            infoModalBody.innerHTML += "<select id='changePerformerSelect' class='form-control' required></select>";
            const changePerformerGroupSelect = document.getElementById("changePerformerSelect");
            fillSelectForAddPerformer(changePerformerGroupSelect);

            const chgBtn = document.createElement("button");
            chgBtn.innerText = "Change";
            chgBtn.className += "btn btn-primary btn-sm";
            infoModalBody.appendChild(chgBtn);
            const dateSplit = row.cells[1].innerHTML.split('/');
            chgBtn.addEventListener ("click", function() {
                const performerData = {
                    fullName: row.cells[0].innerHTML,
                    birthday: { year: dateSplit[2], month: dateSplit[1], day: dateSplit[0] },
                    newGroupName: changePerformerGroupSelect.value
                }
                sendRequest("POST", "/api/v1/chggroup", performerData, true, (text) => {
                    console.log("Callback for POST to /chggroup");
                    console.log(text);
                    updateTable("performers");
                });
            });
            break;
    }
}

// default table
updateTable("groups");
