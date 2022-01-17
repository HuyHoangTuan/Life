const ejs = require("ejs");
const url = require("url");
const qs = require("querystring");
const frame = require("./frame");

exports.getPath = (path, pos = 1) => {
	return path.split("?")[0].split("/")[pos];
};

// exports.parseHandler = (httpUrl) => {
// 	return url.parse(httpUrl).pathname.split("/")[1];
// };

// exports.parseId = (httpUrl) => {
// 	console.log("URL" + httpUrl)
// 	return url.parse(httpUrl).pathname.split("/")[2];
// };

// exports.parseMgr = (httpUrl) => {
// 	return url.parse(httpUrl).pathname.split("/")[2];
// };

// exports.parseMrgId = (httpUrl) => {
// 	return url.parse(httpUrl).pathname.split("/")[3];
// };

exports.getURLQuery = (path, name) => {
	let query = path.split("?")[1];
	if (query != undefined) {
		query = new URLSearchParams(query);
		return query.get(name);
	}
	return null;
};

exports.redirect = (res, location) => {
	res.writeHead(301, { Location: location });
	res.end();
};

exports.renderPage = (res, filePath, data, frameType = 0, options = null) => {
	ejs.renderFile("src/wwwroot/" + filePath, data, options, (err, html) => {
		if (err) console.log(`[Error][Render]${err}`);
		switch (frameType) {
			case 0:
				break;
			case 1:
				html = frame.compiled({ content: html });
				break;
			case 2:
				html = frame.compiledMan({ content: html });
				break;
			case 3:
				html = JSON.stringify({title: data.title, content: html})
				break;
		}
		res.statusCode = 200;
		res.setHeader("Content-Type", "text/html");
		res.write(html);
		res.end();
	});
};

exports.renderError = (res) => {};
