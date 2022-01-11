const ejs = require("ejs");
const url = require("url");
const qs = require("querystring");

exports.parseHandler = (httpUrl) => {
  return url.parse(httpUrl).pathname.split("/")[1];
};

exports.parseId = (httpUrl) => {
  return url.parse(httpUrl).pathname.split("/")[2];
};

exports.parseMgr = (httpUrl) => {
  return url.parse(httpUrl).pathname.split("/")[2];
};

exports.parseMrgId = (httpUrl) => {
  return url.parse(httpUrl).pathname.split("/")[3];
};

exports.getURLQuery = (req) => {
  return qs.parse(url.parse(req.url).query);
};

exports.redirect = (res, location) => {
  res.writeHead(301, { Location: location });
  res.end();
}

exports.renderPage = (res, filePath, data, frame = null, options = null) => {
  ejs.renderFile("./wwwroot/" + filePath, data, options, (err, html) => {
    if (err) console.log(`[Error][Render]${err}`);
    if (frame != null) {
      html = frame({ content: html });
    }
    res.statusCode = 200;
      res.setHeader("Content-Type", "text/html");
      res.write(html);
      res.end();
  });
};

exports.renderError = (res) => {};
