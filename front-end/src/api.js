const http = require("./http");

const apiHost = "http://127.0.0.1:8080";
const apiRoot = "/api";

function buildURL(path, param) {
	url = new URL(`${apiHost}${apiRoot}${path}`);
	if (param != null) {
		for (const [key, value] of Object.entries(param)) {
			url.searchParams.append(key, value);
		}
	}
	return url;
}

exports.forward = (req, res, path, param) => {
	console.log(`[Log][API][${req.method}]Redirect to ` + path);
	http.makeReq(
		buildURL(path, param),
		{
			method: req.method,
			headers: req.headers,
		},
		req,
		res
	);
};

async function doGet(path, param) {
	// console.log(`[Log][GET]${buildURL(path, param).toString()}`);
	let res = await http.get(buildURL(path, param));
	console.log("GET " + res);
	return res;
}

async function doPost(path, param, data) {
	console.log(`[Log][POST]${data}`);
	let res = await http.post(buildURL(path, param), data, {
		headers: {
			"Content-Type": "application/json",
			// "Content-Length": data.length,
		},
	});
	return res;
}

async function doPut(path, param, data) {
	return await http.put(buildURL(path, param), data, {
		headers: {
			"Content-Type": "application/json",
			// "Content-Length": JSON.stringify(data).length,
		},
	});
}

async function doDelete(path, param) {
	return await http.delete(buildURL(path, param));
}

exports.doGet = doGet;
exports.doPost = doPost;
exports.doPut = doPut;
exports.doDelete = doDelete;
