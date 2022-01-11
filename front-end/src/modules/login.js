const qs = require("querystring");
const http = require("http");
const cookie = require("cookie");
const utils = require("./utils");
const args = require("./args");
const session = require("./session.js");

function login(email, password) {
  var data = { email: email, password: password };
  var option = args.generateOptionPOSTLogin(data);
  var req = http.request(option, (res) => {
    console.log(`statusCode: ${res.statusCode}`);
    res.on("data", (d) => {
      process.stdout.write(d);
    });
  });
  req.write(JSON.stringify(data));
  req.end();
}

class LoginHandler {
  static getHandler(req, res, status = 0) {
    var data = { status: status };
    utils.renderPage(res, "login.html", data);
  }
  static postHandler(req, res, body) {
    var post = qs.parse(body);
    if (post.username == "admin" && post.password == "admin") {
      utils.redirect(res, "/management/user");
    } else {
      res.setHeader(
        "Set-Cookie",
        cookie.serialize("id", post.username, {
          httpOnly: true,
          maxAge: 60 * 60 * 24 * 30, // 1 month
        })
      );
    }
    console.log(
      `[Log][Login]Login attempt with email=${post.username}, password=${post.password}`
    );
    utils.redirect(res, "/library");
    //login(post.username, post.password);
  }
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = LoginHandler;
