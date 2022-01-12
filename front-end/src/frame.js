const fs = require("fs");
const ejs = require("ejs");
const url = require("url");

exports.compiled = ejs.compile(
	fs.readFileSync("src/wwwroot/frame.html", "UTF-8")
);

exports.compiledMgr = ejs.compile(
	fs.readFileSync("src/wwwroot/frame.html", "UTF-8")
);

exports.raw = (req) => {
	console.log(url.parse(req.url).query);
};
