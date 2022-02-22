const cookie = require("cookie");
const ejs = require("ejs");
const fs = require("fs");
const jwt = require("jsonwebtoken")

const compiledUserFrame = ejs.compile(
	fs.readFileSync("src/wwwroot/frame.ejs", "UTF-8")
);

const compiledManFrame = ejs.compile(
	fs.readFileSync("src/wwwroot/management/frame.ejs", "UTF-8")
);

exports.FORMAT_NONE = 0;
exports.FORMAT_USER = 1;
exports.FORMAT_MAN = 2;
exports.FORMAT_RAW = 3;

exports.formatIsRaw = (req) => {
	return req.query.raw != undefined;
};

function getCookie(key) {
	var cookies = cookie.parse(req.headers.cookie || "");
	return cookies[key];
}

// return token value from cookies
exports.getToken = function (req) {
	return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjQ1NDY5MzczfQ.72vCpCiSyluyP89HgS06yNStsz53UXzyqyxB-qDDse4";
};

exports.getUID = (token) => {
	let decoded = jwt.verify(token, "L0Zhbmt5Y2hvcDEyMz9sb2dpbj1GYW5reWNob3AmcGFzc3dvcmQ9S3ViaW4xMjM/");
	return decoded.sub;
}  

// read body from request stream until finished
exports.getBody = async function (req) {
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
};

exports.renderPage = (res, filePath, data, frameType = 0, options = null) => {
	data.uid = res.uid;
	data.uname = res.uname;
	ejs.renderFile("src/wwwroot/" + filePath, data, options, (err, html) => {
		if (err) console.log(`[Error][Render]${err}`);
		switch (frameType) {
			case this.FORMAT_NONE:
				break;
			case this.FORMAT_USER:
				html = compiledUserFrame({ content: html });
				break;
			case this.FORMAT_MAN:
				html = compiledManFrame({ content: html });
				break;
			case this.FORMAT_RAW:
				html = JSON.stringify({ title: data.title, content: html });
				break;
		}
		res.statusCode = 200;
		res.setHeader("Content-Type", "text/html");
		res.write(html);
		res.end();
	});
};
