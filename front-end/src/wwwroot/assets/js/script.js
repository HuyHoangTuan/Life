function showDetailDialog(elm) {
	let attrs = elm.attributes;
	let dialog = document.getElementById("dialog-detail");
	for (var i = attrs.length - 1; i >= 0; i--) {
		let field = dialog.querySelector(`[field='${attrs[i].name}']`);
		if (field != undefined) {
			if (field.tagName == "IMG") {
				field.setAttribute("src", attrs[i].value);
			}
			if (field.tagName == "AUDIO") {
				field.src = `/tracks/${attrs[i].value}/audio`;
				console.log(field);
			} else {
				field.setAttribute("value", attrs[i].value);
				field.value = attrs[i].value;
			}
		}
	}
	dialog.classList.toggle("hidden");
}

function toggleDialog(dialogID, callback = null) {
	event.preventDefault();
	event.stopPropagation();
	let dialog = document.getElementById(dialogID);
	dialog.classList.toggle("hidden");
	console.log(callback);
	if (callback) callback(dialog);
	else
	{
		///if(dialogID == "dialog-status-ok") window.location.reload();
	}
}

function toggleOk()
{
	window.location.reload();
}
// function loadRemoteAudio(elm) {
// 	let editForm = document.getElementById("dialog-edit");
// 	let editForm.querySelector(".audio-preview")
// }

function previewImg(srcElm) {
	console.log("onchange");
	let targetField = srcElm.getAttribute("target-field");
	let target = document.querySelector(`[field="${targetField}"]`);
	if (srcElm.files && srcElm.files[0]) {
		let reader = new FileReader();
		reader.onload = (e) => {
			target.setAttribute("src", e.target.result);
		};
		reader.readAsDataURL(srcElm.files[0]);
	}
}

function initMiniPlayer() {
	console.log("Init mini players");
	let players = document.querySelectorAll(".audio-preview");
	players.forEach((player) => {
		let targetAudio = player.querySelector("audio");
		targetAudio.oncanplay = () => {
			sliderTime.value = 0;
			buttonPlay.innerText = "play_arrow";
		};
		let controller = player.querySelector(".controller");
		console.log(controller);
		let sliderTime = controller.querySelector(".sl-time");
		let buttonPlay = controller.querySelector(".bt-play");
		buttonPlay.addEventListener("click", () => {
			if (targetAudio.paused) {
				targetAudio.play();
			} else {
				targetAudio.pause();
			}
		});
		targetAudio.onloadstart = () => {
			buttonPlay.innerText = "play_arrow";
		};
		targetAudio.onpause = () => {
			buttonPlay.innerText = "play_arrow";
		};
		targetAudio.onplay = () => {
			buttonPlay.innerText = "pause";
		};
		targetAudio.onplaying = () => {
			buttonPlay.innerText = "pause";
		};
		targetAudio.ontimeupdate = () => {
			sliderTime.value = (targetAudio.currentTime * 100) / targetAudio.duration;
			// timeElapsed.innerHTML = toTime(p.currentTime);
		};

		let input = player.querySelector("input[type='file']");
		console.log(input);
		input.addEventListener("change", () => {
			console.log("file changed");
			if (input.files && input.files[0]) {
				let reader = new FileReader();
				reader.onload = (e) => {
					targetAudio.setAttribute("src", e.target.result);
					targetAudio.load();
				};
				reader.readAsDataURL(input.files[0]);
			}
		});
	});
}

function preventSubmit(e) {
	e.preventDefault();
	return false;
}

function resetField(elm) {
	console.log("onreset");
	let field = document.querySelector(`[field="${elm.getAttribute("target-field")}"]`);
	field.setAttribute("src", field.getAttribute("origin-src"));
}

function resetTForm(form) {
	let elm = form.querySelectorAll(`[origin-src]`);
	elm.forEach((element) => {
		element.setAttribute("src", element.getAttribute("origin-src"));
	});
	let telm = form.querySelectorAll(`[origin-val]`);
	telm.forEach((element) => {
		element.value = element.getAttribute("origin-val");
	});
}

