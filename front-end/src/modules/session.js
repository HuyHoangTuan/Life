const session = new Map();

class Session {
    sid;
    token;
    user;
    socket;
    constructor(sid){
        this.sid = sid;
    }
}

exports.getSession = (sid) => {
    if(session.has(sid)){
        return session.get(sid);
    } else {
        return null;
    }
}