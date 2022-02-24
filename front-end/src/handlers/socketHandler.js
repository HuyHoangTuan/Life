const api = require("../api");
const utils = require("./utils");
/*
{
    type: code,
    data: 
}
*/
const typ_clt_subs_admin = 9;
const typ_clt_subs = 10;
const typ_clt_wrt_cmt = 11;
const typ_clt_post_cmt = 12;
const typ_clt_edit_cmt = 13;
const typ_clt_del_cmt = 14;

const typ_srv_typing = 15;
const typ_srv_new_cmt = 16;
const typ_srv_edt_cmt = 17;
const typ_srv_del_cmt = 18;
const typ_srv_upd_usr = 19;

var channelMap = new Map();
var socketMap = new Map();
var userMap = new Map();

exports.UserMap = userMap;

exports.handler = (ws, req) => {
    console.log("[Log][Socket]New socket connection");
    if(!userMap.has(req.uid)){
        userMap.set(req.uid, true);
    }
    ws.on("message", async function (mesgRaw) {
        console.log(mesgRaw);
        let mesg = JSON.parse(mesgRaw);
        switch (mesg.type) {
            case typ_clt_subs: {
                let channel = mesg.data.channel;
                

                //clear previous mapped sockets
                if (socketMap.has(this)) {
                    let lastChannel = socketMap.get(this);
                    socketMap.get(lastChannel).splice(this);
                } else {
                    socketMap.set(this, channel);
                }
                //ppisbig
                if (channelMap.has(channel)) {
                    console.log("[Log][Socket]Join channel " + channel);

                    channelMap.get(channel).push(this)
                } else {
                    console.log("[Log][Socket]Created new channel " + channel);
                    let newMap = [];
                    newMap.push(this);
                    channelMap.set(channel, newMap);
                }
            }
                break;
            case typ_clt_wrt_cmt: {

            }
                break;
            case typ_clt_post_cmt: {
                let rawRes = await api.doPost(`/comments?uid=${req.uid}&${mesg.data.type}=${mesg.data.id}`, { token: req.token }, { content: mesg.data.content });
                let res = JSON.parse(rawRes);
                if (res.id) {
                    if (socketMap.has(this)) {
                        let channel = socketMap.get(this);
                        let sockets = channelMap.get(channel);
                        if (sockets) {
                            sockets.forEach((socket) => {
                                socket.send(JSON.stringify(
                                    { type: typ_srv_new_cmt, data: res }
                                ))
                            });
                        }
                    }
                }
            }
                break;
            case typ_clt_edit_cmt: {
                let rawRes = await api.doPut(`/comments/${mesg.data.id}`, { token: req.token }, { content: mesg.data.content });
                let res = JSON.parse(rawRes);
                if (res.status == "success") {
                    if (socketMap.has(this)) {
                        let channel = socketMap.get(this);
                        let sockets = channelMap.get(channel);
                        if (sockets) {
                            sockets.forEach((socket) => {
                                socket.send(JSON.stringify(
                                    { type: typ_srv_edt_cmt, data: { id: mesg.data.id, content: mesg.data.content } }
                                ))
                            });
                        }
                    }
                }
            }
                break;
            case typ_clt_del_cmt: {
                let rawRes = await api.doDelete(`/comments/${mesg.data.id}`, { token: req.token });
                let res = JSON.parse(rawRes);
                if (res.status == "success") {
                    if (socketMap.has(this)) {
                        let channel = socketMap.get(this);
                        let sockets = channelMap.get(channel);
                        if (sockets) {
                            sockets.forEach((socket) => {
                                socket.send(JSON.stringify(
                                    { type: typ_srv_del_cmt, data: { id: mesg.data.id } }
                                ))
                            });
                        }
                    }
                }
            }
                break;
        }
    });
    ws.on("close", function() {
        console.log("Closed socket");
        let channel = socketMap.get(this);
        if(channelMap.has(channel)){
            channelMap.get(channel).splice(this);
        }
        socketMap.delete(this);
        userMap.delete(req.uid);
    })
};
exports.CommentHandler = class {
    static async postHandler(req, res){
        let content = await utils.getBody(req);
        let rawRes = await api.doPost(`/comments?uid=${req.uid}&${req.params.entity == "albums" ? "album" : "playlist"}=${req.params.id}`, { token: req.token }, content);
        let result = JSON.parse(rawRes);
        if (result.id) {
            let channel = `/${req.params.entity}/${req.params.id}`;
            let sockets = channelMap.get(channel);
            console.log("POST"  + channel)
            if (sockets) {
                sockets.forEach((socket) => {
                    socket.send(JSON.stringify(
                        { type: typ_srv_new_cmt, data: result }
                    ))
                });
            }
        }
        res.statusCode = 200;
		res.setHeader("Content-Type", "application/json");
		res.write(rawRes);
		res.end();
    }
}