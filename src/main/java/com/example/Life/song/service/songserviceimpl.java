package com.example.Life.song.service;

import com.example.Life.account.repo.accountrepo;
import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.repo.songrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class songserviceimpl implements songservice
{

    class ContentScore implements Comparable<ContentScore>
    {
        long contentId;
        double score;

        public ContentScore(long contentId, double score)
        {
            this.contentId =contentId;
            this.score =score;
        }
        @Override
        public int compareTo(ContentScore o) {
            return (int)(this.score-o.score);
        }
    }

    @Autowired
    private songrepo songRepo;

    @Autowired
    private accountrepo accountRepo;

    public ArrayList<ContentScore> search(List<String[]> Data, String[] listWord, Map<String[], Long> map)
    {
        ArrayList<Double> IDF = new ArrayList<>();
        ArrayList<Double> FTD = new ArrayList<>();
        ArrayList<ContentScore> score = new ArrayList<>();


        for(String word: listWord)
        {
            int countSongs = 0;
            for(String[] sen: Data)
            {
                double wordInSen = 0;
                for(String _word : sen)
                {
                    if(word.length() <= _word.length())
                    {
                        ///System.out.println(word+" , "+_word.substring(0,word.length()-1));
                        if(word.equals(_word.substring(0,word.length()))) wordInSen++;
                    }
                }
                FTD.add(wordInSen);
                countSongs += ((wordInSen>=1) ? 1 : 0);
            }
            IDF.add(Math.log((double)Data.size()/(double) countSongs)/Math.log(2));
        }
        int col = 0;
        for(String[] sen: Data)
        {
            ///System.out.println
            double S = 0;
            double MAX = 0;
            for(int row = 0;row<listWord.length;row++)
                MAX = Math.max(MAX, FTD.get(row*Data.size()+col));

            if(MAX != 0)
            {
                for(int row = 0;row< listWord.length;row++)
                    S = S + (0.5 + 0.5 * FTD.get(row*Data.size()+col)/(double) MAX)*IDF.get(row);
            }

            col++;
            score.add(new ContentScore(map.get(sen), S));
        }
        Collections.sort(score);
        Collections.reverse(score);
        return score;
    }

    @Override
    public List<songmodel> findSong(String content)
    {
        Map<String[], Long> map = new HashMap<>();
        Map<Long, songmodel> modelMap = new HashMap<>();
        ArrayList<String[] > data =  new ArrayList<>();

        List<songmodel> listModel = songRepo.findAllSong();
        /// merge result
        for(songmodel model: listModel)
        {
            String val = model.getTrack_name().concat(" ")
                    .concat(model.getArtist_name()).concat(" ")
                    .concat(model.getTitle()).concat(" ").toLowerCase();
            data.add(val.split(" "));
            map.put(data.get(data.size()-1), model.getTrack_id());
            modelMap.put(model.getTrack_id(), model);
        }
        /// split words
        String[] wordList = content.split(" ");

        /// search
        ArrayList<ContentScore> score = search(data, wordList, map);

        /// output
        List<songmodel> output = new ArrayList<>();
        for(ContentScore contentScore : score)
        {
            output.add(modelMap.get(contentScore.contentId));
        }
        return output;
    }

    @Override
    public songmodel getSong(long songId)
    {
        List<songmodel> output = songRepo.findSongById(songId);
        if(output.size() == 0) return null;

        return output.get(0);
    }

    @Override
    public boolean deleteSong(long songId)
    {
        List<songmodel> output = songRepo.findSongById(songId);
        if (output.size() == 0) return false;
        songmodel currentSong = output.get(0);
        songRepo.deleteSongById(currentSong.getTrack_id());
        return true;
    }

    @Override
    public List<?> findSongInAlbum(long album_id)
    {
        return songRepo.findSongByAlbum(album_id);
    }

    @Override
    public song save(song newSong)
    {
        return songRepo.save(newSong);
    }
}
