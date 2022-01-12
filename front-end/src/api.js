const http = require("./http");

const apiHost = "http://192.168.192.40:8080";
const apiRoot = "/api";

function buildURL(path, param) {
	url = new URL(`${apiHost}${apiRoot}${path}`);
	for (const [key, value] of Object.entries(param)) {
		url.search += `${key}=${value}`;
	}
	return url
}

async function doGet(path, param) {
	return await http.get(buildURL(path, param))
}

async function doPost(path, param, data) {
	return await http.post(buildURL(path, param), data)
}


async function doPut(path, param, data) {
	return await http.put(buildURL(path, param), data)
}


async function doDelete(path, param) {
	return await http.delete(buildURL(path, param))
}

exports.doGet = doGet
exports.doPost = doPost
exports.doPut = doPut
exports.doDelete = doDelete