function XHR(method, url, callback, data = null, contentType = null) {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (this.readyState == 4) {
			if (callback) callback(this.status, xhr.responseText);
			console.log("XHR status: " + this.status);
			// if (this.status == 200) {
			// 	callback(xhr.responseText);
			// }
		}
	};
	xhr.open(method, url);
	if (contentType != null) {
		xhr.setRequestHeader("content-type", contentType);
	}
	xhr.send(data);
}
var onDrag;

function trackOnDragStart(ev) {
	let hoverElm = this.querySelector(":hover");
	if (hoverElm.classList.contains("handle")) {
		this.style.opacity = 0.4;
		onDrag = this;
		// ev.dataTransfer.effectAllowed = "move";
		// ev.dataTransfer.setData("text/html", this.innerHTML);
	} else {
		ev.preventDefault();
		//return false;
	}
}

function trackOnDragEnd(ev) {
	this.style.opacity = 1;
	onDrag = undefined;
}

function trackOnDragOver(ev) {
	if (this != onDrag) {
		let my = event.clientY;
		let bound = this.getBoundingClientRect();
		let classList = this.classList;
		if (my <= bound.y + bound.height / 2) {
			if (!classList.contains("drag-over-top")) {
				classList.toggle("drag-over-top", true);
				classList.toggle("drag-over-bottom", false);
			}
		} else {
			if (!classList.contains("drag-over-bottom")) {
				classList.toggle("drag-over-bottom", true);
				classList.toggle("drag-over-top", false);
			}
		}
	}
	ev.preventDefault();
	ev.stopPropagation();
	return false;
}

function trackOnDragLeave(ev) {
	this.classList.toggle("drag-over-top", false);
	this.classList.toggle("drag-over-bottom", false);
}

function trackOnDrop(ev) {
	console.log("dropped");
	if (this != onDrag) {
		let my = event.clientY;
		let bound = this.getBoundingClientRect();
		let parent = this.parentNode;

		if (my <= bound.y + bound.height / 2) {
			// if(parent.hasChild(th))
			parent.insertBefore(onDrag, this);
		} else {
			if (this.nextSibling) {
				parent.insertBefore(onDrag, this.nextSibling);
			} else {
				parent.appendChild(onDrag);
			}
		}
	}
	trackRefresh();
	this.classList.toggle("drag-over-top", false);
	this.classList.toggle("drag-over-bottom", false);
}

function trackRefresh() {
	let elmList = document.querySelectorAll("[track-id]");
	for (let i = 0; i < elmList.length; i++) {
		elmList[i].childNodes[1].innerText = i + 1;
	}
}

function trackReset() {
	let elmList = document.querySelectorAll("[track-id]");
	for (let i = 0; i < elmList.length; i++) {
		for (let j = 0; j < elmList.length; j++) {
			if (elmList[j].getAttribute("track-num") == i + 1) {
				elmList[j].parentNode.appendChild(elmList[j]);
				break;
			}
		}
	}
	trackRefresh();
}

function updateTracknum() {
	let elmList = document.querySelectorAll("[track-id]");
	let updatedTracks = 0;
	elmList.forEach((elm) => {
		let newP = parseInt(elm.childNodes[1].innerText);
		if (newP != elm.getAttribute("track-num")) {
			XHR(
				"PUT",
				`/tracks/${elm.getAttribute("track-id")}`,
				(statusCode) => {
					if (statusCode == 200) {
						updatedTracks++;
						if (updatedTracks == elmList.length) {
							console.log("UPDATED TRACKNUM");
						}
					}
				},
				JSON.stringify({ track_num: newP }),
				"application/json"
			);
		}
	});
}

function addTrack(form) {
	form.querySelector("[name='track_num']").value = document.querySelectorAll("[track-id]").length + 1;
	console.log(form.querySelector("[name='track_num']"));
}

function uploadTrack() {
	event.preventDefault();
	event.stopPropagation();
	const form = document.getElementById("dialog-add");
	let formData = new FormData(form);
	/*for (let entry of formData.entries()) {
		let elm = form.querySelector(`[name="${entry[0]}"]`);
		console.log(elm);
		if (elm.type == "file") {
			
			formData.append("file", elm.files[0]);
		}
	}*/
	XHR(
		"POST",
		"/tracks",
		(status) => {
			if (status == 200) {
				toggleDialog("dialog-add", resetTForm);
				///toggleDialog("dialog-status-ok");
				setTimeout(()=>window.location.reload(),100);
			} else {
				toggleDialog("dialog-status-err");
			}
		},
		formData
	);
}

