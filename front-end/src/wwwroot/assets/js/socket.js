var socket;

const typ_clt_subs = 10;
const typ_clt_wrt_cmt = 11;
const typ_clt_post_cmt = 12;
const typ_clt_edit_cmt = 13;
const typ_clt_del_cmt = 14;

const typ_srv_typing = 15;
const typ_srv_new_cmt = 16;
const typ_srv_edt_cmt = 17;
const typ_srv_del_cmt = 18;

var curChannel;
var opened = false;

function createSocket() {
	socket = new WebSocket(`ws://${window.location.host}/ws`);
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

function insertComment(comment) {
	if (document.querySelector(`[comment-id="${comment.id}"]`)){
		return;
	} 

	let commentList = document.getElementById("comment-list");
	let newCmt = document.createElement("div");
	newCmt.className = "comment";
	newCmt.setAttribute("comment-id", comment.id);
	newCmt.innerHTML = `<img class="avatar" src="/users/${comment.creator_id}/avatar">
    <div>
        <b>${comment.display_name}</b>
        <div>${comment.content}</div>
        <h4><span class="icon-font">access_time_filled</span> ${comment.created_timestamp}</h4>
    </div>`;
	commentList.appendChild(newCmt);
}

function updateComment(id, content) {
	let comment = document.querySelector(`[comment-id="${id}"]`);
	if (comment) {
		comment.querySelector(".comment-content").innerHTML = content;
	}
}

function deleteComment(id, self = 0) {
	if (self) {
		socket.send({
			type: typ_clt_del_cmt,
			data: { id: id },
		});
	}
	let comment = document.querySelector(`[comment-id="${id}"]`);
	comment.parentNode.removeChild(comment);
}

function postComment() {
	let input = document.getElementById("input-comment");
	XHR("POST", `/comment${window.location.pathname}`, (status, raw) => {
		let res = JSON.parse(raw);
		if(status == 200){
			let input = document.getElementById("input-comment");
			if(res.status == "Bad comment"){
				input.classList.add("error");
			} else{
				input.value = "";
				insertComment(res);
			}
		}
	}, JSON.stringify({ content: input.value }), "application/json");
}

function subscribe(channel = undefined) {
	if (!channel) channel = window.location.pathname;
	if (opened) {
		socket.send(JSON.stringify(
			{
				type: typ_clt_subs,
				data: {
					channel: channel
				}
			}
		));
		console.log("subscribe to " + window.location.pathname)
	}
}