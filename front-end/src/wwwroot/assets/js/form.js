// function buildParameter(form, type) {
// 	let formData = new FormData(form);
// 	let jsonObject = {};
// 	let failed = 0;
// 	for (let entry of formData.entries()) {
// 		jsonObject[entry[0]] = entry[1].trim();
// 		if ((type == 1 && entry[1] != old[entry[0]]) || type == 0) {
// 			let elmRow = document.getElementById(`${entry[0]}-row`);
// 			if (elmRow.getAttribute("func") == "") continue;
// 			let func = window[elmRow.getAttribute("func")];
// 			console.log(
// 				"FUNC: " +
// 					elmRow.getAttribute("func") +
// 					" : " +
// 					`${entry[0]}-row`
// 			);
// 			let checkResult = func(entry[1].trim());
// 			if (checkResult != 0) {
// 				failed = 1;
// 				elmRow.className = "invalid";
// 				hideErrors(elmRow);
// 				console.log("RES: " + `error-${entry[0]}-${checkResult}`);
// 				document.getElementById(
// 					`error-${entry[0]}-${checkResult}`
// 				).className = "error";
// 			} else {
// 				elmRow.className = "";
// 				hideErrors(elmRow);
// 			}
// 		}
// 	}
// 	return failed == 1 ? 0 : jsonObject;
// }

function submitForm(id, goBack = 1) {
	event.preventDefault();
	event.stopPropagation();
	const form = document.getElementById(id);
	let formData = new FormData(form);
	let jsonData = new Object();
	let files = [];
	let hasFieldUpdated = 0;
	// check for changed fields
	for (let entry of formData.entries()) {
		let elm = form.querySelector(`[name="${entry[0]}"]`);
		if (elm.value != elm.getAttribute("value")) {
			if (elm.type != "file") {
				console.log(entry[0] + " changed");
				jsonData[`${entry[0]}`] = entry[1].trim();
				hasFieldUpdated = 1;
			} else {
				files.push(elm);
			}
		}
	}
	console.log(JSON.stringify(jsonData));

	let showDialog = (statusCode) => {
		if (statusCode == 200) {
			let path = window.location.pathname;
			if(goBack != 0){
				path = path.substring(0, path.lastIndexOf("/"));
				// window.history.state.
			}
			changePage(path);
			toggleDialog("dialog-status-ok");
		} else {
			toggleDialog("dialog-status-err");
		}
	};

	let uploadFiles = () => {
		files.forEach((file) => {
			let fileForm = new FormData();
			fileForm.append("file", file.files[0]);
			console.log("upload file");
			let callback = (statusCode) => {};
			if (file == files[files.length - 1]) {
				callback = showDialog;
			}
			XHR("POST", file.getAttribute("target-action"), callback, fileForm);
		});
	};

	if (hasFieldUpdated) {
		XHR(
			form.getAttribute("method"),
			form.getAttribute("action"),
			(statusCode) => {
				if (statusCode == 200 && files.length > 0) {
					uploadFiles();
				} else {
					showDialog(statusCode);
				}
			},
			JSON.stringify(jsonData)
		);
	} else if (files.length > 0) {
		console.log("upload files only")
		uploadFiles();
	}
}

function resetForm(id) {
	event.preventDefault();
	event.stopPropagation();
	const form = document.getElementById(id);
	form.reset();
}


function submitFormRaw(id, goBack = 1) {
	event.preventDefault();
	event.stopPropagation();
	const form = document.getElementById(id);
	let formData = new FormData(form);
	let jsonData = new Object();
	let files = [];
	let hasFieldUpdated = 0;
	// check for changed fields
	for (let entry of formData.entries()) {
		let elm = form.querySelector(`[name="${entry[0]}"]`);
		if (elm.value != elm.getAttribute("value")) {
			if (elm.type != "file") {
				console.log(entry[0] + " changed");
				jsonData[`${entry[0]}`] = entry[1].trim();
				hasFieldUpdated = 1;
			} else {
				files.push(elm);
			}
		}
	}
	console.log(JSON.stringify(jsonData));

	let showDialog = (statusCode) => {
		if (statusCode == 200) {
			let path = window.location.pathname;
			if(goBack != 0){
				path = path.substring(0, path.lastIndexOf("/"));
			}
			changePage(path);
			toggleDialog("dialog-status-ok");
		} else {
			toggleDialog("dialog-status-err");
		}
	};

	let uploadFiles = () => {
		files.forEach((file) => {
			let fileForm = new FormData();
			fileForm.append("file", file.files[0]);
			console.log("upload file");
			let callback = (statusCode) => {};
			if (file == files[files.length - 1]) {
				callback = showDialog;
			}
			XHR("POST", file.getAttribute("target-action"), callback, fileForm);
		});
	};

	if (hasFieldUpdated) {
		XHR(
			form.getAttribute("method"),
			form.getAttribute("action"),
			(statusCode) => {
				if (statusCode == 200 && files.length > 0) {
					uploadFiles();
				} else {
					showDialog(statusCode);
				}
			},
			JSON.stringify(jsonData)
		);
	} else if (files.length > 0) {
		console.log("upload files only")
		uploadFiles();
	}
}