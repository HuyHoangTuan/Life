/*function buildParameter(form, type) {
    let formData = new FormData(form);
    let jsonObject = {};
    let failed = 0;
    for (let entry of formData.entries()) {
        jsonObject[entry[0]] = entry[1].trim();
        if ((type == 1 && entry[1] != old[entry[0]]) || type == 0) {
            let elmRow = document.getElementById(`${entry[0]}-row`);
            if (elmRow.getAttribute("func") == "") continue;
            let func = window[elmRow.getAttribute("func")];
            console.log("FUNC: " + elmRow.getAttribute("func") + " : " + `${entry[0]}-row`);
            let checkResult = func(entry[1].trim());
            if (checkResult != 0) {
                failed = 1;
                elmRow.className = "invalid";
                hideErrors(elmRow);
                console.log("RES: " + `error-${entry[0]}-${checkResult}`);
                document.getElementById(`error-${entry[0]}-${checkResult}`).className = "error";
            } else {
                elmRow.className = "";
                hideErrors(elmRow);
            }
        }
    }
    return failed == 1 ? 0 : jsonObject;
}

function submitAddForm() {
    const form = document.getElementById("add-form");
    let obj = buildParameter(form, 0);
    if (obj == 0) return;
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(xhr.responseText);
            if (xhr.responseText == "success") {
                changePage(window.location.pathname);
                showWindow("add-dialog");
                document.getElementById("added-status").className = "status-message success";
            } else {
                showWindow("error-dialog");
                document.getElementById("db-error-status").className = "status-message error";
            }
        }
    };
    xhr.open("POST", `${form.getAttribute("action")}`, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(obj));
}

function submitEditForm() {
    const form = document.getElementById("edit-form");
    let obj = buildParameter(form, 1);
    if (obj == 0) return;
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(xhr.responseText);
            if (xhr.responseText == "success") {
                let url = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/"));
                changePage(url);
                showWindow("update-dialog");
                document.getElementById("updated-status").className = "status-message success";
            } else {
                showWindow("error-dialog");
                document.getElementById("db-error-status").className = "status-message error";
            }
        }
    };
    xhr.open("PUT", `${form.getAttribute("action")}`, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(obj));
}

var delId;

function submitDelete() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(xhr.responseText);
            if (xhr.responseText == "success") {
                changePage(window.location.pathname);
                hideWindow("confirm-dialog");
                showWindow("delete-dialog");
                document.getElementById("deleted-status").className = "status-message success";
            } else {
                hideWindow("confirm-dialog");
                showWindow("error-dialog");
                document.getElementById("db-error-status").className = "status-message error";
            }
        }
    };
    xhr.open("DELETE", `${window.location.pathname}/${delId}`, true);
    xhr.send();
}

function doDelete(id) {
    showWindow("confirm-dialog");
    delId = id;
    event.preventDefault();
    event.stopPropagation();
    //changePage(window.location.pathname);
}

function hideWindow(id) {
    let elm = document.getElementById("blur");
    let dialog = document.getElementById(id);
    elm.className = "blur hidden";
    dialog.className = "windows hidden";
}

function showWindow(id) {
    let elm = document.getElementById("blur");
    let dialog = document.getElementById(id);
    elm.className = "blur";
    dialog.className = "windows";
}

function buildSearchParameter(form) {
    let formData = new FormData(form);
    let param = "";
    for (let entry of formData.entries()) {
        if (!entry[0].endsWith("check") && (entry[1] != "")) {
            if (param != "") {
                param = param + "&";
            }
            param = `${param}filter=${entry[0]}&keyword=${entry[1]}`;
        }
    }
    return param;
}

function querySearch() {
    const searchValue = document.getElementById("search-area");
    const filterForm = document.getElementById("filter-form");
    let url = `${window.location.pathname}?`;
    let param = buildSearchParameter(filterForm);
    let hasParam = true;

    if (searchValue.value == null || searchValue.value.trim() == "") {
        if (param != "") {
            url = url + param;
        } else {
            url = `${window.location.pathname}`;
            hasParam = false;
        }
    } else {
        url = url + `filter=${searchValue.getAttribute("name")}&keyword=${searchValue.value.trim()}${param == "" ? "" : "&" + param}`;
    }
    window.history.pushState({
        id: ++id,
        url: window.location.origin + url,
        title: document.title,
        hasParam: hasParam
    }, "", url);
    loadPage(url, hasParam);
}

function toggleFilter() {
    let filter = document.getElementById("filter");
    if (filter.className == "") {
        filter.className = "hidden";
    } else {
        filter.className = "";
    }
}*/

function clearInput(id) {
  let input = document.getElementById(`${id}-input`);
  input.value = null;
  console.log("delete " + id);
}
var lastButton;
function dropdownSingle(element) {
  var dropdowns = document.getElementById("music-setting-content");

  if (lastButton == element) {
    dropdowns.classList.toggle("show");
  } else {
    dropdowns.classList.add("show");
  }
  lastButton = element;
  var a = element.getBoundingClientRect();
  console.log(a.top + "px");
  dropdowns.style.position = "fixed";
  dropdowns.style.top = a.y + "px";
  dropdowns.style.left = a.right + "px";
  dropdowns.style.position = "absolute";
}