function initTrackDragNDrop() {
	console.log("Init track drag n drop");
	let elmList = document.querySelectorAll("[track-id]");
	elmList.forEach(function (elm) {
		elm.addEventListener("dragstart", trackOnDragStart);
		elm.addEventListener("dragend", trackOnDragEnd);
		elm.addEventListener("dragover", trackOnDragOver);
		elm.addEventListener("dragenter", trackOnDragOver);
		elm.addEventListener("dragleave", trackOnDragLeave);
		elm.addEventListener("drop", trackOnDrop);
	});
}

function deleteTrack(ev, id) {
	console.log(ev.target);
	let f = function () {
		console.log("callingback");
		XHR("DELETE", `/tracks/${id}`, () => {});
		setTimeout(()=>window.location.reload(), 100);
	};
	toggleDeleteDialog(f);
	event.preventDefault();
	event.stopPropagation();
	return false;
}

function toggleDeleteDialog(fcallback = null) {
	console.log(fcallback);
	let dialog = document.getElementById("dialog-del-confirm");
	dialog.classList.toggle("hidden");
	if (fcallback) {
		let okBt = dialog.querySelector(".dialog-button-ok");
		okBt.innerHTML = "Ok";
		okBt.addEventListener("click", fcallback);
	}
}

function resetDeleteDialog(dialog) {
	console.log("reset");
	let ok = dialog.querySelector(".dialog-button-ok");
	ok.outetHTML = ok.outetHTML;
}

var currentTrack;

function togglePopup(ev) {
	let popup = document.querySelector(".popup");
	let bound = ev.target.getBoundingClientRect();

	if (popup.classList.contains("hidden")) {
		popup.style.left = `${bound.right}px`;
		popup.style.top = `${bound.top}px`;
		popup.classList.remove("hidden");
	} else {
		popup.classList.add("hidden");
	}

	console.log(popup.style.top);
	event.preventDefault();
	event.stopPropagation();
}

function addPlaylistTrack() {
	let f = function(status, raw) {
		if (status == 200) {
			let pl = JSON.parse(raw);
			insertPopupLibrary(pl);
			addTrackToPL(pl.id);
			document.querySelector(".popup").classList.add("hidden");
			toggleDialog("dialog-add");
			toggleDialog("dialog-status-ok");
		}
	}
	console.log(f);
	addPlaylist(f);
}

function addPlaylist(fcallback) {
	let dialog = document.getElementById("dialog-add");
	console.log(fcallback);
	XHR("POST", dialog.getAttribute("action"), fcallback, JSON.stringify({ title: dialog.querySelector("input[name='title']").value }), "application/json");
}

function addNewPlaylist()
{
	let f = function(status, raw)
	{
		if(status==200)
		{
			toggleDialog("dialog-add");
			setTimeout(()=> window.location.reload(), 200);
		}
	}
	addPlaylist(f);
}
function insertPopupLibrary(pl) {
	let popup = document.querySelector(".popup");
	let newUl = document.createElement("li");
	newUl.setAttribute("onclick", `addTrackToPL(${pl.id})`);
	newUl.innerText = pl.title;
	let uls = popup.querySelectorAll("li")
	popup.insertBefore(newUl, uls[uls.length-1]);
}

function addTrackToPL(plId) {
	console.log(plId);
	let dialog = document.getElementById("dialog-add");
	XHR("PUT", `${dialog.getAttribute("action")}/${plId}/tracks/${currentTrack}`, null, JSON.stringify({}), "application/json");
	let popup = document.querySelector(".popup");
	popup.classList.add("hidden");
}

function deletePlaylist() {
	XHR("DELETE", `${window.location.pathname}`, ()=>{
		changePage("/library");
	}, JSON.stringify({}), "application/json");
}

function changePassword(uid)
{
	let f = function(status, data)
	{
		if(status == 200)
		{
			console.log(data);
		}
	}
	XHR("GET",`/users/${uid}`, f, null, "application/json");

}