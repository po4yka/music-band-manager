import {currentTableName} from "./table";

/**
 * Change active top navigation tab
 */
export function changeActiveTopNav() {
    const navSectionNewActive = document.getElementById(currentTableName + "Nav");
    const navSectionOldActive = document.getElementsByClassName("active");
    // FIXME: theoretically we can have more than 1 element
    if (navSectionOldActive.length > 0) {
        navSectionOldActive[0].classList.remove("active");
    }
    if (navSectionNewActive !== null) navSectionNewActive.classList.add("active");
}

export function changeAddBtnStatus() {
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