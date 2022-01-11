const fs = require("fs");
const ejs = require("ejs");
const url = require("url");

exports.compiled = ejs.compile(
  fs.readFileSync("./wwwroot/frame.html", "UTF-8")
);

exports.compiledMgr = ejs.compile(
  fs.readFileSync("./wwwroot/management/frame.html", "UTF-8")
);

exports.raw = (req) => {
    console.log(url.parse(req.url).query);
};
