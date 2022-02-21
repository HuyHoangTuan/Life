function showDetailDialog(elm) {
	let attrs = elm.attributes;
	for (var i = attrs.length - 1; i >= 0; i--) {
		let field = document.querySelector(`[field='${attrs[i].name}']`);
		if (field != undefined) {
			if (field.tagName == "IMG") {
				field.setAttribute("src", attrs[i].value);
			}
			if (field.tagName == "audio") {
				field.setAttribute("src", `/tracks/${attrs[i].value}/audio`);
			} else {
				field.setAttribute("value", attrs[i].value);
				field.value = attrs[i].value;
			}
		}
	}
	document.getElementById("dialog-detail").classList.toggle("hidden");
}

function toggleDialog(dialogID, callback = null) {
	event.preventDefault();
	event.stopPropagation();
	let dialog = document.getElementById(dialogID);
	dialog.classList.toggle("hidden");
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

function loadLocalAudio(srcElm) {
	let targetField = srcElm.getAttribute("target-field");
	let targetAudio = document.querySelector(`[field=${targetField}]`);
	if (srcElm.files && srcElm.files[0]) {
		let reader = new FileReader();
		reader.onload = (e) => {
			if (targetAudio.getAttribute("src") != "") {
				targetAudio.setAttribute("src", e.target.result);
				targetAudio.load();
				return;
			}
			targetAudio.setAttribute("src", e.target.result);
			targetAudio.oncanplay = () => {
				sliderTime.value = 0;
				buttonPlay.innerText = "play_arrow";
			};
			let controller = document.querySelector(".controller");
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
			targetAudio.onpause = () => {
				buttonPlay.innerText = "play_arrow";
			};
			targetAudio.onplay = () => {
				buttonPlay.innerText = "pause";
			};
			targetAudio.ontimeupdate = () => {
				sliderTime.value =
					(targetAudio.currentTime * 100) / targetAudio.duration;
				// timeElapsed.innerHTML = toTime(p.currentTime);
			};
			targetAudio.load();
		};
		reader.readAsDataURL(srcElm.files[0]);
	}
}

function preventSubmit(e) {
	e.preventDefault();
	return false;
}

function resetField(elm) {
	console.log("onreset");
	let field = document.querySelector(
		`[field="${elm.getAttribute("target-field")}"]`
	);
	field.setAttribute("src", field.getAttribute("origin-src"));
}

function resetTForm(form) {
	console.log("FUCKOOGG");
	let elm = form.querySelectorAll(`[origin-src]`);
	console.log(elm);
	elm.forEach((element) => {
		element.setAttribute("src", element.getAttribute("origin-src"));
	});
}

function XHR(method, url, callback, data = null, contentType = null) {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (this.readyState == 4) {
			callback(this.status, xhr.responseText);
			console.log("XHR status: " + this.status);
			// if (this.status == 200) {
			// 	callback(xhr.responseText);
			// }
		}
	};
	xhr.open(method, url);
	if (contentType != null) {
		console.log("DDDDD");
		xhr.setRequestHeader("content-type", contentType);
	}
	xhr.send(data);
}

var onDrag;

function trackOnDragStart(elm) {
	elm.style.opacity = 0.4;
	onDrag = elm;
}

function trackOnDragEnd(elm) {
	elm.style.opacity = 1;
}

function trackOnDragOver(elm) {
	if (elm != onDrag) {
		elm.style.background = "red";
	}
}

function trackOnDragLeave(elm) {
	
}
