const hostname = "192.168.192.50";
const port = 8080;

function genOpt(option, data) {
  option.hostname = hostname;
  option.port = port;
  option.headers = {
    "Content-Type": "application/json",
    "Content-Length": JSON.stringify(data).length,
  };
  return option;
}

exports.generateOptionPOSTLogin = (data) => {
  return genOpt({ path: "/api/authenticate", method: "POST" }, data);
};

exports.generateOptionMgrGETUserList = (data) => {
  return genOpt({ path: "/api/account", method: "POST" }, data);
};


