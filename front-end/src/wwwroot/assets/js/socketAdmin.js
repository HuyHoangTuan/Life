var socket;

const typ_clt_subs_admin = 9;

const typ_srv_upd_usr = 19;


var curChannel;
var opened = false;

function createSocket() {
	socket = new WebSocket(`ws://${window.location.host}/admin`);
	socket.onopen = () => {
		console.log("Open socket connection");
		opened = true;
		if (curChannel) {
			subscribe(curChannel);
		}
	};
	socket.onmessage = (rawMesg) => {
		let mesg = JSON.parse(rawMesg.data);
		switch (mesg.type) {
			case typ_srv_new_cmt:
				{
					console.log(mesg.data);
					insertComment(mesg.data);
				}
				break;
			case typ_srv_edt_cmt:
				{
				}
				break;
			case typ_srv_del_cmt:
				{
				}
				break;
		}
	};
	socket.onclose = () => { };
	socket.onerror = () => {
		console.log("socket error");
		socket.close();
		createSocket();
	};
}

function updateOnlineUser(){
    
}