import React from 'react';
import './App.css';

function App() {
  return (
    <React.Fragment>
      <header>
    <div id="logo">
        <div class="material-icons">
            headphones
        </div>
    </div>
    <div id="searchbar">
        <input type="text" placeholder="Search..." id="search"/>
    </div>
    <div id="setting">
        <span>Hello,...</span>
        <div id="option-button" onclick="settingpopupFunction()">
            <div class="material-icons">
                more_horiz
            </div>
        </div>
    </div>
</header>
<main>
    <div id="player-bar">
        <div id="player-control">
            <button id="skip-button" class="material-icons">
                skip_previous
            </button>

            <button id="pause-button" class="material-icons">
                pause
            </button>

            <button id="next-button" class="material-icons">
                skip_next
            </button>

            <button id="shuffle-button" class="material-icons">
                shuffle
            </button>

            <button id="replay-button" class="material-icons">
                replay
            </button>

            <div id="timer-bar">
                <div id="current-time">
                    <div class="time-text">00:00</div>
                </div>

                <div id="progress-bar">
                    <progress value="0" min="0">
                        <span id="progress-line"></span>
                    </progress>
                </div>

                <div id="total-time">
                    <div class="time-text">--:--</div>
                </div>
            </div>

            <button id="volumn-setting" class="material-icons">
                volume_up
            </button>

        </div>
        <div id="name-song">
            <div id="name-song-title">
                <div class="name-song-name-and-artist">
                    Morning coffe - Chevy
                </div>
                <div id="name-song-album">
                    Single
                </div>
            </div>
            <img src="https://i1.sndcdn.com/artworks-000488673150-u7ncn3-t240x240.jpg" id="name-song-cover"/>
        </div>
    </div>
    <div id="content">
        <div id="history-column">
            <div id="listening-history" class="content-card">
                <div class="history-tab">
                    Listening history
                </div>
                <div class="listening-history-song">
                    <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"/>
                    <div class="listening-history-info">
                        <span class="listening-history-title">
                        On the run
                    </span>
                        <br/>
                        <span class="listening-history-artist">
                        Blue Floyd
                    </span>
                    </div>
                </div>
            </div>
            <div id="artist-history" class="content-card">
                <div class="history-tab">
                    Latest from people you know
                </div>
                <div class="latest-artist">
                    <img src="https://i1.sndcdn.com/artworks-000488673150-u7ncn3-t240x240.jpg" class="latest-artist-cover"/>
                    <span class="latest-artist-name">
                        Chevy
                    </span>
                </div>
            </div>
        </div>
        <div id="main-screen">
            <div id="library" class="content-card">
                <div class="card-title">
                    Your library
                </div>
                <div class="album-list">
                    <div class="album">
                        <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"
                             class="image"/>
                        <div>
                                    <span class="album-name">
                                        The dark side of the moon
                                    </span>
                            <br/>
                            <span class="artist">
                                        Blue Floyd
                                    </span>
                        </div>
                    </div>
                    <div class="album">
                        <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"
                             class="image"/>
                        <div>
                                    <span class="album-name">
                                        The dark side of the moon
                                    </span>
                            <br/>
                            <span class="artist">
                                        Blue Floyd
                                    </span>
                        </div>
                    </div>
                    <div class="album">
                        <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"
                             class="image"/>
                        <div>
                                    <span class="album-name">
                                        The dark side of the moon
                                    </span>
                            <br/>
                            <span class="artist">
                                        Blue Floyd
                                    </span>
                        </div>
                    </div>
                    <div class="album">
                        <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"
                             class="image"/>
                        <div>
                                    <span class="album-name">
                                        The dark side of the moon
                                    </span>
                            <br/>
                            <span class="artist">
                                        Blue Floyd
                                    </span>
                        </div>
                    </div>
                    <div class="album">
                        <img src="https://media.npr.org/assets/img/2013/04/22/dark-side_sq-1da3a0a7b934f431c175c91396a1606b3adf5c83-s1100-c50.jpg"
                             class="image"/>
                        <div>
                                    <span class="album-name">
                                        The dark side of the moon
                                    </span>
                            <br/>
                            <span class="artist">
                                        Blue Floyd
                                    </span>
                        </div>
                    </div>

                </div>
            </div>
            <div id="favorite-artist" class="content-card">
                <div class="card-title">
                    Your favorite artist
                </div>
                <div class="album">
                    <div class="artist_">
                        <img src="https://i1.sndcdn.com/artworks-000488673150-u7ncn3-t240x240.jpg" class="image"/>
                        <div class="album-name">Chevy</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<footer>

</footer>

    </React.Fragment>
  );
}

export default App;
