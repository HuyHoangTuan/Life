// class ProfileHandler {
// 	static async getHandler(req, res) {
// 		let mode = getMode(req);
// 		let keyword = utils.getURLQuery(req.url, "keyword");
// 		switch (req.params["entity"]) {
// 			case "albums": {
// 				let albumList = entities.Album.searchAlbum(keyword, getToken());
// 				utils.renderPage(
// 					res,
// 					"search.ejs",
// 					{ result: { albums: albumList } },
// 					mode ? 3 : 1
// 				);
// 				break;
// 			}
// 			case "artists": {
// 				let artistList = entities.Artist.searchArtist(
// 					keyword,
// 					getToken()
// 				);
// 				utils.renderPage(
// 					res,
// 					"search.ejs",
// 					{ result: { artists: artistList } },
// 					mode ? 3 : 1
// 				);
// 				break;
// 			}
// 			case "tracks": {
// 				let trackList = entities.Track.searchTrack(keyword, getToken());
// 				utils.renderPage(
// 					res,
// 					"search.ejs",
// 					{ result: { tracks: trackList } },
// 					mode ? 3 : 1
// 				);
// 				break;
// 			}
// 			case undefined:
// 				let albumList = await entities.Album.searchAlbum(
// 					keyword,
// 					getToken()
// 				);
// 				let artistList = await entities.Artist.searchArtist(
// 					keyword,
// 					getToken()
// 				);
// 				let trackList = await entities.Track.searchTrack(
// 					keyword,
// 					getToken()
// 				);
// 				utils.renderPage(
// 					res,
// 					"search.ejs",
// 					{
// 						result: {
// 							tracks: trackList,
// 							artists: artistList,
// 							albums: albumList,
// 						},
// 					},
// 					mode ? 3 : 1
// 				);
// 				break;
// 			default:
// 				console.log(utils.getPath(req.url, 2));
// 		}
// 	}
// }
// exports.Handler = ProfileHandler;
