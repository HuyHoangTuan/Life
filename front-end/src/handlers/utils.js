const cookie = require("cookie");
const qs = require("querystring");

function getMode(req) {
	// return utils.getURLQuery(req.url, "raw") == "true" ? true : false;
	return req.query.raw != undefined;
}

function getCookie(key) {
	var cookies = cookie.parse(req.headers.cookie || "");
	return cookies[key];
}

function getToken() {
	return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjQyMzYwODU3fQ.MEfen90dvOGM4zuzNm-BLJTdQgC-ZCo_1dMqcTzboYw";
}

exports.getBody = async function(req) {
	return new Promise((resolve) => {
		let body = "";
		req.on("data", (data) => {
			body += data;
			// Too much data, kill the connection!
			if (body.length > 1e6) request.connection.destroy();
		});
		req.on("end", (data) => {
			resolve(body);
		});
	});
}