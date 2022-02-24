var id = 0;

window.addEventListener("load", (e) => {
	if (window.history.state == null) {
		let url = window.location.pathname + window.location.search;
		window.history.replaceState({ url: url }, "title", url);
	}
});

window.addEventListener("popstate", (e) => {
	changePage(e.state.url, true);
});

function updateContent(statusCode, content) {
	const container = document.getElementById("page-view");
	container.innerHTML = content;
}

function changePage(url, back = false) {
	console.log("[Change]" + url);
	XHR("GET", buildRawURL(url), (statusCode, data) => {
		console.log(data);
		jsonData = JSON.parse(data);
		document.title = jsonData.title;
		if (!back) {
			window.history.pushState({ url: url }, "title", url);
		}
		updateContent(statusCode, jsonData.content);
	});
}

function buildRawURL(path) {
	let url = new URL(document.location.origin + path);
	url.searchParams.append("raw", true);
	return url;
}

var last;

function search(keyword) {
	if (keyword == "") return;
	clearTimeout(last);
	last = setTimeout(() => {
		changePage(`/search?keyword=${keyword}`);
	}, 1000);
}
