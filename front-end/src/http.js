const http = require("http");

function logging(req) {
	req.on("error", (error) => {
		console.error(`[Error][http]${error}`);
	});
}

exports.makeReq = (url, options = {}, fwdReq, fwdRes) => {
	let req = http.request(url, options, (res) => {
		fwdRes.writeHead(res.statusCode, res.statusMessage, res.headers);
		res.on("data", (data) => {
			fwdRes.write(data);
		});
		res.on("end", () => {
			fwdRes.end();
		});
	});
	
	fwdReq.on("data", (data) => {
		// process.stdout.write(data);
		req.write(data);
	});
	fwdReq.on("end", () => {
		req.end();
	});
	logging(req);
};

exports.get = (url) => {
	return new Promise((resolve) => {
		let req = http.request(url, { method: "GET" }, (res) => {
			let resBody = "";
			res.on("data", (data) => {
				resBody += data;
				// process.stdout.write(data);
			});
			res.on("end", () => {
				resolve(resBody);
			});
		});
		logging(req);
		req.end();
	});

	// let req = http.request(url, { method: "GET" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req)
	// req.end();
};

exports.post = (url, data, options = null) => {
	if (options == null) {
		options = { method: "POST" };
	} else {
		options.method = "POST";
	}
	return new Promise((resolve) => {
		let req = http.request(url, options, (res) => {
			let resBody = "";
			res.on("data", (data) => {
				resBody += data;
				// process.stdout.write(data);
			});
			res.on("end", () => {
				resolve(resBody);
			});
		});
		logging(req);
		req.write(data);
		req.end();
	});
};

exports.put = (url, data, options = null) => {
	if (options == null) {
		options = { method: "PUT" };
	} else {
		options.method = "PUT";
	}
	return new Promise((resolve) => {
		let req = http.request(url, options, (res) => {
			let resBody = "";
			res.on("data", (data) => {
				resBody += data;
				// process.stdout.write(data);
			});
			res.on("end", () => {
				resolve(resBody);
			});
		});
		logging(req);
		req.write(data);
		req.end();
	});
};

exports.delete = (url) => {
	return new Promise((resolve) => {
		let req = http.request(url, { method: "DELETE" }, (res) => {
			res.on("data", (data) => {
				resolve(data);
			});
		});
		logging(req);
		req.end();
	});
	// let req = http.request(url, { method: "DELETE" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req);
	// req.end();
};

/* 

const http = require("http");
const url = require("url");

function logging(req) {
	req.on("error", (error) => {
		console.error(`[Log][http]${error.stack}`);
	});
}

function convertURLtoOptions(url, method) {
	return {
		hostname: url.hostname,
		port: url.port,
		path: `${url.path}?${url.search}`,
		method: method,
	};
}

exports.get = (url) => {
	return new Promise((resolve) => {
		let req = http.request(convertURLtoOptions(url, "GET"), (res) => {
			res.on("data", (data) => {
				resolve(data);
			});
		});
		logging(req);
		req.end();
	});

	// let req = http.request(url, { method: "GET" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req)
	// req.end();
};

exports.post = (url, data) => {
	return new Promise((resolve) => {
		let req = http.request(convertURLtoOptions(url, "POST"), (res) => {
			res.on("data", (data) => {
				resolve(data);
			});
		});
		logging(req);
		req.write(data);
		req.end();
	});
	// http.request(url, { method: "POST" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req);
	// req.write(data);
	// req.end();
};

exports.put = (url, data) => {
	return new Promise((resolve) => {
		let req = http.request(convertURLtoOptions(url, "PUT"), (res) => {
			res.on("data", (data) => {
				resolve(data);
			});
		});
		logging(req);
		req.write(data);
		req.end();
	});
	// http.request(url, { method: "PUT" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req);
	// req.write(data);
	// req.end();
};

exports.delete = (url) => {
	return new Promise((resolve) => {
		let req = http.request(convertURLtoOptions(url, "DELETE"), (res) => {
			res.on("data", (data) => {
				resolve(data);
			});
		});
		logging(req);
		req.end();
	});
	// let req = http.request(url, { method: "DELETE" }, (res) => {
	// 	res.on("data", (data) => callback(data));
	// });
	// logging(req);
	// req.end();
};


*/
