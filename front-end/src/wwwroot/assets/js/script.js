function showDetailDialog(elm) {
	let attrs = elm.attributes;
	for (var i = attrs.length - 1; i >= 0; i--) {
		let field = document.querySelector(`[field='${attrs[i].name}']`);
		if (field != undefined) {
			if (field.tagName == "IMG") {
				field.setAttribute("src", attrs[i].value);
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

function resetField(elm) {
	console.log("onreset");
	let field = document.querySelector(
		`[field="${elm.getAttribute("target-field")}"]`
	);
	field.setAttribute("src", field.getAttribute("origin-src"));
}

function XHR(method, url, callback, data = null, contentType = null) {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function () {
		if (this.readyState == 4) {
			callback(this.status, xhr.responseText)
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