window.onclick = function (event) {
  if (!event.target.matches(".dropbtn")) {
    var dropdowns = document.getElementById("music-setting-content");
    /*for(let i=0;i<dropdowns.length;i++){
            var openDropdown=dropdowns[i];
            if(openDropdown.classList.contains("show")){
                openDropdown.classList.remove("show");
            }
        }*/
  }
};
function volumnPopup(element) {
  var volPop = document.getElementById("volumn-content");
  volPop.classList.toggle("show");
}
function searchDropdown(element) {
  var dropdown;
  if (element.classList.contains("song-btn")) {
    dropdown = document.getElementById("dropdown-content-song");
  } else if (element.classList.contains("album-btn")) {
    dropdown = document.getElementById("dropdown-content-album");
  } else if (element.classList.contains("playlist-btn")) {
    dropdown = document.getElementById("dropdown-content-playlist");
  } else if (element.classList.contains("artist-btn")) {
    dropdown = document.getElementById("dropdown-content-artist");
  }
  if (lastButton == element) {
    dropdown.classList.toggle("show");
  } else {
    dropdown.classList.add("show");
  }
  lastButton = element;
  var a = element.getBoundingClientRect();

  dropdown.style.position = "fixed";
  dropdown.style.top = a.top + "px";
  dropdown.style.left = a.right + "px";
  console.log(dropdown.classList.toggle("show"));
}
function switchTab(element) {
  var tabContent = document.getElementsByClassName("tab-content");
  for (let i = 0; i < tabContent.length; i++) {
    tabContent[i].style.display = "none";
  }
  var lastTabActive = document.querySelectorAll(".tab.active");
  console.log(lastTabActive);
  lastTabActive[0].classList.toggle("active", false);
  element.classList.toggle("active", true);
  var tab = document.getElementById(`${element.id}-content`);
  tab.style.display = "block";
}
function deleteConfirm(element) {
  var checkBox = document.getElementById("dialog-container");
  checkBox.classList.toggle("show");
}
function cancel(element) {
  var checkBox = document.getElementById("dialog-container");
  checkBox.classList.toggle("show");
}
function scrollToContent(element) {
  var destinationElement = document.getElementById(`${element.id}-content`);
  var contentElement = destinationElement.parentElement;
  var paddingElement = contentElement.previousSibling.previousSibling;
  var scrollElement = contentElement.parentElement;
  // console.log(destinationElement.getBoundingClientRect().y + " " + contentElement.getBoundingClientRect().y);
  scrollElement.scrollTop =
    destinationElement.getBoundingClientRect().y -
    contentElement.getBoundingClientRect().y +
    paddingElement.getBoundingClientRect().height;
}
var scrolling = 0;
function scroll_(element) {
  if (scrolling == 1) return;
  scrolling = 1;
  console.log(scrolling);
  var left = document.getElementById("btn-left");
  var right = document.getElementById("btn-right");
  var content = document.getElementById("index-function");
  var contentScrollLeft = content.scrollLeft;
  if (element == right) {
    contentScrollLeft += content.getBoundingClientRect().width * 0.511;
    content.scrollLeft = contentScrollLeft;
  }
  if (element == left) {
    contentScrollLeft -= content.getBoundingClientRect().width * 0.511;
    content.scrollLeft = contentScrollLeft;
  }
  setTimeout(() => {
    scrolling = 0;
  }, 500);
}

function faqDropdown(element) {
  var faqContent = document.getElementById(`${element.id}-content`);
  faqContent.classList.toggle("faq-show");
  faqContent.style.width = "100%";
}
// function goLeftAnimation(element) {
//   console.log(windows.scrollTop);
//   if (document.documentElement.scrollTop > 100) {
//     document.getElementById("index-content").className = "slide-left";
//     console.log("acbc");
//   }
// }

// define an observer instance
var observer = new IntersectionObserver(onIntersection, {
  root: null, // default is the viewport
  threshold: 0.0001, // percentage of taregt's visible area. Triggers "onIntersection"
});

// callback is called on intersection change
function onIntersection(entries, opts) {
  entries.forEach((entry) => {
    entry.target.classList.toggle('slide-left', entry.isIntersecting);
    // console.log("lolo");
  });
}

var observer2 = new IntersectionObserver(onIntersection2, {
  root: null, // default is the viewport
  threshold: 0.0001, // percentage of taregt's visible area. Triggers "onIntersection"
});

// callback is called on intersection change
function onIntersection2(entries, opts) {
  entries.forEach((entry) => {
    entry.target.classList.toggle('slide-top', entry.isIntersecting);
    console.log("lolo");
    console.log(entry.isIntersecting);

  });
}
var observer3 = new IntersectionObserver(onIntersection3, {
  root: null, // default is the viewport
  threshold: 0.0001, // percentage of taregt's visible area. Triggers "onIntersection"
});

// callback is called on intersection change
function onIntersection3(entries, opts) {
  entries.forEach((entry) => {
    entry.target.classList.add('slide-top', entry.isIntersecting);
    console.log("lolo");
    console.log(entry.isIntersecting);
    
  });
}

// Use the bserver to observe an element
function init() {
  observer.observe(document.getElementById("index-function"));
  observer2.observe(document.getElementById("faq"));
  observer3.observe(document.getElementById("index-general"));
}

// To stop observing:
// observer.unobserve(entry.target)
