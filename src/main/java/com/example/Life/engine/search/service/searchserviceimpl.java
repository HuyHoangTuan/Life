package com.example.Life.engine.search.service;

import com.example.Life.LifeApplication;
import com.example.Life.account.repo.accountrepo;
import com.example.Life.album.repo.albumrepo;
import com.example.Life.engine.search.model.ContentScore;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import com.example.Life.song.repo.songrepo;
import com.example.Life.song.service.songservice;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.util.*;

@Service
public class searchserviceimpl implements searchservice
{
    @Autowired
    private songrepo songRepo;

    private List<ContentScore> search(String[] listWords, List<String[]> listContents, Map<String[], Long> map)
    {
        ArrayList<Double> IDF = new ArrayList<>();
        ArrayList<Double> FTD = new ArrayList<>();
        ArrayList<ContentScore> score = new ArrayList<>();


        for(String word: listWords)
        {
            int countSongs = 0;
            for(String[] sen: listContents)
            {
                double wordInSen = 0;
                for(String _word : sen)
                {
                    if(word.length() <= _word.length())
                    {

                        if(word.equals(_word.substring(0,word.length()))) wordInSen++;
                    }
                }
                FTD.add(wordInSen);
                countSongs += ((wordInSen>=1) ? 1 : 0);
            }
            IDF.add(Math.log((double)listContents.size()/(double) countSongs)/Math.log(2));
        }
        int col = 0;
        for(String[] sen: listContents)
        {
            ///System.out.println
            double S = 0;
            double MAX = 0;
            for(int row = 0;row<listWords.length;row++)
                MAX = Math.max(MAX, FTD.get(row*listContents.size()+col));

            if(MAX != 0)
            {
                for(int row = 0;row< listWords.length;row++)
                    S = S + (0.5 + 0.5 * FTD.get(row*listContents.size()+col)/(double) MAX)*IDF.get(row);
            }

            ///System.out.println(map.get(sen)+", "+sen);
            col++;
            score.add(new ContentScore(map.get(sen), S));
        }
        Collections.sort(score);
        Collections.reverse(score);
        return score;
    }
    @Override
    public List<?> searchSongWith(String content, int index)
    {
        String[] listWords = content.toLowerCase().split(" ");
        List<String[]> listContents = new ArrayList<>();
        Map<String[], Long> map =  new HashMap<>();
        List<songmodel> listSongs = songRepo.findAllSongs();
        for(songmodel currentSong : listSongs)
        {
            if(currentSong.getActive() == false) continue;
            String s = currentSong.getTrack_name().concat(currentSong.getArtist_name()).toLowerCase();
            listContents.add(s.split(" "));
            map.put(listContents.get(listContents.size()-1), currentSong.getTrack_id());
        }
        List<ContentScore> result = search(listWords, listContents, map);

        int perPage = 20;
        int fromIndex = perPage*(index-1);
        int toIndex = Math.min(perPage*index-1, result.size()-1);
        if(fromIndex>toIndex) return null;
        List<songoutputmodel> output = new ArrayList<>();
        for(int i=fromIndex; i<=toIndex;i++)
        {
            songmodel currentSong = songRepo.findSongById(result.get(i).getContentId()).get(0);
            if(currentSong == null) continue;
            songoutputmodel songOutput = new songoutputmodel();
            songOutput.setAlbum_id(currentSong.getAlbum_id());
            songOutput.setArtist_id(currentSong.getArtist_id());
            songOutput.setArtist_name(currentSong.getArtist_name());
            songOutput.setTitle(currentSong.getTitle());
            songOutput.setRelease_date(currentSong.getRelease_date());
            songOutput.setTrack_name(currentSong.getTrack_name());
            songOutput.setTrack_num(currentSong.getTrack_num());
            songOutput.setTrack_id(currentSong.getTrack_id());
            songOutput.setActive(currentSong.getActive());
            String path = LifeApplication.defaultDataDir + "\\" + Long.toString(songOutput.getArtist_id()) + "\\"
                    + Long.toString(songOutput.getAlbum_id())+"\\"
                    + Long.toString(songOutput.getTrack_num())+".mp3";
            File file = new File(path);
            try {
                AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
                Map properties = audioFileFormat.properties();
                songOutput.setDuration((Long) (properties.get("duration")) * 0.000001);

            } catch (Exception e) { }
            output.add(songOutput);
        }
        return output;
    }
}
