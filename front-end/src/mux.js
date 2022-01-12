class Router {
    constructor(){
        this.subrouters = []
    }

    map(path, handler){
        
    }

    handler(req, res){

    }

    Subrouter(){
        let subrouter = new Router()
        this.subrouters.push(subrouter)
        return subrouter
    }
}

exports.Router = Router