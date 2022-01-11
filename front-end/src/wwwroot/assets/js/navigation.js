var id = 0;

function initPage(){
    if (id === 0) {
        if (window.history.state == null) {
            window.history.replaceState({
                id: id,
                url: window.location.href,
                //title: document.title,
                hasParam: window.location.search != null && window.location.search != ""
            }, "");
        }/* else {
            id = window.history.state.id;
        }*/
    }
}

window.addEventListener('popstate', (event) => {
    //console.log(event.state);
    document.title = event.state.title;
    //document.getElementById("page-title").innerHTML = event.state.title;
    console.log("POP: " + event.state.url);
    loadPage(event.state.url, event.state.hasParam);
    id = event.state.id;
})


function updatePage(newContent) {
    const container = document.getElementById("view-container");
    container.innerHTML = newContent;
}

function loadPage(url, hasParam) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            updatePage(xhr.responseText);
        }
    };
    xhr.open("GET", `${url}${hasParam ? '&' : '?'}raw`, true);
    xhr.send();
}

function changePage(url, hasParam = false) {
    window.history.pushState({id: ++id, url: url, hasParam: hasParam}, "", url);
    console.log(window.location.href);
    loadPage(url, hasParam);
}

function changePageIndex(index) {
    console.log(window.location.search);
    if (window.location.search == null || window.location.search == "") {
        changePage(`${window.location.pathname}?index=${index}`, true);
    } else {
        changePage(`${window.location.pathname}&index=${index}`, true);
    }
}