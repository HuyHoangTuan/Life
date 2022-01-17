var player;
var songSlider;
var volSilder;

function initPlayer(){
    player = document.createElement("AUDIO");
    player.volume = 0.05;
    player.onpause = () => {
        stopSlider(songSlider);
        console.log("Player paused");
    };
    player.onplay = () => {
        updateSongSlider();
        console.log("Player is playing");
    };
    player.onseeked = () => {
        
        if(player.paused){
            stopSlider(songSlider);
        } else {
            updateSongSlider();
        }
        console.log("Seek");
    };
    songSlider = document.getElementById("slider-song");
}

function playSong(element) {
  var songId = element.getAttribute("song-id");
  var test = `https://archive.org/download/TheBeatlesAbbeyRoadMONO/11%20Mean%20Mr.%20Mustard.mp3`;



  // var test = `http://192.168.192.50:8080/api/song?id=${songId}`;
  if (player == undefined) {
      initPlayer();
  }
  player.src = test;
  player.addEventListener("loadeddata", () => {
    if (player.readyState >= 2) {
      player.play();
    } else {
      console.log("FAILED");
    }
  });
  player.load();
}

function toggleMusic() {
  if (player != undefined) {
    if(player.paused){
        player.play();
    } else {
        player.pause();
    }
  }
}

function updateSongSlider(){
    setSliderTime(songSlider, player.duration - player.currentTime);
}

function seek(value){
    player.currentTime = (value * player.duration) / 100;
    //updateSongSlider();
}